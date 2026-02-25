package com.chinaex123.cobblestone_generator.block.functions;

import com.chinaex123.cobblestone_generator.config.CobblestoneGeneratorConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class HayblockCobblegen {

    /**
     * 处理干草捆圆石生成器的特殊功能
     * 为周围指定范围内的玩家提供持续的生命恢复效果
     *
     * @param level 当前世界对象
     * @param generatorPos 干草捆圆石生成器的位置
     */
    public static void handleHayblockFunction(Level level, BlockPos generatorPos) {
        // 只在服务端执行
        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) {
            return;
        }

        // 每20t执行一次，提高执行频率
        if (serverLevel.getGameTime() % 20 != 0) {
            return;
        }

        // 从配置获取治疗范围
        double healRange = CobblestoneGeneratorConfig.getHayblockHealRange();

        // 扩大一点检测范围确保覆盖
        double expandedRange = healRange + 1.0;

        // 创建检测区域
        AABB detectionArea = new AABB(
                generatorPos.getX() - expandedRange,
                generatorPos.getY() - expandedRange,
                generatorPos.getZ() - expandedRange,
                generatorPos.getX() + expandedRange,
                generatorPos.getY() + expandedRange,
                generatorPos.getZ() + expandedRange
        );

        // 获取区域内所有玩家
        for (Player player : serverLevel.getEntitiesOfClass(Player.class, detectionArea)) {
            // 使用方块坐标计算距离
            BlockPos playerPos = player.blockPosition();
            int deltaX = Math.abs(playerPos.getX() - generatorPos.getX());
            int deltaY = Math.abs(playerPos.getY() - generatorPos.getY());
            int deltaZ = Math.abs(playerPos.getZ() - generatorPos.getZ());

            // 计算最大坐标差（Chebyshev距离）
            double distance = Math.max(Math.max(deltaX, deltaY), deltaZ);

            // 检查玩家是否在治疗范围内
            if (distance <= healRange) {
                // 给予生命恢复效果
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0, false, true));
            }
        }
    }
}
