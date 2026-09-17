package com.chinaex123.cobblestone_generator.dataGen;

import com.chinaex123.cobblestone_generator.CobblestoneGenerator;
import com.chinaex123.cobblestone_generator.init.CGBlocks;
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
                .add(CGBlocks.STONE_COBBLEGEN.getKey())
                .add(CGBlocks.COPPER_COBBLEGEN.getKey())
                .add(CGBlocks.IRON_COBBLEGEN.getKey())
                .add(CGBlocks.GOLD_COBBLEGEN.getKey())
                .add(CGBlocks.DIAMOND_COBBLEGEN.getKey())
                .add(CGBlocks.EMERALD_COBBLEGEN.getKey())
                .add(CGBlocks.NETHERITE_COBBLEGEN.getKey())
                .add(CGBlocks.AMETHYST_COBBLEGEN.getKey())
                .add(CGBlocks.REDSTONE_COBBLEGEN.getKey())
                .add(CGBlocks.GLOWSTONE_COBBLEGEN.getKey())
                .add(CGBlocks.HAYBLOCK_COBBLEGEN.getKey())
                .add(CGBlocks.SCULK_COBBLEGEN.getKey());

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(CGBlocks.COPPER_COBBLEGEN.getKey());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(CGBlocks.IRON_COBBLEGEN.getKey())
                .add(CGBlocks.GOLD_COBBLEGEN.getKey())
                .add(CGBlocks.AMETHYST_COBBLEGEN.getKey())
                .add(CGBlocks.REDSTONE_COBBLEGEN.getKey())
                .add(CGBlocks.GLOWSTONE_COBBLEGEN.getKey())
                .add(CGBlocks.HAYBLOCK_COBBLEGEN.getKey())
                .add(CGBlocks.SCULK_COBBLEGEN.getKey());

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(CGBlocks.DIAMOND_COBBLEGEN.getKey())
                .add(CGBlocks.EMERALD_COBBLEGEN.getKey())
                .add(CGBlocks.NETHERITE_COBBLEGEN.getKey());
    }
}
