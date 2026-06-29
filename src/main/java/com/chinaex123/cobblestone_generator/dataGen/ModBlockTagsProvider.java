package com.chinaex123.cobblestone_generator.dataGen;

import com.chinaex123.cobblestone_generator.CobblestoneGenerator;
import com.chinaex123.cobblestone_generator.init.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, CobblestoneGenerator.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.STONE_COBBLEGEN.getKey())
                .add(ModBlocks.COPPER_COBBLEGEN.getKey())
                .add(ModBlocks.IRON_COBBLEGEN.getKey())
                .add(ModBlocks.GOLD_COBBLEGEN.getKey())
                .add(ModBlocks.DIAMOND_COBBLEGEN.getKey())
                .add(ModBlocks.EMERALD_COBBLEGEN.getKey())
                .add(ModBlocks.NETHERITE_COBBLEGEN.getKey())
                .add(ModBlocks.AMETHYST_COBBLEGEN.getKey())
                .add(ModBlocks.REDSTONE_COBBLEGEN.getKey())
                .add(ModBlocks.GLOWSTONE_COBBLEGEN.getKey())
                .add(ModBlocks.HAYBLOCK_COBBLEGEN.getKey())
                .add(ModBlocks.SCULK_COBBLEGEN.getKey());

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.COPPER_COBBLEGEN.getKey());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.IRON_COBBLEGEN.getKey())
                .add(ModBlocks.GOLD_COBBLEGEN.getKey())
                .add(ModBlocks.AMETHYST_COBBLEGEN.getKey())
                .add(ModBlocks.REDSTONE_COBBLEGEN.getKey())
                .add(ModBlocks.GLOWSTONE_COBBLEGEN.getKey())
                .add(ModBlocks.HAYBLOCK_COBBLEGEN.getKey())
                .add(ModBlocks.SCULK_COBBLEGEN.getKey());

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.DIAMOND_COBBLEGEN.getKey())
                .add(ModBlocks.EMERALD_COBBLEGEN.getKey())
                .add(ModBlocks.NETHERITE_COBBLEGEN.getKey());
    }
}
