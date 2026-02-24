package com.chinaex123.cobblestone_generator.block;

import com.chinaex123.cobblestone_generator.config.CobblestoneGeneratorConfig;

public enum CobblestoneGeneratorTier {
    STONE(4, 60),
    COPPER(8, 40),
    IRON(16, 20),
    GOLD(32, 15),
    DIAMOND(64, 10),
    EMERALD(128, 5),
    NETHERITE(256, 1),

    AMETHYST(256, 20),
    REDSTONE(64, 20);

    private final int defaultOutputAmount;
    private final int defaultGenerationTicks;

    CobblestoneGeneratorTier(int defaultOutputAmount, int defaultGenerationTicks) {
        this.defaultOutputAmount = defaultOutputAmount;
        this.defaultGenerationTicks = defaultGenerationTicks;
    }

    public int getDefaultOutputAmount() {
        return defaultOutputAmount;
    }

    public int getDefaultGenerationTicks() {
        return defaultGenerationTicks;
    }

    // 通过配置获取实际值
    public int getOutputAmount() {
        return CobblestoneGeneratorConfig.getOutputAmount(this);
    }

    // 通过配置获取实际值
    public int getGenerationTicks() {
        return CobblestoneGeneratorConfig.getGenerationTicks(this);
    }
}
