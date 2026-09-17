package com.chinaex123.cobblestone_generator.block;

import com.chinaex123.cobblestone_generator.config.CobblestoneGeneratorConfig;

/**
 * 圆石生成器等级枚举。
 * <p>
 * 定义各等级（普通与特殊）的默认产出数量与生成间隔，
 * 并提供从配置读取实际值的访问方法。
 */
public enum CobblestoneGeneratorTier {
    /** 石等级 */
    STONE(4, 60),
    /** 铜等级 */
    COPPER(8, 40),
    /** 铁等级 */
    IRON(16, 20),
    /** 金等级 */
    GOLD(32, 15),
    /** 钻石等级 */
    DIAMOND(64, 10),
    /** 绿宝石等级 */
    EMERALD(128, 5),
    /** 下界合金等级 */
    NETHERITE(256, 1),

    /** 紫水晶等级 */
    AMETHYST(256, 20),
    /** 红石等级 */
    REDSTONE(64, 20),
    /** 荧石等级 */
    GLOWSTONE(64, 20),
    /** 干草块等级 */
    HAYBLOCK(64, 20),
    /** 幽匿等级 */
    SCULK(128, 20);

    private final int defaultOutputCount;
    private final int defaultOutputTicks;

    CobblestoneGeneratorTier(int OutputCount, int OutputTicks) {
        this.defaultOutputCount = OutputCount;
        this.defaultOutputTicks = OutputTicks;
    }

    /** 获取默认产出数量 */
    public int getDefaultOutputCount() {
        return defaultOutputCount;
    }

    /** 获取默认产出间隔 */
    public int getDefaultOutputTicks() {
        return defaultOutputTicks;
    }

    /** 获取实际产出数量 */
    public int getOutputCount() {
        return CobblestoneGeneratorConfig.getOutputCount(this);
    }

    /** 获取实际产出间隔 */
    public int getGenerationTicks() {
        return CobblestoneGeneratorConfig.getGenerationTicks(this);
    }
}
