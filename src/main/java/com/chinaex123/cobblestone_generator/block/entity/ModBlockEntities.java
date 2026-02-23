package com.chinaex123.cobblestone_generator.block.entity;

import com.chinaex123.cobblestone_generator.CobblestoneGenerator;
import com.chinaex123.cobblestone_generator.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, CobblestoneGenerator.MOD_ID);

    public static final Supplier<BlockEntityType<CobblestoneGeneratorBlockEntity>> COBBLE_GENERATOR =
            BLOCK_ENTITIES.register("cobble_generator",
                    () -> BlockEntityType.Builder.of(CobblestoneGeneratorBlockEntity::new,
                            ModBlocks.STONE_GEN.get(),
                            ModBlocks.COPPER_GEN.get(),
                            ModBlocks.IRON_GEN.get(),
                            ModBlocks.GOLD_GEN.get(),
                            ModBlocks.DIAMOND_GEN.get(),
                            ModBlocks.EMERALD_GEN.get(),
                            ModBlocks.NETHERITE_GEN.get()
                    ).build(null));

    public static void register(IEventBus bus){
        BLOCK_ENTITIES.register(bus);
    }
}