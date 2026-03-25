package com.chinaex123.cobblestone_generator.client;

import com.chinaex123.cobblestone_generator.CobblestoneGenerator;
import com.chinaex123.cobblestone_generator.block.ModBlocks;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.color.block.BlockColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = CobblestoneGenerator.MOD_ID, value = Dist.CLIENT)
public class ModBlockColors {

    /**
     * 注册方块颜色处理器，为圆石生成器方块提供基于生物群系的水颜色渲染。
     * 该方法在颜色处理器注册事件中被调用，为所有生成器方块设置水纹理的颜色处理逻辑。
     *
     * @param event 方块颜色处理器注册事件对象
     */
    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        BlockColor waterColorHandler = (state, getter, pos, tintIndex) -> {
            // tintindex 0 对应模型中的水的面
            if (tintIndex == 0) {
                try {
                    // 尝试获取生物群系水颜色
                    if (getter != null && pos != null) {
                        return BiomeColors.getAverageWaterColor(getter, pos);
                    }
                    // 如果参数为空，使用默认颜色
                    return 0x3F76E4; // Minecraft默认水蓝色
                } catch (Exception e) {
                    // 捕获任何异常，使用默认颜色
                    return 0x3F76E4; // 默认水蓝色作为后备
                }
            }
            return -1; // -1 表示不染色
        };

        // 为所有圆石生成器方块注册颜色处理器
        event.register(waterColorHandler,
                ModBlocks.STONE_COBBLEGEN.get(),
                ModBlocks.COPPER_COBBLEGEN.get(),
                ModBlocks.IRON_COBBLEGEN.get(),
                ModBlocks.GOLD_COBBLEGEN.get(),
                ModBlocks.EMERALD_COBBLEGEN.get(),
                ModBlocks.DIAMOND_COBBLEGEN.get(),
                ModBlocks.NETHERITE_COBBLEGEN.get(),
                ModBlocks.AMETHYST_COBBLEGEN.get(),
                ModBlocks.REDSTONE_COBBLEGEN.get(),
                ModBlocks.GLOWSTONE_COBBLEGEN.get(),
                ModBlocks.HAYBLOCK_COBBLEGEN.get(),
                ModBlocks.SCULK_COBBLEGEN.get()
        );
    }

//    @SubscribeEvent
//    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
//        // 创建物品颜色处理器
//        ItemColor itemColorHandler = (stack, tintIndex) -> {
//            // tintindex 0 对应模型中的水的面
//            return 0x3F76E4;
//        };
//
//        // 为所有圆石生成器物品注册颜色处理器
//        event.register(itemColorHandler,
//                ModBlocks.STONE_COBBLEGEN.get().asItem(),
//                ModBlocks.COPPER_COBBLEGEN.get().asItem(),
//                ModBlocks.IRON_COBBLEGEN.get().asItem(),
//                ModBlocks.GOLD_COBBLEGEN.get().asItem(),
//                ModBlocks.EMERALD_COBBLEGEN.get().asItem(),
//                ModBlocks.DIAMOND_COBBLEGEN.get().asItem(),
//                ModBlocks.NETHERITE_COBBLEGEN.get().asItem()
//        );
//    }
}