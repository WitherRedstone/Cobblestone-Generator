package com.chinaex123.cobblestone_generator.init;

import com.chinaex123.cobblestone_generator.CobblestoneGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CGCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CobblestoneGenerator.MOD_ID);

    public static final Supplier<CreativeModeTab> COBBLESTONE_GENERATOR_TAB =
            CREATIVE_MODE_TAB.register("cobblestone_generator_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(CGBlocks.STONE_COBBLEGEN.get()))
                    .title(Component.translatable("itemGroup.cobblestone_generator_tab"))
                    .displayItems((parameters, output) -> {

                        output.accept(CGBlocks.STONE_COBBLEGEN.get());
                        output.accept(CGBlocks.COPPER_COBBLEGEN.get());
                        output.accept(CGBlocks.IRON_COBBLEGEN.get());
                        output.accept(CGBlocks.GOLD_COBBLEGEN.get());
                        output.accept(CGBlocks.DIAMOND_COBBLEGEN.get());
                        output.accept(CGBlocks.EMERALD_COBBLEGEN.get());
                        output.accept(CGBlocks.NETHERITE_COBBLEGEN.get());

                        output.accept(CGBlocks.AMETHYST_COBBLEGEN.get());
                        output.accept(CGBlocks.REDSTONE_COBBLEGEN.get());
                        output.accept(CGBlocks.GLOWSTONE_COBBLEGEN.get());
                        output.accept(CGBlocks.HAYBLOCK_COBBLEGEN.get());
                        output.accept(CGBlocks.SCULK_COBBLEGEN.get());

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
