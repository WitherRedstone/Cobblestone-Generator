package com.chinaex123.cobblestone_generator.block;

public enum CobblestoneGeneratorTier {
    STONE(4, 60),
    COPPER(8, 40),
    IRON(16, 20),
    GOLD(32, 15),
    DIAMOND(64, 10),
    EMERALD(128, 5),
    NETHERITE(256, 1);

    private final int outputAmount;
    private final int generationTicks;

    CobblestoneGeneratorTier(int outputAmount, int generationTicks) {
        this.outputAmount = outputAmount;
        this.generationTicks = generationTicks;
    }

    public int getOutputAmount() {
        return outputAmount;
    }

    public int getGenerationTicks() {
        return generationTicks;
    }
}
