package com.chinaex123.cobblestone_generator;

import com.chinaex123.cobblestone_generator.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CobblestoneGenerator.MOD_ID);

    public static final Supplier<CreativeModeTab> COBBLESTONE_GENERATOR_TAB =
            CREATIVE_MODE_TAB.register("cobblestone_generator_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.STONE_COBBLEGEN.get()))
                    .title(Component.translatable("itemGroup.cobblestone_generator_tab"))
                    .displayItems((parameters, output) -> {

                        output.accept(ModBlocks.STONE_COBBLEGEN.get());
                        output.accept(ModBlocks.COPPER_COBBLEGEN.get());
                        output.accept(ModBlocks.IRON_COBBLEGEN.get());
                        output.accept(ModBlocks.GOLD_COBBLEGEN.get());
                        output.accept(ModBlocks.DIAMOND_COBBLEGEN.get());
                        output.accept(ModBlocks.EMERALD_COBBLEGEN.get());
                        output.accept(ModBlocks.NETHERITE_COBBLEGEN.get());

                        output.accept(ModBlocks.AMETHYST_COBBLEGEN.get());
                        output.accept(ModBlocks.REDSTONE_COBBLEGEN.get());

                    })
                    .build());

    // 注册到NeoForge事件总线里
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
