package com.chinaex123.cobblestone_generator.block.entity;

import com.chinaex123.cobblestone_generator.init.CGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 普通圆石生成器方块实体。
 * <p>
 * 继承 BaseGeneratorBlockEntity，复用其生成、存储、输出、同步与能力注册逻辑，
 * 仅绑定普通生成器类型，并在服务端 tick 中调用生成与输出方法。
 */
public class NormalGeneratorBlockEntity extends BaseGeneratorBlockEntity {

    public NormalGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(CGBlockEntities.COBBLE_GENERATOR.get(), pos, state);
    }

    /**
     * 普通生成器方块实体的tick方法，负责处理圆石生成和物品输出逻辑。
     * 该方法每游戏刻执行一次，仅在服务端运行。
     *
     * @param level 当前方块所在的世界对象
     * @param pos 当前方块的位置坐标
     * @param state 当前方块的状态
     * @param blockEntity 当前的普通生成器方块实体实例
     */
    public static void tick(Level level, BlockPos pos, BlockState state, NormalGeneratorBlockEntity blockEntity) {
        if (level.isClientSide()) return;

        // 处理基础生成逻辑
        blockEntity.handleBasicGeneration();

        // 处理物品输出
        blockEntity.handleItemOutput();
    }
}
