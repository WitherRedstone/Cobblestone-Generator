package com.chinaex123.cobblestone_generator.block.entity;

import com.chinaex123.cobblestone_generator.CobblestoneGenerator;
import com.chinaex123.cobblestone_generator.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CobblestoneGenerator.MOD_ID);

    // 普通生成器方块实体
    public static final Supplier<BlockEntityType<NormalGeneratorBlockEntity>> COBBLE_GENERATOR =
            BLOCK_ENTITIES.register("cobble_generator",
                    () -> BlockEntityType.Builder.of(NormalGeneratorBlockEntity::new,
                            ModBlocks.STONE_COBBLEGEN.get(),
                            ModBlocks.COPPER_COBBLEGEN.get(),
                            ModBlocks.IRON_COBBLEGEN.get(),
                            ModBlocks.GOLD_COBBLEGEN.get(),
                            ModBlocks.DIAMOND_COBBLEGEN.get(),
                            ModBlocks.EMERALD_COBBLEGEN.get(),
                            ModBlocks.NETHERITE_COBBLEGEN.get()
                    ).build(null));

    // 特殊生成器方块实体
    public static final Supplier<BlockEntityType<SpecialGeneratorBlockEntity>> SPECIAL_GENERATOR =
            BLOCK_ENTITIES.register("special_generator",
                    () -> BlockEntityType.Builder.of(SpecialGeneratorBlockEntity::new,
                            ModBlocks.REDSTONE_COBBLEGEN.get(),
                            ModBlocks.AMETHYST_COBBLEGEN.get(),
                            ModBlocks.GLOWSTONE_COBBLEGEN.get(),
                            ModBlocks.HAYBLOCK_COBBLEGEN.get(),
                            ModBlocks.SCULK_COBBLEGEN.get()
                    ).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}