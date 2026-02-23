package com.chinaex123.cobblestone_generator.config;

import net.minecraft.core.Direction;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;

public class CobblestoneGeneratorConfig {
    private static final Builder BUILDER = new Builder();

    // 输出方向配置
    private static final ModConfigSpec.EnumValue<Direction> OUTPUT_DIRECTION;

    public static final ModConfigSpec SPEC;

    static {
        BUILDER.push("Cobblestone Generator Settings");

        OUTPUT_DIRECTION = BUILDER
                .comment("设置圆石生成机的输出方向 (默认: UP)")
                .defineEnum("outputDirection", Direction.UP);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    // 配置值缓存
    private static Direction cachedOutputDirection = Direction.UP;

    /**
     * 获取输出方向
     */
    public static Direction getOutputDirection() {
        return cachedOutputDirection;
    }

    /**
     * 设置输出方向
     */
    public static void setOutputDirection(Direction direction) {
        if (OUTPUT_DIRECTION != null) {
            OUTPUT_DIRECTION.set(direction);
        }
        cachedOutputDirection = direction;
    }

    /**
     * 在主类中调用此方法来注册配置
     */
    public static void registerConfig(ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, SPEC);
    }

    /**
     * 配置重载时调用
     */
    public static void onConfigReload() {
        if (OUTPUT_DIRECTION != null) {
            cachedOutputDirection = OUTPUT_DIRECTION.get();
        }
    }
}
