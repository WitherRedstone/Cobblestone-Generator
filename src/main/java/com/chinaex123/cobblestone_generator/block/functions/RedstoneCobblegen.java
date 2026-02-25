package com.chinaex123.cobblestone_generator.block.functions;

import com.chinaex123.cobblestone_generator.block.CobblestoneGeneratorBlock;
import com.chinaex123.cobblestone_generator.block.entity.NormalGeneratorBlockEntity;
import com.chinaex123.cobblestone_generator.block.entity.SpecialGeneratorBlockEntity;
import com.chinaex123.cobblestone_generator.config.CobblestoneGeneratorConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class RedstoneCobblegen {

    /**
     * 处理红石圆石生成器的信号输出逻辑。
     * 根据配置的红石信号模式（持续或间隔），控制方块的红石信号强度。
     *
     * @param level 当前世界对象
     * @param pos 红石圆石生成器的位置
     * @param state 红石圆石生成器的方块状态
     * @param blockEntity 红石圆石生成器的方块实体
     */
    public static void handleRedstoneSignal(Level level, BlockPos pos, BlockState state, SpecialGeneratorBlockEntity blockEntity) {
        if (level.isClientSide) return;

        CobblestoneGeneratorConfig.RedstoneSignalMode signalMode = CobblestoneGeneratorConfig.getRedstoneSignalMode();
        int signalInterval = CobblestoneGeneratorConfig.getRedstoneSignalInterval();

        switch (signalMode) {
            case CONTINUOUS:
                // CONTINUOUS模式：始终保持15级信号
                updateSignalIfDifferent(level, pos, state, 15);
                break;

            case INTERVAL:
                // INTERVAL模式：按配置间隔产生脉冲信号
                long worldTime = level.getGameTime();

                if (worldTime % signalInterval == 0) {
                    level.setBlock(pos, state.setValue(CobblestoneGeneratorBlock.POWER, 15), 3);
                } else if (worldTime % signalInterval == 1) {
                    level.setBlock(pos, state.setValue(CobblestoneGeneratorBlock.POWER, 0), 3);
                }
                break;
        }
    }

    /**
     * 只在信号不同时才更新，避免不必要的方块更新
     * @param level 世界对象
     * @param pos 方块位置
     * @param state 方块状态
     * @param targetPower 目标信号强度
     */
    private static void updateSignalIfDifferent(Level level, BlockPos pos, BlockState state, int targetPower) {
        int currentPower = state.getValue(CobblestoneGeneratorBlock.POWER);
        if (currentPower != targetPower) {
            level.setBlock(pos, state.setValue(CobblestoneGeneratorBlock.POWER, targetPower), 3);
        }
    }

    /**
     * 检查生成器内部是否有圆石库存
     * @param blockEntity 方块实体
     * @return 是否有圆石
     */
    public static boolean hasCobblestoneInInventory(NormalGeneratorBlockEntity blockEntity) {
        for (int i = 0; i < 9; i++) {
            var stack = blockEntity.getItemHandler().getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == net.minecraft.world.item.Items.COBBLESTONE) {
                return true;
            }
        }
        return false;
    }
}
