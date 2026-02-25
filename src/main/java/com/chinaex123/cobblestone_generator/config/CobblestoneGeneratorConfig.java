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
    public static final ModConfigSpec.ConfigValue<String> SCULK_TARGET_BLOCKS_STRING;
    public static final ModConfigSpec.IntValue SCULK_CONVERSION_RADIUS;
    public static final ModConfigSpec.IntValue SCULK_CONVERSION_CHANCE;


    // 每个等级的配置数组
    public static final ModConfigSpec.IntValue[] OUTPUT_COUNTS = new ModConfigSpec.IntValue[CobblestoneGeneratorTier.values().length];
    public static final ModConfigSpec.IntValue[] GENERATION_TICKS = new ModConfigSpec.IntValue[CobblestoneGeneratorTier.values().length];

    public static final ModConfigSpec SPEC;

    // 红石信号模式枚举
    public enum RedstoneSignalMode {
        CONTINUOUS,  // 持续模式：有物品时持续保持15级信号
        INTERVAL     // 间隔模式：按设定间隔检查并发出信号
    }

    static {
        // 全局配置
        BUILDER.push("全局配置");
        OUTPUT_DIRECTION = BUILDER
                .comment("输出方向 (默认: UP)")
                .defineEnum("outputDirection", Direction.UP);
        AUTO_OUTPUT_ENABLED = BUILDER
                .comment("是否启用自动输出功能 (默认: true)")
                .define("autoOutputEnabled", true);
        SPEED_MULTIPLIER = BUILDER
                .comment("全局速度倍数 (0.1-10.0, 默认: 1.0)")
                .defineInRange("speedMultiplier", 1.0, 0.1, 10.0);
        BUILDER.pop();

        // 每个的圆石生成器配置
        BUILDER.push("圆石生成器配置");

        BUILDER.push("紫水晶圆石生成器功能配置");
        AMETHYST_GROWTH_SPEED_MULTIPLIER = BUILDER
                .comment("紫水晶母岩生长速度倍数 (默认: 2.0)")
                .defineInRange("amethystGrowthSpeedMultiplier", 2.0, 1.0, 10.0);
        BUILDER.pop();

        BUILDER.push("红石圆石生成器功能配置");
        REDSTONE_SIGNAL_MODE = BUILDER
                .comment("红石信号模式 (CONTINUOUS: 持续15级信号, INTERVAL: 按间隔持续15级信号)")
                .defineEnum("redstoneSignalMode", RedstoneSignalMode.CONTINUOUS);
        REDSTONE_SIGNAL_INTERVAL = BUILDER
                .comment("红石信号间隔ticks (仅在INTERVAL模式下有效, 默认: 20)")
                .defineInRange("redstoneSignalInterval", 20, 1, 1200);
        BUILDER.pop();

        BUILDER.push("干草块圆石生成器功能配置");
        HAYBLOCK_HEAL_RANGE = BUILDER
                .comment("治疗范围(距方块几格) (默认: 1.0)")
                .defineInRange("hayblockHealRange", 1.0, 1.0, 32.0);
        HAYBLOCK_REGENERATION_LEVEL = BUILDER  // 新增配置
                .comment("生命恢复药水等级 (默认: 0 表示I级)")
                .defineInRange("hayblockRegenerationLevel", 0, 0, 255);
        HAYBLOCK_REGENERATION_DURATION = BUILDER  // 新增配置
                .comment("生命恢复药水持续时间(ticks) (默认: 60 = 3秒)")
                .defineInRange("hayblockRegenerationDuration", 60, 20, 1200);
        BUILDER.pop();

        BUILDER.push("幽匿圆石生成器功能配置");
        SCULK_TARGET_BLOCKS_STRING = BUILDER
                .comment("可转换为目标方块的列表，用逗号分隔 (支持方块ID和tag)")
                .define("sculkTargetBlocks", "minecraft:moss_block");
        SCULK_CONVERSION_RADIUS = BUILDER
                .comment("转换半径 (默认: 3)")
                .defineInRange("sculkConversionRadius", 3, 1, 8);
        SCULK_CONVERSION_CHANCE = BUILDER
                .comment("转换概率 (1-100, 默认: 10)")
                .defineInRange("sculkConversionChance", 10, 1, 100);
        BUILDER.pop();

        CobblestoneGeneratorTier[] tiers = CobblestoneGeneratorTier.values();
        for (int i = 0; i < tiers.length; i++) {
            CobblestoneGeneratorTier tier = tiers[i];
            String tierName = tier.name().toLowerCase();

            BUILDER.push(tierName);
            OUTPUT_COUNTS[i] = BUILDER
                    .comment("每次输出数量 (默认: " + tier.getDefaultOutputCount() + ")")
                    .defineInRange("outputCount", tier.getDefaultOutputCount(), 1, 10240);
            GENERATION_TICKS[i] = BUILDER
                    .comment("生成间隔ticks (默认: " + tier.getDefaultOutputTicks() + ")")
                    .defineInRange("generationTicks", tier.getDefaultOutputTicks(), 1, 1200);
            BUILDER.pop();
        }

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    // 全局配置获取方法
    public static Direction getOutputDirection() {
        return OUTPUT_DIRECTION != null ? OUTPUT_DIRECTION.get() : Direction.UP;
    }

    public static double getSpeedMultiplier() {
        return SPEED_MULTIPLIER != null ? SPEED_MULTIPLIER.get() : 1.0;
    }

    // 红石信号配置获取方法
    public static RedstoneSignalMode getRedstoneSignalMode() {
        return REDSTONE_SIGNAL_MODE != null ? REDSTONE_SIGNAL_MODE.get() : RedstoneSignalMode.CONTINUOUS;
    }

    /**
     * 获取红石信号间隔配置值。
     * 如果配置项未初始化，则返回默认值20。
     *
     * @return 红石信号间隔的tick数，用于INTERVAL模式下的信号发送间隔
     */
    public static int getRedstoneSignalInterval() {
        return REDSTONE_SIGNAL_INTERVAL != null ? REDSTONE_SIGNAL_INTERVAL.get() : 20;
    }

    /**
     * 获取紫水晶生长速度倍数配置值。
     * 如果配置项未初始化，则返回默认值2.0。
     *
     * @return 紫水晶母岩生长速度倍数，用于控制紫水晶芽的生成和进化速度
     */
    public static double getAmethystGrowthSpeedMultiplier() {
        return AMETHYST_GROWTH_SPEED_MULTIPLIER != null ? AMETHYST_GROWTH_SPEED_MULTIPLIER.get() : 2.0;
    }

    /**
     * 根据生成器等级获取对应的输出数量配置值。
     * 若配置项未初始化或超出范围，则返回该等级的默认输出数量。
     *
     * @param tier 生成器等级枚举
     * @return 对应的输出数量配置值或默认值
     */
    public static int getOutputCount(CobblestoneGeneratorTier tier) {
        // 获取枚举的序号作为数组索引
        int index = tier.ordinal();

        // 边界检查：确保索引在数组范围内
        if (index < OUTPUT_COUNTS.length) {
            // 检查该索引位置是否已初始化
            if (OUTPUT_COUNTS[index] != null) {
                return OUTPUT_COUNTS[index].get();
            }
        }

        // 若配置项未初始化或超出范围，返回默认值
        return tier.getDefaultOutputCount();
    }

    /**
     * 根据生成器等级获取对应的生成间隔配置值。
     * 若配置项未初始化或超出范围，则返回该等级的默认生成间隔。
     *
     * @param tier 生成器等级枚举
     * @return 对应的生成间隔配置值（tick数）或默认值
     */
    public static int getGenerationTicks(CobblestoneGeneratorTier tier) {
        int index = tier.ordinal();
        if (index < GENERATION_TICKS.length && GENERATION_TICKS[index] != null) {
            return GENERATION_TICKS[index].get();
        }
        return tier.getDefaultOutputTicks();
    }

    /**
     * 获取干草块治疗范围的配置值。
     * 若配置项未初始化，则返回默认值1.0。
     *
     * @return 干草块治疗范围（单位：方块距离）
     */
    public static double getHayblockHealRange() {
        return HAYBLOCK_HEAL_RANGE != null ? HAYBLOCK_HEAL_RANGE.get() : 1.0;
    }

    /**
     * 获取幽匿方块转换的目标方块列表配置值。
     * 若配置项未初始化，则返回默认的目标方块列表。
     *
     * @return 目标方块列表，支持方块ID和标签格式（如"minecraft:stone,#minecraft:base_stone_overworld"）
     */
    public static List<String> getSculkTargetBlocks() {
        if (SCULK_TARGET_BLOCKS_STRING != null) {
            String raw = SCULK_TARGET_BLOCKS_STRING.get();
            return Arrays.asList(raw.split(","));
        }
        return List.of("minecraft:minecraft:moss_block");
    }

    /**
     * 获取幽匿方块转换的范围配置值。
     * 若配置项未初始化，则返回默认值3。
     *
     * @return 转换范围（单位：方块距离）
     */
    public static int getSculkConversionRadius() {
        return SCULK_CONVERSION_RADIUS != null ? SCULK_CONVERSION_RADIUS.get() : 3;
    }

    /**
     * 获取幽匿方块转换的概率配置值。
     * 若配置项未初始化，则返回默认值10。
     *
     * @return 转换概率（百分比）
     */
    public static int getSculkConversionChance() {
        return SCULK_CONVERSION_CHANCE != null ? SCULK_CONVERSION_CHANCE.get() : 10;
    }

    public static void onConfigReload() {
        // 添加配置重载时需要执行的逻辑
        System.out.println("Cobblestone Generator config reloaded");
    }
}
