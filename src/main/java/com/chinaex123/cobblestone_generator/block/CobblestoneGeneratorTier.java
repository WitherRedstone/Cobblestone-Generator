package com.chinaex123.cobblestone_generator.block;

import com.chinaex123.cobblestone_generator.config.CobblestoneGeneratorConfig;

public enum CobblestoneGeneratorTier {
    STONE(4, 60),   // 石
    COPPER(8, 40),  // 铜
    IRON(16, 20),   // 铁
    GOLD(32, 15),   // 金
    DIAMOND(64, 10),    // 钻石
    EMERALD(128, 5),    // 绿宝石
    NETHERITE(256, 1),  // 下界合金

    AMETHYST(256, 20),  // 紫水晶
    REDSTONE(64, 20),   // 红石
    GLOWSTONE(64, 20),   // 荧石
    HAYBLOCK(64, 20),   // 干草块
    SCULK(128, 20);   // 幽匿

    private final int defaultOutputCount;
    private final int defaultOutputTicks;

    CobblestoneGeneratorTier(int OutputCount, int OutputTicks) {
        this.defaultOutputCount = OutputCount;
        this.defaultOutputTicks = OutputTicks;
    }

    public int getDefaultOutputCount() {
        return defaultOutputCount;
    }

    public int getDefaultOutputTicks() {
        return defaultOutputTicks;
    }

    // 通过配置获取实际值
    public int getOutputCount() {
        return CobblestoneGeneratorConfig.getOutputCount(this);
    }

    // 通过配置获取实际值
    public int getGenerationTicks() {
        return CobblestoneGeneratorConfig.getGenerationTicks(this);
    }
}
