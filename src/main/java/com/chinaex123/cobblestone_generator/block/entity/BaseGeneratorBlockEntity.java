package com.chinaex123.cobblestone_generator.block.entity;

import com.chinaex123.cobblestone_generator.block.CobblestoneGeneratorBlock;
import com.chinaex123.cobblestone_generator.block.CobblestoneGeneratorTier;
import com.chinaex123.cobblestone_generator.config.CobblestoneGeneratorConfig;
import com.chinaex123.cobblestone_generator.network.NetworkHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public abstract class BaseGeneratorBlockEntity extends BlockEntity {
    protected final CobblestoneGeneratorTier tier;
    protected int generateTimer = 0;
    protected int lastProcessedSlot = 0;

    /**
     * 物品处理器，用于管理方块实体内部的物品存储。
     * 该处理器包含9个槽位，每个槽位的行为被重写以满足特定需求。
     * <p>
     * onContentsChanged: 当槽位内容发生变化时调用，标记方块实体为脏状态以触发保存。
     * isItemValid: 禁止外部向槽位插入物品，始终返回false。
     * insertItem: 禁止外部向槽位插入物品，始终返回传入的物品栈。
     */
    protected final ItemStackHandler itemHandler = new ItemStackHandler(9) {
        /**
         * 当物品处理器中的槽位内容发生变化时调用此方法。
         * 该方法会标记当前方块实体为脏状态，以便在适当的时候进行数据保存。
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
         * @param slot 要检查的槽位索引
         * @param stack 要检查的物品栈
         * @return 始终返回false，表示槽位无效
         */
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false;
        }

        /**
         * 尝试将物品栈插入到指定槽位中。
         * 此实现始终返回传入的物品栈，表示不允许任何物品插入。
         *
         * @param slot 目标槽位索引
         * @param stack 要插入的物品栈
         * @param simulate 是否为模拟操作（true表示仅检查，不实际插入）
         * @return 始终返回传入的物品栈，表示插入失败
         */
        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }
    };

    /**
     * 构造函数，初始化基础生成器方块实体。
     * 通过父类构造函数设置方块实体的基本信息，并获取当前方块的生成器等级。
     *
     * @param type 方块实体的类型
     * @param pos 方块实体在世界中的位置
     * @param state 方块实体对应方块的状态
     */
    public BaseGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tier = ((CobblestoneGeneratorBlock) state.getBlock()).getTier();
    }

    /**
     * 从NBT数据中加载方块实体的额外状态信息。
     * 此方法负责恢复生成计时器、最后处理的槽位索引以及物品处理器的库存数据。
     *
     * @param nbt 包含序列化数据的NBT标签
     * @param registries 用于反序列化的注册表提供者
     */
    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        this.generateTimer = nbt.getInt("GenerateTimer");
        this.lastProcessedSlot = nbt.getInt("LastProcessedSlot");
        itemHandler.deserializeNBT(registries, nbt.getCompound("Inventory"));
    }

    /**
     * 将方块实体的额外状态信息保存到NBT数据中。
     * 此方法负责序列化生成计时器、最后处理的槽位索引以及物品处理器的库存数据。
     *
     * @param nbt 用于存储序列化数据的NBT标签
     * @param registries 用于序列化的注册表提供者
     */
    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.putInt("GenerateTimer", this.generateTimer);
        nbt.putInt("LastProcessedSlot", this.lastProcessedSlot);
        nbt.put("Inventory", itemHandler.serializeNBT(registries));
    }

    /**
     * 标记方块实体为脏状态，并触发网络同步。
     * 此方法在方块实体状态发生变化时调用，确保客户端和服务端数据一致。
     */
    @Override
    public void setChanged() {
        super.setChanged();
        NetworkHelper.syncBlockEntity(level, worldPosition, getBlockState());
    }

    /**
     * 处理基础的圆石生成逻辑。
     * 该方法负责计时、应用配置的速度倍数、检查存储空间并生成圆石。
     * 生成的圆石会被放入物品处理器的槽位中，并更新处理状态。
     */
    protected void handleBasicGeneration() {
        // 增加生成计时器
        generateTimer++;

        // 应用速度倍数配置
        double speedMultiplier = CobblestoneGeneratorConfig.getSpeedMultiplier();
        // 计算实际的生成间隔tick数（至少为1）
        int effectiveGenerationTicks = (int) Math.max(1, tier.getGenerationTicks() / speedMultiplier);

        // 检查是否到达生成时间
        if (generateTimer >= effectiveGenerationTicks) {
            // 重置生成计时器
            generateTimer = 0;

            // 计算本次生成的圆石数量（最大64个）
            int outputCount = Math.min(tier.getOutputCount(), 64);
            // 创建圆石物品栈
            ItemStack cobblestone = new ItemStack(Items.COBBLESTONE, outputCount);

            // 检查是否有空闲空间
            boolean hasSpace = false;
            // 遍历所有9个槽位检查存储空间
            for (int i = 0; i < 9; i++) {
                ItemStack stack = itemHandler.getStackInSlot(i);
                // 如果槽位为空，或者槽位中有圆石且未满堆叠上限
                if (stack.isEmpty() ||
                        (stack.getItem() == Items.COBBLESTONE &&
                                stack.getCount() + outputCount <= stack.getMaxStackSize())) {
                    hasSpace = true;
                    break;
                }
            }

            // 生成逻辑
            if (hasSpace) {
                // 尝试9次寻找合适的槽位（轮询机制）
                for (int attempt = 0; attempt < 9; attempt++) {
                    // 计算当前要检查的槽位索引
                    int slotIndex = (lastProcessedSlot + attempt) % 9;
                    // 获取该槽位的物品栈
                    ItemStack stack = itemHandler.getStackInSlot(slotIndex);

                    // 如果槽位为空
                    if (stack.isEmpty()) {
                        // 将圆石放入该槽位
                        itemHandler.setStackInSlot(slotIndex, cobblestone.copy());
                        // 更新最后处理的槽位索引
                        lastProcessedSlot = (slotIndex + 1) % 9;
                        // 标记方块实体状态已改变
                        setChanged();
                        // 完成放置，跳出循环
                        break;
                    }
                    // 如果槽位中有圆石且可以合并
                    else if (stack.getItem() == Items.COBBLESTONE &&
                            stack.getCount() + outputCount <= stack.getMaxStackSize()) {
                        // 增加圆石数量
                        stack.grow(outputCount);
                        // 更新最后处理的槽位索引
                        lastProcessedSlot = (slotIndex + 1) % 9;
                        // 标记方块实体状态已改变
                        setChanged();
                        // 完成合并，跳出循环
                        break;
                    }
                }
            }
        }
    }

    /**
     * 处理物品的自动输出逻辑。
     * 该方法将当前方块实体中存储的圆石转移到相邻方块的物品处理器中。
     * 输出方向由配置决定，支持批量转移和槽位轮询机制。
     */
    protected void handleItemOutput() {
        // 获取配置的输出方向
        Direction outputDirection = CobblestoneGeneratorConfig.getOutputDirection();
        // 计算目标方块的位置
        BlockPos targetPos = worldPosition.relative(outputDirection);

        // 初始化目标方块实体
        BlockEntity targetBlockEntity = null;
        // 检查当前世界是否存在，避免空指针异常
        if (level != null) {
            targetBlockEntity = level.getBlockEntity(targetPos);
        }

        // 如果目标位置存在方块实体
        if (targetBlockEntity != null) {
            // 获取目标方块的物品处理能力
            var handler = level.getCapability(Capabilities.ItemHandler.BLOCK, targetPos, outputDirection.getOpposite());
            // 如果目标方块支持物品处理
            if (handler != null) {
                // 轮询9个槽位寻找可输出的圆石
                for (int slotAttempt = 0; slotAttempt < 9; slotAttempt++) {
                    // 计算当前要检查的源槽位索引（轮询机制）
                    int sourceSlot = (lastProcessedSlot + slotAttempt) % 9;
                    // 获取源槽位的物品栈
                    ItemStack stack = itemHandler.getStackInSlot(sourceSlot);

                    // 如果槽位不为空且包含圆石
                    if (!stack.isEmpty() && stack.getItem() == Items.COBBLESTONE) {
                        // 计算本次转移的最大数量（最多64个）
                        int transferCount = Math.min(stack.getCount(), 64);
                        // 创建要转移的物品栈副本
                        ItemStack toTransfer = stack.copy();
                        toTransfer.setCount(transferCount);

                        // 初始化剩余待转移的物品栈
                        ItemStack remaining = toTransfer;
                        // 遍历目标方块的所有槽位进行插入
                        for (int targetSlot = 0; targetSlot < handler.getSlots() && !remaining.isEmpty(); targetSlot++) {
                            // 检查目标槽位是否接受该物品
                            if (handler.isItemValid(targetSlot, remaining)) {
                                // 尝试将物品插入目标槽位
                                remaining = handler.insertItem(targetSlot, remaining, false);
                            }
                        }

                        // 计算实际转移的数量
                        int actuallyTransferred = transferCount - remaining.getCount();
                        // 如果有物品被成功转移
                        if (actuallyTransferred > 0) {
                            // 减少源槽位中的物品数量
                            stack.shrink(actuallyTransferred);
                            // 标记方块实体状态已改变
                            setChanged();
                            // 更新最后处理的槽位索引（轮询机制）
                            lastProcessedSlot = (sourceSlot + 1) % 9;
                            // 完成本次转移，跳出循环
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取当前方块实体的物品处理器实例。
     *
     * @return 物品处理器对象，用于管理内部物品存储
     */
    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    /**
     * 注册方块实体的能力，使其支持物品处理功能。
     * 该方法为普通生成器和特殊生成器分别注册物品处理器能力，
     * 限制只有非上方的面才能访问物品处理器。
     *
     * @param event 能力注册事件对象，用于注册方块实体能力
     */
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        // 为普通生成器注册物品处理能力
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

        // 为特殊生成器注册物品处理能力
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.SPECIAL_GENERATOR.get(),
                (be, side) -> {
                    if (side != Direction.UP) {
                        return be.getItemHandler();
                    }
                    return null;
                }
        );
    }
}
