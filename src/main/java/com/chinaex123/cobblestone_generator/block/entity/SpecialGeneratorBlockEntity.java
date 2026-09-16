package com.chinaex123.cobblestone_generator.block.entity;

import com.chinaex123.cobblestone_generator.block.functions.AmethystCobblegen;
import com.chinaex123.cobblestone_generator.block.functions.HayblockCobblegen;
import com.chinaex123.cobblestone_generator.block.functions.RedstoneCobblegen;
import com.chinaex123.cobblestone_generator.block.functions.SculkCobblegen;
import com.chinaex123.cobblestone_generator.init.CGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 特殊圆石生成器方块实体。
 * <p>
 * 继承 BaseGeneratorBlockEntity，除基础生成与输出外，
 * 还根据等级（红石、紫水晶、干草块、幽匿）执行对应特殊功能。
 */
public class SpecialGeneratorBlockEntity extends BaseGeneratorBlockEntity {

    public SpecialGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(CGBlockEntities.SPECIAL_GENERATOR.get(), pos, state);
    }

    /**
     * 特殊生成器方块实体的tick方法，负责处理特殊功能、圆石生成和物品输出逻辑。
     * <p>
     * 该方法根据生成器类型执行不同的特殊功能。
     *
     * @param level 当前方块所在的世界对象
     * @param pos 当前方块的位置坐标
     * @param state 当前方块的状态
     * @param blockEntity 当前的特殊生成器方块实体实例
     */
    public static void tick(Level level, BlockPos pos, BlockState state, SpecialGeneratorBlockEntity blockEntity) {
        if (level.isClientSide) return;

        // 处理特殊功能
        switch (blockEntity.tier) {
            case REDSTONE:
                RedstoneCobblegen.handleRedstoneSignal(level, pos, state, blockEntity);
                break;
            case AMETHYST:
                AmethystCobblegen.handleAmethystFunction(level, pos);
                break;
            case HAYBLOCK:
                HayblockCobblegen.handleHayblockFunction(level, pos);
                break;
            case SCULK:
                SculkCobblegen.handleSculkFunction(level, pos);
                break;
        }

        // 处理基础生成逻辑
        blockEntity.handleBasicGeneration();

        // 处理物品输出
        blockEntity.handleItemOutput();
    }
}
