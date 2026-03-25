package com.chinaex123.cobblestone_generator.client;

import com.chinaex123.cobblestone_generator.CobblestoneGenerator;
import com.chinaex123.cobblestone_generator.block.ModBlocks;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.List;

@EventBusSubscriber(modid = CobblestoneGenerator.MOD_ID, value = Dist.CLIENT)
public class ModBlockColors {

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(List.of(new BlockTintSource() {
                    @Override
                    public int color(BlockState state) {
                        return 0xFF3F76E4;
                    }
                }),
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
}
