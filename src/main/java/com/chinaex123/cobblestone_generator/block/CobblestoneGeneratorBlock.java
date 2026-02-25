package com.chinaex123.cobblestone_generator.block;

import com.chinaex123.cobblestone_generator.block.entity.NormalGeneratorBlockEntity;
import com.chinaex123.cobblestone_generator.block.entity.ModBlockEntities;
import com.chinaex123.cobblestone_generator.block.entity.SpecialGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CobblestoneGeneratorBlock extends BaseEntityBlock {
    public static final IntegerProperty POWER = BlockStateProperties.POWER;
    private final CobblestoneGeneratorTier tier;

    /**
     * 获取方块的编解码器，用于序列化和反序列化方块状态。
     *
     * @return 方块的MapCodec编解码器实例
     */
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(properties -> new CobblestoneGeneratorBlock(properties, CobblestoneGeneratorTier.STONE));
    }

    /**
     * 定义方块的状态属性。
     * 添加红石信号强度属性到方块状态定义中。
     *
     * @param builder 方块状态定义构建器
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWER); // 添加红石信号属性
        super.createBlockStateDefinition(builder);
    }

    /**
     * 判断该方块是否可以作为红石信号源。
     *
     * @param state 当前方块状态
     * @return 始终返回true，表示该方块可以作为信号源
     */
    @Override
    public boolean isSignalSource(@NotNull BlockState state) {
        // 红石等级的方块可以作为信号源
        return true;
    }

    /**
     * 获取方块向指定方向发出的红石信号强度。
     *
     * @param blockState 当前方块状态
     * @param blockAccess 方块访问器
     * @param pos 当前方块位置
     * @param side 信号传输的方向
     * @return 红石信号强度值（0-15）
     */
    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        // 返回红石信号强度
        return blockState.getValue(POWER);
    }

    /**
     * 构造函数，创建圆石生成器方块实例。
     *
     * @param properties 方块的基本属性
     * @param tier 圆石生成器的等级类型
     */
    public CobblestoneGeneratorBlock(Properties properties, CobblestoneGeneratorTier tier) {
        super(properties);
        this.tier = tier;
    }

    /**
     * 获取当前圆石生成器的等级。
     *
     * @return 圆石生成器的等级枚举值
     */
    public CobblestoneGeneratorTier getTier() {
        return tier;
    }

    /**
     * 创建新的方块实体实例。
     *
     * @param pos 方块实体的位置
     * @param state 方块的状态
     * @return 新创建的普通生成器方块实体
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NormalGeneratorBlockEntity(pos, state);
    }

    /**
     * 获取方块的渲染形状。
     *
     * @param state 当前方块状态
     * @return 方块的渲染形状（使用模型渲染）
     */
    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    /**
     * 处理玩家无物品交互圆石生成器方块的逻辑。
     * 玩家右键点击方块时，从生成器中提取圆石到玩家背包或掉落。
     *
     * @param state 当前方块状态
     * @param level 当前世界对象
     * @param pos 方块位置
     * @param player 交互的玩家
     * @param hitResult 点击结果信息
     * @return 交互结果枚举值
     */
    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof NormalGeneratorBlockEntity generatorBE)) {
            return InteractionResult.SUCCESS;
        }

        // 提取物品逻辑...
        for (int i = 0; i < 9; i++) {
            ItemStack stack = generatorBE.getItemHandler().getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == Items.COBBLESTONE) {
                int extractCount = Math.min(stack.getCount(), 64);
                ItemStack toGive = stack.copy();
                toGive.setCount(extractCount);

                if (!player.getInventory().add(toGive)) {
                    player.drop(toGive, false);
                }

                stack.shrink(extractCount);
                generatorBE.setChanged(); // 使用封装的同步方法

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.SUCCESS;
    }

    /**
     * 获取方块实体的ticker回调函数，用于服务端的周期性更新。
     * 根据生成器的等级类型决定使用特殊生成器还是普通生成器的tick方法。
     *
     * @param level 当前世界对象，用于判断是否为客户端
     * @param state 当前方块的状态
     * @param blockEntityType 方块实体的类型
     * @param <T> 方块实体的泛型参数
     * @return BlockEntityTicker回调函数，客户端返回null，服务端根据生成器类型返回相应的tick方法
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return null;
        }

        // 使用switch表达式根据生成器等级判断ticker类型
        return switch (this.getTier()) {
            case REDSTONE, AMETHYST, GLOWSTONE, HAYBLOCK, SCULK ->
                    createTickerHelper(blockEntityType, ModBlockEntities.SPECIAL_GENERATOR.get(), SpecialGeneratorBlockEntity::tick);
            default ->
                    createTickerHelper(blockEntityType, ModBlockEntities.COBBLE_GENERATOR.get(), NormalGeneratorBlockEntity::tick);
        };
    }
}
