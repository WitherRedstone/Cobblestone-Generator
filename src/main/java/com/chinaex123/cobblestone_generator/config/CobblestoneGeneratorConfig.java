package com.chinaex123.cobblestone_generator.config;

import com.chinaex123.cobblestone_generator.block.CobblestoneGeneratorTier;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.List;

public class CobblestoneGeneratorConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // 全局配置
    public static final ModConfigSpec.EnumValue<Direction> OUTPUT_DIRECTION;
    public static final ModConfigSpec.BooleanValue AUTO_OUTPUT_ENABLED;
    public static final ModConfigSpec.DoubleValue SPEED_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue AMETHYST_GROWTH_SPEED_MULTIPLIER;
    public static final ModConfigSpec.EnumValue<RedstoneSignalMode> REDSTONE_SIGNAL_MODE;
    public static final ModConfigSpec.IntValue REDSTONE_SIGNAL_INTERVAL;
    public static final ModConfigSpec.DoubleValue HAYBLOCK_HEAL_RANGE;
    public static final ModConfigSpec.IntValue HAYBLOCK_REGENERATION_LEVEL;
    public static final ModConfigSpec.IntValue HAYBLOCK_REGENERATION_DURATION;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> SCULK_TARGET_BLOCKS;
    public static final ModConfigSpec.IntValue SCULK_CONVERSION_RADIUS;
    public static final ModConfigSpec.IntValue SCULK_CONVERSION_CHANCE;


    // 每个等级的配置数组
    public static final ModConfigSpec.IntValue[] OUTPUT_COUNTS = new ModConfigSpec.IntValue[CobblestoneGeneratorTier.values().length];
    public static final ModConfigSpec.IntValue[] GENERATION_TICKS = new ModConfigSpec.IntValue[CobblestoneGeneratorTier.values().length];

    public static final ModConfigSpec SPEC;

    // 红石信号模式枚举
    public enum RedstoneSignalMode {
        /** 持续模式：有物品时持续保持15级信号 */
        CONTINUOUS,
        /** 间隔模式：按设定间隔检查并发出信号 */
        INTERVAL
    }

    static {
        BUILDER.comment("通用配置").push("Common Config");
        OUTPUT_DIRECTION = BUILDER
                .comment("输出方向")
                .comment("Output direction")
                .defineEnum("outputDirection", Direction.UP);
        AUTO_OUTPUT_ENABLED = BUILDER
                .comment("是否启用自动输出功能")
                .comment("Whether to enable the automatic output function.")
                .define("autoOutputEnabled", true);
        SPEED_MULTIPLIER = BUILDER
                .comment("全局速度倍数")
                .comment("Global speed multiplier.")
                .defineInRange("speedMultiplier", 1.0, 0.1, Integer.MAX_VALUE);
        BUILDER.pop();


        BUILDER.comment("圆石生成器").push("Cobblestone Generator");

        BUILDER.comment("石圆石生成器").push("Stone Cobblegen");
        OUTPUT_COUNTS[0] = BUILDER
                .comment("每次输出数量")
                .comment("Output quantity per time")
                .defineInRange("outputCount", 4, 1, Integer.MAX_VALUE);
        GENERATION_TICKS[0] = BUILDER
                .comment("生成间隔（tick）")
                .comment("Spawn interval (ticks)")
                .defineInRange("generationTicks", 60, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("铜圆石生成器").push("Copper Cobblegen");
        OUTPUT_COUNTS[1] = BUILDER
                .comment("每次输出数量")
                .comment("Output quantity per time")
                .defineInRange("outputCount", 8, 1, Integer.MAX_VALUE);
        GENERATION_TICKS[1] = BUILDER
                .comment("生成间隔（tick）")
                .comment("Spawn interval (ticks)")
                .defineInRange("generationTicks", 40, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("铁圆石生成器").push("Iron Cobblegen");
        OUTPUT_COUNTS[2] = BUILDER
                .comment("每次输出数量")
                .comment("Output quantity per time")
                .defineInRange("outputCount", 16, 1, Integer.MAX_VALUE);
        GENERATION_TICKS[2] = BUILDER
                .comment("生成间隔（tick）")
                .comment("Spawn interval (ticks)")
                .defineInRange("generationTicks", 20, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("金圆石生成器").push("Gold Cobblegen");
        OUTPUT_COUNTS[3] = BUILDER
                .comment("每次输出数量")
                .comment("Output quantity per time")
                .defineInRange("outputCount", 32, 1, Integer.MAX_VALUE);
        GENERATION_TICKS[3] = BUILDER
                .comment("生成间隔（tick）")
                .comment("Spawn interval (ticks)")
                .defineInRange("generationTicks", 15, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("钻石圆石生成器").push("Diamond Cobblegen");
        OUTPUT_COUNTS[4] = BUILDER
                .comment("每次输出数量")
                .comment("Output quantity per time")
                .defineInRange("outputCount", 64, 1, Integer.MAX_VALUE);
        GENERATION_TICKS[4] = BUILDER
                .comment("生成间隔（tick）")
                .comment("Spawn interval (ticks)")
                .defineInRange("generationTicks", 10, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("绿宝石圆石生成器").push("Emerald Cobblegen");
        OUTPUT_COUNTS[5] = BUILDER
                .comment("每次输出数量")
                .comment("Output quantity per time")
                .defineInRange("outputCount", 128, 1, Integer.MAX_VALUE);
        GENERATION_TICKS[5] = BUILDER
                .comment("生成间隔（tick）")
                .comment("Spawn interval (ticks)")
                .defineInRange("generationTicks", 5, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("下界合金圆石生成器").push("Netherite Cobblegen");
        OUTPUT_COUNTS[6] = BUILDER
                .comment("每次输出数量")
                .comment("Output quantity per time")
                .defineInRange("outputCount", 256, 1, Integer.MAX_VALUE);
        GENERATION_TICKS[6] = BUILDER
                .comment("生成间隔（tick）")
                .comment("Spawn interval (ticks)")
                .defineInRange("generationTicks", 1, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("紫水晶圆石生成器").push("Amethyst Cobblegen");
        OUTPUT_COUNTS[7] = BUILDER
                .comment("每次输出数量")
                .comment("Output quantity per time")
                .defineInRange("outputCount", 256, 1, Integer.MAX_VALUE);
        GENERATION_TICKS[7] = BUILDER
                .comment("生成间隔（tick）")
                .comment("Spawn interval (ticks)")
                .defineInRange("generationTicks", 20, 1, Integer.MAX_VALUE);
        AMETHYST_GROWTH_SPEED_MULTIPLIER = BUILDER
                .comment("紫水晶母岩生长速度倍数")
                .comment("Amethyst growth speed multiplier.")
                .defineInRange("amethystGrowthSpeedMultiplier", 2.0, 1.0, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("红石圆石生成器").push("Redstone Cobblegen");
        OUTPUT_COUNTS[8] = BUILDER
                .comment("每次输出数量")
                .comment("Output quantity per time")
                .defineInRange("outputCount", 64, 1, Integer.MAX_VALUE);
        GENERATION_TICKS[8] = BUILDER
                .comment("生成间隔（tick）")
                .comment("Spawn interval (ticks)")
                .defineInRange("generationTicks", 20, 1, Integer.MAX_VALUE);
        REDSTONE_SIGNAL_MODE = BUILDER
                .comment("红石信号模式 (CONTINUOUS: 持续15级信号, INTERVAL: 按间隔持续15级信号)")
                .comment("Redstone signal mode (CONTINUOUS: continuous level-15 signal, INTERVAL: continuous level-15 signal at intervals)")
                .defineEnum("redstoneSignalMode", RedstoneSignalMode.CONTINUOUS);
        REDSTONE_SIGNAL_INTERVAL = BUILDER
                .comment("红石信号间隔ticks (仅在INTERVAL模式下有效)")
                .comment("Redstone signal interval ticks (only valid in INTERVAL mode)")
                .defineInRange("redstoneSignalInterval", 20, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("荧石圆石生成器").push("Glowstone Cobblegen");
        OUTPUT_COUNTS[9] = BUILDER
                .comment("每次输出数量")
                .comment("Output quantity per time")
                .defineInRange("outputCount", 64, 1, Integer.MAX_VALUE);
        GENERATION_TICKS[9] = BUILDER
                .comment("生成间隔（tick）")
                .comment("Spawn interval (ticks)")
                .defineInRange("generationTicks", 20, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("干草块圆石生成器").push("Hayblock Cobblegen");
        OUTPUT_COUNTS[10] = BUILDER
                .comment("每次输出数量")
                .comment("Output quantity per time")
                .defineInRange("outputCount", 64, 1, Integer.MAX_VALUE);
        GENERATION_TICKS[10] = BUILDER
                .comment("生成间隔（tick）")
                .comment("Spawn interval (ticks)")
                .defineInRange("generationTicks", 20, 1, Integer.MAX_VALUE);
        HAYBLOCK_HEAL_RANGE = BUILDER
                .comment("治疗范围")
                .comment("Hayblock heal range")
                .defineInRange("hayblockHealRange", 1.0, 1.0, Integer.MAX_VALUE);
        HAYBLOCK_REGENERATION_LEVEL = BUILDER
                .comment("生命恢复药水等级")
                .comment("Health regeneration potion level")
                .defineInRange("hayblockRegenerationLevel", 0, 0, 255);
        HAYBLOCK_REGENERATION_DURATION = BUILDER
                .comment("生命恢复药水持续时间（tick）")
                .comment("Health regeneration potion duration (in ticks)")
                .defineInRange("hayblockRegenerationDuration", 60, 20, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("幽匿圆石生成器").push("Sculk Cobblegen");
        OUTPUT_COUNTS[11] = BUILDER
                .comment("每次输出数量")
                .comment("Output quantity per time")
                .defineInRange("outputCount", 128, 1, Integer.MAX_VALUE);
        GENERATION_TICKS[11] = BUILDER
                .comment("生成间隔（tick）")
                .comment("Spawn interval (ticks)")
                .defineInRange("generationTicks", 20, 1, Integer.MAX_VALUE);
        SCULK_TARGET_BLOCKS = BUILDER
                .comment("可转换为目标方块的列表（支持方块ID和tag）")
                .comment("List of blocks that can be converted into the target block (supports block IDs and tags)")
                .defineList("sculkTargetBlocks", List.of(
                        "minecraft:moss_block"
                ), obj -> obj instanceof String);
        SCULK_CONVERSION_RADIUS = BUILDER
                .comment("转换半径范围")
                .comment("Conversion radius range")
                .defineInRange("sculkConversionRadius", 3, 1, Integer.MAX_VALUE);
        SCULK_CONVERSION_CHANCE = BUILDER
                .comment("转换概率（%）")
                .comment("Conversion chance (%)")
                .defineInRange("sculkConversionChance", 10, 1, 100);
        BUILDER.pop();

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static int getOutputCount(CobblestoneGeneratorTier tier) {
        return OUTPUT_COUNTS[tier.ordinal()].get();
    }

    public static int getGenerationTicks(CobblestoneGeneratorTier tier) {
        return GENERATION_TICKS[tier.ordinal()].get();
    }

    public static List<String> getSculkTargetBlocks() {
        return SCULK_TARGET_BLOCKS.get().stream().map(String.class::cast).toList();
    }
}