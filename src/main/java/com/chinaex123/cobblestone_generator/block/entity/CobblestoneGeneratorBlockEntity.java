package com.chinaex123.cobblestone_generator.block.entity;

import com.chinaex123.cobblestone_generator.block.CobblestoneGeneratorBlock;
import com.chinaex123.cobblestone_generator.block.CobblestoneGeneratorTier;
import com.chinaex123.cobblestone_generator.config.CobblestoneGeneratorConfig;
import com.chinaex123.cobblestone_generator.config.CobblestoneGeneratorConfig.RedstoneSignalMode;
import com.chinaex123.cobblestone_generator.network.NetworkHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class CobblestoneGeneratorBlockEntity extends BlockEntity {
    private final CobblestoneGeneratorTier tier;
    private int generateTimer = 0;
    private int lastProcessedSlot = 0;

    /**
     * 物品处理器，用于管理方块实体内部的物品存储。
     * 该处理器包含9个槽位，用于存储生成的圆石。
     * <p>
     * 重写了以下方法：
     * - onContentsChanged: 当槽位内容发生变化时调用，标记方块实体为脏数据以触发保存。
     * - isItemValid: 禁止外部向槽位插入物品，始终返回false。
     * - insertItem: 禁止外部向槽位插入物品，直接返回传入的物品栈。
     */
    private final ItemStackHandler itemHandler = new ItemStackHandler(9) {
        /**
         * 当物品槽位的内容发生变化时调用此方法。
         * 用于标记方块实体的数据已更改，触发数据保存机制。
         *
         * @param slot 发生变化的槽位索引
         */
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        /**
         * 检查指定槽位是否可以接受给定的物品栈。
         * 此实现始终返回false，表示不允许任何物品插入到槽位中。
         *
         * @param slot 槽位索引
         * @param stack 要检查的物品栈
         * @return 始终返回false，表示不允许插入物品
         */
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false;
        }

        /**
         * 尝试将物品栈插入到指定槽位中。
         * 此实现直接返回传入的物品栈，表示不允许插入任何物品。
         *
         * @param slot 目标槽位索引
         * @param stack 要插入的物品栈
         * @param simulate 是否为模拟操作（true表示模拟，false表示实际操作）
         * @return 始终返回传入的物品栈，表示插入失败
         */
        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }
    };

    /**
     * 构造函数，用于初始化圆石生成器方块实体。
     * 设置方块实体的位置、状态，并获取对应的生成器等级。
     *
     * @param pos 方块实体在世界中的位置
     * @param state 方块实体对应方块的状态
     */
    public CobblestoneGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COBBLE_GENERATOR.get(), pos, state);
        this.tier = ((CobblestoneGeneratorBlock) state.getBlock()).getTier();
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        this.generateTimer = nbt.getInt("GenerateTimer");
        this.signalTimer = nbt.getInt("SignalTimer");
        this.lastProcessedSlot = nbt.getInt("LastProcessedSlot");
        itemHandler.deserializeNBT(registries, nbt.getCompound("Inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.putInt("GenerateTimer", this.generateTimer);
        nbt.putInt("SignalTimer", this.signalTimer);
        nbt.putInt("LastProcessedSlot", this.lastProcessedSlot);
        nbt.put("Inventory", itemHandler.serializeNBT(registries));
    }

    @Override
    public void setChanged() {
        super.setChanged();
        // 使用工具类进行同步
        NetworkHelper.syncBlockEntity(level, worldPosition, getBlockState());
    }

    private void triggerSync() {
        if (NetworkHelper.shouldSync(level)) {
            NetworkHelper.syncBlockEntity(level, worldPosition, getBlockState());
        }
    }


    // 红石信号更新方法
    public static void updateRedstoneSignal(Level level, BlockPos pos, BlockState state, CobblestoneGeneratorBlockEntity blockEntity) {
        if (blockEntity.tier != CobblestoneGeneratorTier.REDSTONE) {
            return;
        }

        CobblestoneGeneratorConfig.RedstoneSignalMode signalMode = CobblestoneGeneratorConfig.getRedstoneSignalMode();
        int signalInterval = CobblestoneGeneratorConfig.getRedstoneSignalInterval();

        switch (signalMode) {
            case CONTINUOUS:
                // CONTINUOUS模式：始终保持15级信号
                updateSignalIfDifferent(level, pos, state, 15);
                break;

            case INTERVAL:
                // INTERVAL模式：按配置间隔产生脉冲信号（在tick方法中处理）
                break;
        }
    }


    // 辅助方法：只在信号不同时才更新
    private static void updateSignalIfDifferent(Level level, BlockPos pos, BlockState state, int targetPower) {
        int currentPower = state.getValue(CobblestoneGeneratorBlock.POWER);
        if (currentPower != targetPower) {
            level.setBlock(pos, state.setValue(CobblestoneGeneratorBlock.POWER, targetPower), 3);
        }
    }

    // 辅助方法：检查是否有圆石
    private static boolean hasCobblestoneInInventory(CobblestoneGeneratorBlockEntity blockEntity) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = blockEntity.itemHandler.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == Items.COBBLESTONE) {
                return true;
            }
        }
        return false;
    }

    private int signalTimer = 0;

    @Override
    public void onLoad() {
        super.onLoad();
        // 初始化随机偏移，避免所有方块同步
        this.signalTimer = level.random.nextInt(10);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CobblestoneGeneratorBlockEntity blockEntity) {
        if (level.isClientSide) return;

        blockEntity.generateTimer++;
        blockEntity.signalTimer++;

        CobblestoneGeneratorConfig.RedstoneSignalMode signalMode = CobblestoneGeneratorConfig.getRedstoneSignalMode();
        int signalInterval = CobblestoneGeneratorConfig.getRedstoneSignalInterval();

        if (signalMode == CobblestoneGeneratorConfig.RedstoneSignalMode.CONTINUOUS) {
            updateRedstoneSignal(level, pos, state, blockEntity);
        }

        // INTERVAL模式：使用独立计时器确保同步
        if (signalMode == CobblestoneGeneratorConfig.RedstoneSignalMode.INTERVAL) {
            if (blockEntity.signalTimer >= signalInterval) {
                blockEntity.signalTimer = 0;
                level.setBlock(pos, state.setValue(CobblestoneGeneratorBlock.POWER, 15), 3);
            }
            else if (blockEntity.signalTimer == 1) {
                level.setBlock(pos, state.setValue(CobblestoneGeneratorBlock.POWER, 0), 3);
            }
        }

        // 应用速度倍数配置
        double speedMultiplier = CobblestoneGeneratorConfig.getSpeedMultiplier();
        int effectiveGenerationTicks = (int) Math.max(1, blockEntity.tier.getGenerationTicks() / speedMultiplier);

        if (blockEntity.generateTimer >= effectiveGenerationTicks) {
            blockEntity.generateTimer = 0;

            int outputAmount = Math.min(blockEntity.tier.getOutputAmount(), 64);
            ItemStack cobblestone = new ItemStack(Items.COBBLESTONE, outputAmount);

            // 检查是否有空闲空间
            boolean hasSpace = false;
            for (int i = 0; i < 9; i++) {
                ItemStack stack = blockEntity.itemHandler.getStackInSlot(i);
                if (stack.isEmpty() ||
                        (stack.getItem() == Items.COBBLESTONE &&
                                stack.getCount() + outputAmount <= stack.getMaxStackSize())) {
                    hasSpace = true;
                    break;
                }
            }

            // 生成逻辑
            if (hasSpace) {
                boolean itemGenerated = false;
                for (int attempt = 0; attempt < 9; attempt++) {
                    int slotIndex = (blockEntity.lastProcessedSlot + attempt) % 9;
                    ItemStack stack = blockEntity.itemHandler.getStackInSlot(slotIndex);

                    if (stack.isEmpty()) {
                        blockEntity.itemHandler.setStackInSlot(slotIndex, cobblestone.copy());
                        blockEntity.lastProcessedSlot = (slotIndex + 1) % 9;
                        blockEntity.setChanged();
                        itemGenerated = true;
                        break;
                    } else if (stack.getItem() == Items.COBBLESTONE &&
                            stack.getCount() + outputAmount <= stack.getMaxStackSize()) {
                        stack.grow(outputAmount);
                        blockEntity.lastProcessedSlot = (slotIndex + 1) % 9;
                        blockEntity.setChanged();
                        itemGenerated = true;
                        break;
                    }
                }
            }
        }

        // 输出逻辑
        Direction outputDirection = CobblestoneGeneratorConfig.getOutputDirection();
        BlockPos targetPos = pos.relative(outputDirection);

        var targetBlockEntity = level.getBlockEntity(targetPos);
        if (targetBlockEntity != null) {
            var handler = level.getCapability(Capabilities.ItemHandler.BLOCK, targetPos, outputDirection.getOpposite());
            if (handler != null) {
                for (int slotAttempt = 0; slotAttempt < 9; slotAttempt++) {
                    int sourceSlot = (blockEntity.lastProcessedSlot + slotAttempt) % 9;
                    ItemStack stack = blockEntity.itemHandler.getStackInSlot(sourceSlot);

                    if (!stack.isEmpty() && stack.getItem() == Items.COBBLESTONE) {
                        int transferAmount = Math.min(stack.getCount(), 64);
                        ItemStack toTransfer = stack.copy();
                        toTransfer.setCount(transferAmount);

                        ItemStack remaining = toTransfer;
                        for (int targetSlot = 0; targetSlot < handler.getSlots() && !remaining.isEmpty(); targetSlot++) {
                            if (handler.isItemValid(targetSlot, remaining)) {
                                remaining = handler.insertItem(targetSlot, remaining, false);
                            }
                        }

                        int actuallyTransferred = transferAmount - remaining.getCount();
                        if (actuallyTransferred > 0) {
                            stack.shrink(actuallyTransferred);
                            blockEntity.setChanged();
                            blockEntity.lastProcessedSlot = (sourceSlot + 1) % 9;
                            break;
                        }
                    }
                }
            }
        }
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.COBBLE_GENERATOR.get(),
                (be, side) -> {
                    if (side != Direction.UP) {
                        return be.getItemHandler();
                    }
                    return null;
                }
        );
    }
}
