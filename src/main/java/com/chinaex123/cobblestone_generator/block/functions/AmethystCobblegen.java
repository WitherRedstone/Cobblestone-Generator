package com.chinaex123.cobblestone_generator.block.functions;

import com.chinaex123.cobblestone_generator.config.CobblestoneGeneratorConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class AmethystCobblegen {

    /**
     * 处理紫水晶圆石生成器的特殊功能。
     * 该方法定期扫描生成器周围3x3x3区域内的紫水晶母岩，并有一定概率加速其生长。
     *
     * @param level 当前世界对象
     * @param generatorPos 紫水晶圆石生成器的位置
     */
    public static void handleAmethystFunction(Level level, BlockPos generatorPos) {
        // 每40游戏刻执行一次，控制执行频率
        if (level.getGameTime() % 40 != 0) return;

        // 3x3x3区域检测（包括自身位置，共27个位置）
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    // 跳过生成器自身位置
                    if (x == 0 && y == 0 && z == 0) continue;

                    BlockPos checkPos = generatorPos.offset(x, y, z);
                    BlockState checkState = level.getBlockState(checkPos);

                    // 检查是否为紫水晶母岩
                    if (checkState.is(Blocks.BUDDING_AMETHYST)) {
                        double speedMultiplier = CobblestoneGeneratorConfig.getAmethystGrowthSpeedMultiplier();
                        double triggerChance = 0.05 * speedMultiplier; // 降低概率因为检测范围更大

                        if (level.random.nextDouble() < triggerChance) {
                            advanceAmethystGrowth(level, checkPos, checkState);
                        }
                    }
                }
            }
        }
    }

    /**
     * 推进紫水晶母岩的生长过程。
     * 该方法检查母岩六个面的相邻方块，要么生成新的紫水晶芽，要么推进现有芽的生长阶段。
     *
     * @param level 当前世界对象
     * @param pos 紫水晶母岩的位置
     * @param buddingState 紫水晶母岩的方块状态
     */
    private static void advanceAmethystGrowth(Level level, BlockPos pos, BlockState buddingState) {
        // 仍然是检查6个面，但检测范围扩大了
        for (Direction direction : Direction.values()) {
            BlockPos crystalPos = pos.relative(direction);
            BlockState adjacentState = level.getBlockState(crystalPos);

            // 生成新芽
            if (adjacentState.isAir()) {
                if (level.random.nextDouble() < 0.3) { // 降低概率避免过于频繁
                    createNewAmethystBud(level, pos, direction);
                }
            }
            // 推进现有芽
            else {
                Block adjacentBlock = adjacentState.getBlock();
                if (adjacentBlock == Blocks.SMALL_AMETHYST_BUD ||
                        adjacentBlock == Blocks.MEDIUM_AMETHYST_BUD ||
                        adjacentBlock == Blocks.LARGE_AMETHYST_BUD) {

                    if (level.random.nextDouble() < 0.2) { // 降低概率
                        Block newCrystalBlock = getNextAmethystStage(adjacentBlock);
                        if (newCrystalBlock != adjacentBlock) {
                            BlockState newState = newCrystalBlock.defaultBlockState()
                                    .setValue(AmethystClusterBlock.FACING,
                                            adjacentState.getValue(AmethystClusterBlock.FACING))
                                    .setValue(AmethystClusterBlock.WATERLOGGED,
                                            adjacentState.getValue(AmethystClusterBlock.WATERLOGGED));

                            level.setBlock(crystalPos, newState, 3);
                            level.playSound(null, crystalPos, SoundEvents.AMETHYST_CLUSTER_BREAK,
                                    SoundSource.BLOCKS, 0.6f, 1.5f);
                        }
                    }
                }
            }
        }
    }

    /**
     * 在指定位置创建新的紫水晶芽。
     * 该方法根据母岩面朝向创建相应朝向的小型紫水晶芽，并播放放置音效。
     *
     * @param level 当前世界对象
     * @param motherPos 紫水晶母岩的位置
     * @param face 母岩的面朝向，决定芽的生成位置和朝向
     */
    private static void createNewAmethystBud(Level level, BlockPos motherPos, Direction face) {
        BlockPos budPos = motherPos.relative(face);

        BlockState budState = Blocks.SMALL_AMETHYST_BUD.defaultBlockState()
                .setValue(AmethystClusterBlock.FACING, face)
                .setValue(AmethystClusterBlock.WATERLOGGED, false);

        if (budState.canSurvive(level, budPos)) {
            level.setBlock(budPos, budState, 3);
            level.playSound(null, budPos, SoundEvents.AMETHYST_CLUSTER_PLACE,
                    SoundSource.BLOCKS, 0.8f, 1.0f);
        }
    }

    /**
     * 获取紫水晶生长的下一个阶段方块。
     * 该方法根据当前紫水晶芽的阶段，返回进化后的下一阶段方块类型。
     *
     * @param currentStage 当前紫水晶芽的方块类型
     * @return 下一阶段的紫水晶方块类型，如果已是最终阶段则返回原方块
     */
    private static Block getNextAmethystStage(Block currentStage) {
        if (currentStage == Blocks.SMALL_AMETHYST_BUD) {
            return Blocks.MEDIUM_AMETHYST_BUD;
        } else if (currentStage == Blocks.MEDIUM_AMETHYST_BUD) {
            return Blocks.LARGE_AMETHYST_BUD;
        } else if (currentStage == Blocks.LARGE_AMETHYST_BUD) {
            return Blocks.AMETHYST_CLUSTER;
        }
        return currentStage;
    }
}
