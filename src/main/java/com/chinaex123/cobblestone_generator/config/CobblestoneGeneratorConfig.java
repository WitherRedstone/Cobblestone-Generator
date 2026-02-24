package com.chinaex123.cobblestone_generator.config;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

import com.chinaex123.cobblestone_generator.block.CobblestoneGeneratorTier;

public class CobblestoneGeneratorConfig {
    private static final Builder BUILDER = new Builder();

    // 全局设置
    public static final ModConfigSpec.EnumValue<Direction> OUTPUT_DIRECTION;
    public static final ModConfigSpec.BooleanValue AUTO_OUTPUT_ENABLED;
    public static final ModConfigSpec.DoubleValue SPEED_MULTIPLIER;

    // 每个等级的独立配置
    public static final ModConfigSpec.IntValue[] OUTPUT_AMOUNTS = new IntValue[CobblestoneGeneratorTier.values().length];
    public static final ModConfigSpec.IntValue[] GENERATION_TICKS = new IntValue[CobblestoneGeneratorTier.values().length];

    public static final ModConfigSpec SPEC;

    static {
        BUILDER.push("Cobblestone Generator Settings");

        // 全局配置
        OUTPUT_DIRECTION = BUILDER
                .comment("设置圆石生成机的输出方向 (默认: UP)")
                .defineEnum("outputDirection", Direction.UP);

        AUTO_OUTPUT_ENABLED = BUILDER
                .comment("是否启用自动输出功能 (默认: true)")
                .define("autoOutputEnabled", true);

        SPEED_MULTIPLIER = BUILDER
                .comment("全局速度倍数 (0.1-10.0, 默认: 1.0)")
                .defineInRange("speedMultiplier", 1.0, 0.1, 10.0);

        // 每个等级的配置
        BUILDER.push("tier_settings");
        CobblestoneGeneratorTier[] tiers = CobblestoneGeneratorTier.values();
        for (int i = 0; i < tiers.length; i++) {
            CobblestoneGeneratorTier tier = tiers[i];
            String tierName = tier.name().toLowerCase();

            BUILDER.push(tierName);

            OUTPUT_AMOUNTS[i] = BUILDER
                    .comment("每次输出数量 (默认: " + tier.getDefaultOutputAmount() + ")")
                    .defineInRange("outputAmount", tier.getDefaultOutputAmount(), 1, 1024);

            GENERATION_TICKS[i] = BUILDER
                    .comment("生成间隔ticks (默认: " + tier.getDefaultGenerationTicks() + ")")
                    .defineInRange("generationTicks", tier.getDefaultGenerationTicks(), 1, 2400);

            BUILDER.pop();
        }
        BUILDER.pop();

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    // 全局配置获取方法
    public static Direction getOutputDirection() {
        return OUTPUT_DIRECTION != null ? OUTPUT_DIRECTION.get() : Direction.UP;
    }

    public static boolean isAutoOutputEnabled() {
        return AUTO_OUTPUT_ENABLED != null ? AUTO_OUTPUT_ENABLED.get() : true;
    }

    public static double getSpeedMultiplier() {
        return SPEED_MULTIPLIER != null ? SPEED_MULTIPLIER.get() : 1.0;
    }

    // 等级配置获取方法
    public static int getOutputAmount(CobblestoneGeneratorTier tier) {
        int index = tier.ordinal();
        if (index < OUTPUT_AMOUNTS.length && OUTPUT_AMOUNTS[index] != null) {
            return OUTPUT_AMOUNTS[index].get();
        }
        return tier.getDefaultOutputAmount();
    }

    public static int getGenerationTicks(CobblestoneGeneratorTier tier) {
        int index = tier.ordinal();
        if (index < GENERATION_TICKS.length && GENERATION_TICKS[index] != null) {
            return GENERATION_TICKS[index].get();
        }
        return tier.getDefaultGenerationTicks();
    }

    // 配置重载回调方法
    public static void onConfigReload() {
        // 添加配置重载时需要执行的逻辑
        System.out.println("Cobblestone Generator config reloaded");
    }

    // 注册方法
    public static void registerConfig(net.neoforged.fml.ModContainer container) {
        container.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, SPEC);
    }
}
