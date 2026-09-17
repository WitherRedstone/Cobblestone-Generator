package com.chinaex123.cobblestone_generator.data;

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
                .add(CGBlocks.STONE_COBBLEGEN.get())
                .add(CGBlocks.COPPER_COBBLEGEN.get())
                .add(CGBlocks.IRON_COBBLEGEN.get())
                .add(CGBlocks.GOLD_COBBLEGEN.get())
                .add(CGBlocks.DIAMOND_COBBLEGEN.get())
                .add(CGBlocks.EMERALD_COBBLEGEN.get())
                .add(CGBlocks.NETHERITE_COBBLEGEN.get())
                .add(CGBlocks.AMETHYST_COBBLEGEN.get())
                .add(CGBlocks.REDSTONE_COBBLEGEN.get())
                .add(CGBlocks.GLOWSTONE_COBBLEGEN.get())
                .add(CGBlocks.HAYBLOCK_COBBLEGEN.get())
                .add(CGBlocks.SCULK_COBBLEGEN.get());

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(CGBlocks.COPPER_COBBLEGEN.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(CGBlocks.IRON_COBBLEGEN.get())
                .add(CGBlocks.GOLD_COBBLEGEN.get())
                .add(CGBlocks.AMETHYST_COBBLEGEN.get())
                .add(CGBlocks.REDSTONE_COBBLEGEN.get())
                .add(CGBlocks.GLOWSTONE_COBBLEGEN.get())
                .add(CGBlocks.HAYBLOCK_COBBLEGEN.get())
                .add(CGBlocks.SCULK_COBBLEGEN.get());

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(CGBlocks.DIAMOND_COBBLEGEN.get())
                .add(CGBlocks.EMERALD_COBBLEGEN.get())
                .add(CGBlocks.NETHERITE_COBBLEGEN.get());
    }
}
