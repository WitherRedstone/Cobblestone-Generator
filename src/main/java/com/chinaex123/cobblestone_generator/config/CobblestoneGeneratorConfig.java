package com.chinaex123.cobblestone_generator.config;

import com.chinaex123.cobblestone_generator.block.CobblestoneGeneratorTier;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.common.ModConfigSpec;

public class CobblestoneGeneratorConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // 全局配置
    public static final ModConfigSpec.EnumValue<Direction> OUTPUT_DIRECTION;
    public static final ModConfigSpec.BooleanValue AUTO_OUTPUT_ENABLED;
    public static final ModConfigSpec.DoubleValue SPEED_MULTIPLIER;

    // 红石信号配置
    public static final ModConfigSpec.EnumValue<RedstoneSignalMode> REDSTONE_SIGNAL_MODE;
    public static final ModConfigSpec.IntValue REDSTONE_SIGNAL_INTERVAL;

    // 每个等级的配置数组
    public static final ModConfigSpec.IntValue[] OUTPUT_AMOUNTS = new ModConfigSpec.IntValue[CobblestoneGeneratorTier.values().length];
    public static final ModConfigSpec.IntValue[] GENERATION_TICKS = new ModConfigSpec.IntValue[CobblestoneGeneratorTier.values().length];

    public static final ModConfigSpec SPEC;

    // 红石信号模式枚举
    public enum RedstoneSignalMode {
        CONTINUOUS,  // 持续模式：有物品时持续保持15级信号
        INTERVAL     // 间隔模式：按设定间隔检查并发出信号
    }

    static {
        BUILDER.push("Cobblestone Generator Settings");

        // 全局配置
        OUTPUT_DIRECTION = BUILDER
                .comment("输出方向 (默认: UP)")
                .defineEnum("outputDirection", Direction.UP);

        AUTO_OUTPUT_ENABLED = BUILDER
                .comment("是否启用自动输出功能 (默认: true)")
                .define("autoOutputEnabled", true);

        SPEED_MULTIPLIER = BUILDER
                .comment("全局速度倍数 (0.1-10.0, 默认: 1.0)")
                .defineInRange("speedMultiplier", 1.0, 0.1, 10.0);

        // 红石信号配置
        BUILDER.push("redstone_settings");

        REDSTONE_SIGNAL_MODE = BUILDER
                .comment("红石信号模式 (CONTINUOUS: 持续15级信号, INTERVAL: 按间隔持续15级信号)")
                .defineEnum("redstoneSignalMode", RedstoneSignalMode.CONTINUOUS);

        REDSTONE_SIGNAL_INTERVAL = BUILDER
                .comment("红石信号间隔ticks (仅在INTERVAL模式下有效, 默认: 20)")
                .defineInRange("redstoneSignalInterval", 20, 1, 1200);

        BUILDER.pop();

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

    // 红石信号配置获取方法
    public static RedstoneSignalMode getRedstoneSignalMode() {
        return REDSTONE_SIGNAL_MODE != null ? REDSTONE_SIGNAL_MODE.get() : RedstoneSignalMode.CONTINUOUS;
    }

    public static int getRedstoneSignalInterval() {
        return REDSTONE_SIGNAL_INTERVAL != null ? REDSTONE_SIGNAL_INTERVAL.get() : 20;
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

    public static void onConfigReload() {
        // 添加配置重载时需要执行的逻辑
        System.out.println("Cobblestone Generator config reloaded");
    }
}
