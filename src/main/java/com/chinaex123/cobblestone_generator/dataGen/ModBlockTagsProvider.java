//package com.chinaex123.cobblestone_generator.dataGen;
//
//import com.chinaex123.cobblestone_generator.CobblestoneGenerator;
//import com.chinaex123.cobblestone_generator.block.ModBlocks;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.data.PackOutput;
//import net.minecraft.tags.BlockTags;
//import net.neoforged.neoforge.common.data.BlockTagsProvider;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.concurrent.CompletableFuture;
//
//public class ModBlockTagsProvider extends BlockTagsProvider {
//    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
//        super(output, lookupProvider, CobblestoneGenerator.MOD_ID);
//    }
//
//    @Override
//    protected void addTags(HolderLookup.@NotNull Provider provider) {
//        tag(BlockTags.MINEABLE_WITH_PICKAXE)
//                .add(ModBlocks.STONE_COBBLEGEN.get())
//                .add(ModBlocks.COPPER_COBBLEGEN.get())
//                .add(ModBlocks.IRON_COBBLEGEN.get())
//                .add(ModBlocks.GOLD_COBBLEGEN.get())
//                .add(ModBlocks.DIAMOND_COBBLEGEN.get())
//                .add(ModBlocks.EMERALD_COBBLEGEN.get())
//                .add(ModBlocks.NETHERITE_COBBLEGEN.get())
//                .add(ModBlocks.AMETHYST_COBBLEGEN.get())
//                .add(ModBlocks.REDSTONE_COBBLEGEN.get())
//                .add(ModBlocks.GLOWSTONE_COBBLEGEN.get())
//                .add(ModBlocks.HAYBLOCK_COBBLEGEN.get())
//                .add(ModBlocks.SCULK_COBBLEGEN.get());
//
//        tag(BlockTags.NEEDS_STONE_TOOL)
//                .add(ModBlocks.COPPER_COBBLEGEN.get());
//
//        tag(BlockTags.NEEDS_IRON_TOOL)
//                .add(ModBlocks.IRON_COBBLEGEN.get())
//                .add(ModBlocks.GOLD_COBBLEGEN.get())
//                .add(ModBlocks.AMETHYST_COBBLEGEN.get())
//                .add(ModBlocks.REDSTONE_COBBLEGEN.get())
//                .add(ModBlocks.GLOWSTONE_COBBLEGEN.get())
//                .add(ModBlocks.HAYBLOCK_COBBLEGEN.get())
//                .add(ModBlocks.SCULK_COBBLEGEN.get());
//
//        tag(BlockTags.NEEDS_DIAMOND_TOOL)
//                .add(ModBlocks.DIAMOND_COBBLEGEN.get())
//                .add(ModBlocks.EMERALD_COBBLEGEN.get())
//                .add(ModBlocks.NETHERITE_COBBLEGEN.get());
//    }
//}
