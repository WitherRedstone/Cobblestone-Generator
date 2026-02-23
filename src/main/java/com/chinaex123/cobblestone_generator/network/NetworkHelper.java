package com.chinaex123.cobblestone_generator.network;

import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class NetworkHelper {
    /**
     * 简单的网络同步方法
     * 通知客户端方块状态已改变
     */
    public static void syncBlockEntity(Level level, BlockPos pos, BlockState state) {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    /**
     * 检查是否需要进行网络同步
     */
    public static boolean shouldSync(Level level) {
        return level != null && !level.isClientSide;
    }
}
