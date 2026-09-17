package com.chinaex123.cobblestone_generator.client;

import com.chinaex123.cobblestone_generator.CobblestoneGenerator;
import com.chinaex123.cobblestone_generator.init.CGBlocks;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.List;

/**
 * 方块颜色处理器。
 * 为所有圆石生成器方块的水纹理部分注册基于生物群系的水颜色。
 */
@EventBusSubscriber(modid = CobblestoneGenerator.MOD_ID, value = Dist.CLIENT)
public class ModBlockColors {

    /**
     * 注册方块颜色处理器，为圆石生成器方块提供基于生物群系的水颜色渲染。
     * <p>
     * 该方法在颜色处理器注册事件中被调用，为所有生成器方块设置水纹理的颜色处理逻辑。
     *
     * @param event 方块颜色处理器注册事件对象
     */
    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(List.of(new BlockTintSource() {
                    @Override
                    public int color(BlockState state) {
                        return 0xFF3F76E4;
                    }
                }),
                CGBlocks.STONE_COBBLEGEN.get(),
                CGBlocks.COPPER_COBBLEGEN.get(),
                CGBlocks.IRON_COBBLEGEN.get(),
                CGBlocks.GOLD_COBBLEGEN.get(),
                CGBlocks.EMERALD_COBBLEGEN.get(),
                CGBlocks.DIAMOND_COBBLEGEN.get(),
                CGBlocks.NETHERITE_COBBLEGEN.get(),
                CGBlocks.AMETHYST_COBBLEGEN.get(),
                CGBlocks.REDSTONE_COBBLEGEN.get(),
                CGBlocks.GLOWSTONE_COBBLEGEN.get(),
                CGBlocks.HAYBLOCK_COBBLEGEN.get(),
                CGBlocks.SCULK_COBBLEGEN.get()
        );
    }
}
