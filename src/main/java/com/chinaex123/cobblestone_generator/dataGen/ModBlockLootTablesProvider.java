package com.chinaex123.cobblestone_generator.dataGen;

import com.chinaex123.cobblestone_generator.init.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootTablesProvider extends BlockLootSubProvider {
    public ModBlockLootTablesProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.STONE_COBBLEGEN.get());
        dropSelf(ModBlocks.COPPER_COBBLEGEN.get());
        dropSelf(ModBlocks.IRON_COBBLEGEN.get());
        dropSelf(ModBlocks.GOLD_COBBLEGEN.get());
        dropSelf(ModBlocks.DIAMOND_COBBLEGEN.get());
        dropSelf(ModBlocks.EMERALD_COBBLEGEN.get());
        dropSelf(ModBlocks.NETHERITE_COBBLEGEN.get());
        dropSelf(ModBlocks.AMETHYST_COBBLEGEN.get());
        dropSelf(ModBlocks.REDSTONE_COBBLEGEN.get());
        dropSelf(ModBlocks.GLOWSTONE_COBBLEGEN.get());
        dropSelf(ModBlocks.HAYBLOCK_COBBLEGEN.get());
        dropSelf(ModBlocks.SCULK_COBBLEGEN.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCK_REGISTER.getEntries()
                .stream()
                .map(Holder::value)::iterator;
    }
}
