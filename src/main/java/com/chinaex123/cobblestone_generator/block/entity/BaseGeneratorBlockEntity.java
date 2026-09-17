package com.chinaex123.cobblestone_generator.block.entity;

import com.chinaex123.cobblestone_generator.block.CobblestoneGeneratorBlock;
import com.chinaex123.cobblestone_generator.block.CobblestoneGeneratorTier;
import com.chinaex123.cobblestone_generator.config.CobblestoneGeneratorConfig;
import com.chinaex123.cobblestone_generator.init.CGBlockEntities;
import com.chinaex123.cobblestone_generator.network.NetworkHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

/**
 * 圆石生成器方块实体基类。
 * <p>
 * 负责圆石的定时生成、9 槽位内部存储、自动输出到相邻容器、
 * 数据持久化、网络同步以及物品处理能力注册。
 * 具体生成速度与产出由等级和配置决定。
 */
public abstract class BaseGeneratorBlockEntity extends BlockEntity {
    /** 生成器等级，决定生成间隔与单次产出数量 */
    protected final CobblestoneGeneratorTier tier;
    /** 生成计时器，每 tick 自增，达到有效生成间隔后触发产出 */
    protected int generateTimer = 0;
    /** 上次处理过的槽位索引，用于轮询分配产出与输出 */
    protected int lastProcessedSlot = 0;

    /** 内部存储槽位总数 */
    private static final int TOTAL_SLOTS = 9;

    /** 内部物品存储数组，共 9 个槽位 */
    public final ItemStack[] items = new ItemStack[TOTAL_SLOTS];

    // 初始化所有槽位为空物品
    {
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            items[i] = ItemStack.EMPTY;
        }
    }

    /**
     * 物品处理能力实现。
     * <p>
     * 对外暴露内部 9 个槽位，禁止插入，仅允许提取；
     * 提取时需匹配物品与组件，并触发存档与同步。
     */
    private final ResourceHandler<@NotNull ItemResource> itemHandler = new ResourceHandler<>() {
        @Override
        public int size() {
            return TOTAL_SLOTS;
        }

        @Override
        public @NonNull ItemResource getResource(int index) {
            if (index >= 0 && index < TOTAL_SLOTS) {
                ItemStack stack = items[index];
                if (!stack.isEmpty()) {
                    return ItemResource.of(stack);
                }
            }
            return ItemResource.EMPTY;
        }

        @Override
        public long getAmountAsLong(int index) {
            if (index >= 0 && index < TOTAL_SLOTS) {
                ItemStack stack = items[index];
                return stack.isEmpty() ? 0L : stack.getCount();
            }
            return 0L;
        }

        @Override
        public long getCapacityAsLong(int index, ItemResource resource) {
            return 64L;
        }

        /** 禁止外部插入物品 */
        @Override
        public int insert(int index, ItemResource resource, int amount, @NotNull TransactionContext transaction) {
            return 0;
        }

        /** 允许提取物品，提取后更新槽位并标记变更 */
        @Override
        public int extract(int index, ItemResource resource, int amount, @NotNull TransactionContext transaction) {
            if (index >= 0 && index < TOTAL_SLOTS) {
                ItemStack current = items[index];

                if (current.isEmpty()) {
                    return 0;
                }

                if (!ItemStack.isSameItemSameComponents(current, resource.toStack(1))) {
                    return 0;
                }

                int toExtract = Math.min(amount, current.getCount());

                if (toExtract <= 0) {
                    return 0;
                }

                ItemStack remaining = current.copyWithCount(current.getCount() - toExtract);
                items[index] = remaining.isEmpty() ? ItemStack.EMPTY : remaining;

                setChanged();

                return toExtract;
            }

            return 0;
        }

        @Override
        public boolean isValid(int index, ItemResource resource) {
            return true;
        }
    };

    /**
     * 能量处理能力实现。
     * <p>
     * 该生成器不涉及能量存储，所有方法均返回 0。
     */
    private final EnergyHandler energyHandler = new EnergyHandler() {
        @Override
        public int insert(int maxReceive, @NotNull TransactionContext transaction) {
            return 0;
        }

        @Override
        public int extract(int maxExtract, @NotNull TransactionContext transaction) {
            return 0;
        }

        @Override
        public long getAmountAsLong() {
            return 0;
        }

        @Override
        public long getCapacityAsLong() {
            return 0;
        }
    };

    /**
     * 构造方块实体。
     *
     * @param type  方块实体类型
     * @param pos   方块位置
     * @param state 方块状态，用于获取对应等级
     */
    public BaseGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tier = ((CobblestoneGeneratorBlock) state.getBlock()).getTier();
    }

    /**
     * 从存档加载数据。
     * <p>
     * 读取生成计时器、上次处理槽位以及 9 个槽位的物品数据。
     */
    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        CompoundTag customData = input.read("custom_data", CompoundTag.CODEC).orElse(new CompoundTag());
        this.generateTimer = customData.getInt("GenerateTimer").orElse(0);
        this.lastProcessedSlot = customData.getInt("LastProcessedSlot").orElse(0);

        CompoundTag itemsTag = customData.getCompound("items").orElse(new CompoundTag());
        for (int i = 0; i < items.length; i++) {
            String key = "item" + i;
            int finalI = i;

            if (itemsTag.contains(key)) {
                var itemTagOpt = itemsTag.getCompound(key);
                itemTagOpt.flatMap(itemTag -> ItemStack.CODEC.parse(NbtOps.INSTANCE, itemTag)
                        .result()).ifPresent(stack -> items[finalI] = stack);
            } else {
                items[finalI] = ItemStack.EMPTY;
            }
        }
    }

    /**
     * 保存数据到存档。
     * <p>
     * 写入生成计时器、上次处理槽位以及 9 个槽位的物品数据。
     */
    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        CompoundTag customData = new CompoundTag();
        customData.putInt("GenerateTimer", this.generateTimer);
        customData.putInt("LastProcessedSlot", this.lastProcessedSlot);

        CompoundTag itemsTag = new CompoundTag();
        for (int i = 0; i < items.length; i++) {
            int finalI = i;
            ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, items[i]).result().ifPresent(itemTag -> itemsTag.put("item" + finalI, itemTag));
        }
        customData.put("items", itemsTag);

        output.store("custom_data", CompoundTag.CODEC, customData);
    }

    /**
     * 标记方块实体已变更，并触发网络同步。
     */
    @Override
    public void setChanged() {
        super.setChanged();
        NetworkHelper.syncBlockEntity(level, worldPosition, getBlockState());
    }

    /**
     * 处理基础的圆石生成逻辑。
     * <p>
     * 每 tick 递增计时器，达到有效生成间隔后尝试向内部槽位添加圆石。
     * 生成速度受配置中的速度倍率影响，产出数量由等级决定。
     * 采用轮询方式从上次处理的槽位开始寻找可存放的位置。
     */
    protected void handleBasicGeneration() {
        generateTimer++;

        double speedMultiplier = CobblestoneGeneratorConfig.SPEED_MULTIPLIER.get();
        int effectiveGenerationTicks = (int) Math.max(1, tier.getGenerationTicks() / speedMultiplier);

        if (generateTimer >= effectiveGenerationTicks) {
            generateTimer = 0;

            int outputCount = Math.min(tier.getOutputCount(), 64);
            ItemStack cobblestone = new ItemStack(Items.COBBLESTONE, outputCount);

            // 检查是否有空位或可堆叠的圆石槽位
            boolean hasSpace = false;
            for (int i = 0; i < 9; i++) {
                ItemStack stack = items[i];
                if (stack.isEmpty() ||
                        (stack.getItem() == Items.COBBLESTONE &&
                                stack.getCount() + outputCount <= stack.getMaxStackSize())) {
                    hasSpace = true;
                    break;
                }
            }

            if (hasSpace) {
                // 从上次处理槽位开始轮询，找到第一个可存放的位置
                for (int attempt = 0; attempt < 9; attempt++) {
                    int slotIndex = (lastProcessedSlot + attempt) % 9;
                    ItemStack stack = items[slotIndex];

                    if (stack.isEmpty()) {
                        items[slotIndex] = cobblestone.copy();
                        lastProcessedSlot = (slotIndex + 1) % 9;
                        setChanged();
                        break;
                    }
                    else if (stack.getItem() == Items.COBBLESTONE &&
                            stack.getCount() + outputCount <= stack.getMaxStackSize()) {
                        stack.grow(outputCount);
                        lastProcessedSlot = (slotIndex + 1) % 9;
                        setChanged();
                        break;
                    }
                }
            }
        }
    }

    /**
     * 处理向相邻容器自动输出圆石的逻辑。
     * <p>
     * 根据配置的输出方向获取相邻方块的物品能力，
     * 从上次处理槽位开始轮询，将圆石转移到目标容器中。
     * 每次最多转移 64 个，转移成功后更新槽位并标记变更。
     */
    protected void handleItemOutput() {
        Direction outputDirection = CobblestoneGeneratorConfig.OUTPUT_DIRECTION.get();
        BlockPos targetPos = worldPosition.relative(outputDirection);

        BlockEntity targetBlockEntity = null;
        if (level != null) {
            targetBlockEntity = level.getBlockEntity(targetPos);
        }

        if (targetBlockEntity != null) {
            var handler = level.getCapability(Capabilities.Item.BLOCK, targetPos, outputDirection.getOpposite());
            if (handler != null) {
                // 从上次处理槽位开始轮询，寻找可输出的圆石
                for (int slotAttempt = 0; slotAttempt < 9; slotAttempt++) {
                    int sourceSlot = (lastProcessedSlot + slotAttempt) % 9;
                    ItemStack stack = items[sourceSlot];

                    if (!stack.isEmpty() && stack.getItem() == Items.COBBLESTONE) {
                        int transferCount = Math.min(stack.getCount(), 64);
                        ItemStack toTransfer = stack.copy();
                        toTransfer.setCount(transferCount);

                        ItemStack remaining = toTransfer;
                        // 遍历目标容器的所有槽位，尝试插入
                        for (int targetSlot = 0; targetSlot < handler.size() && !remaining.isEmpty(); targetSlot++) {
                            ItemResource targetResource = handler.getResource(targetSlot);
                            if (targetResource.isEmpty() ||
                                    ItemStack.isSameItemSameComponents(remaining, targetResource.toStack(1))) {
                                try (Transaction transaction = Transaction.openRoot()) {
                                    long inserted = handler.insert(targetSlot, ItemResource.of(remaining), remaining.getCount(), transaction);
                                    if (inserted > 0) {
                                        remaining.shrink((int) inserted);
                                        transaction.commit();
                                    }
                                }
                            }
                        }

                        int actuallyTransferred = transferCount - remaining.getCount();
                        if (actuallyTransferred > 0) {
                            stack.shrink(actuallyTransferred);
                            setChanged();
                            lastProcessedSlot = (sourceSlot + 1) % 9;
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取物品处理能力。
     *
     * @return 内部物品处理器
     */
    public ResourceHandler<@NotNull ItemResource> getItemHandler() {
        return itemHandler;
    }

    /**
     * 获取能量处理能力（始终返回空实现）。
     *
     * @param side 访问方向，忽略
     * @return 能量处理器
     */
    @Nullable
    public EnergyHandler getEnergyHandler(@Nullable Direction side) {
        return energyHandler;
    }

    /**
     * 注册方块实体的能力。
     * <p>
     * 为圆石生成器和特殊生成器注册物品处理能力，
     * 除上方以外的所有方向均可访问。
     *
     * @param event 能力注册事件
     */
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                CGBlockEntities.COBBLE_GENERATOR.get(),
                (be, side) -> {
                    if (side != Direction.UP) {
                        return be.getItemHandler();
                    }
                    return null;
                }
        );

        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                CGBlockEntities.SPECIAL_GENERATOR.get(),
                (be, side) -> {
                    if (side != Direction.UP) {
                        return be.getItemHandler();
                    }
                    return null;
                }
        );
    }
}