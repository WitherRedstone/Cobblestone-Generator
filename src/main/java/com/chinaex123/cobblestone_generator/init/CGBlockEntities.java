package com.chinaex123.cobblestone_generator.init;

import com.chinaex123.cobblestone_generator.CobblestoneGenerator;
import com.chinaex123.cobblestone_generator.block.entity.NormalGeneratorBlockEntity;
import com.chinaex123.cobblestone_generator.block.entity.SpecialGeneratorBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CGBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CobblestoneGenerator.MOD_ID);

    // 普通生成器方块实体
    public static final Supplier<BlockEntityType<NormalGeneratorBlockEntity>> COBBLE_GENERATOR =
            BLOCK_ENTITIES.register("cobble_generator",
                    () -> BlockEntityType.Builder.of(NormalGeneratorBlockEntity::new,
                            CGBlocks.STONE_COBBLEGEN.get(),
                            CGBlocks.COPPER_COBBLEGEN.get(),
                            CGBlocks.IRON_COBBLEGEN.get(),
                            CGBlocks.GOLD_COBBLEGEN.get(),
                            CGBlocks.DIAMOND_COBBLEGEN.get(),
                            CGBlocks.EMERALD_COBBLEGEN.get(),
                            CGBlocks.NETHERITE_COBBLEGEN.get()
                    ).build(null));

    // 特殊生成器方块实体
    public static final Supplier<BlockEntityType<SpecialGeneratorBlockEntity>> SPECIAL_GENERATOR =
            BLOCK_ENTITIES.register("special_generator",
                    () -> BlockEntityType.Builder.of(SpecialGeneratorBlockEntity::new,
                            CGBlocks.REDSTONE_COBBLEGEN.get(),
                            CGBlocks.AMETHYST_COBBLEGEN.get(),
                            CGBlocks.GLOWSTONE_COBBLEGEN.get(),
                            CGBlocks.HAYBLOCK_COBBLEGEN.get(),
                            CGBlocks.SCULK_COBBLEGEN.get()
                    ).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}