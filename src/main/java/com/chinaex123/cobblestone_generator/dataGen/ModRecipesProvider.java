package com.chinaex123.cobblestone_generator.dataGen;

import com.chinaex123.cobblestone_generator.CobblestoneGenerator;
import com.chinaex123.cobblestone_generator.init.CGBlocks;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipesProvider extends RecipeProvider {
    private final HolderLookup.Provider provider;
    private final RecipeOutput output;

    public ModRecipesProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
        this.provider = provider;
        this.output = output;
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(packOutput, lookupProvider);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
            return new ModRecipesProvider(provider, output);
        }

        @Override
        public @NotNull String getName() {
            return CobblestoneGenerator.MOD_ID;
        }
    }

    @Override
    protected void buildRecipes() {
        HolderGetter<Item> itemRegistryLookup = this.registries.lookupOrThrow(Registries.ITEM);

        // 石原石刷石机
        ShapedRecipeBuilder.shaped(itemRegistryLookup, RecipeCategory.MISC, CGBlocks.STONE_COBBLEGEN.get())
                .pattern("BBB")
                .pattern("CAD")
                .pattern("BBB")
                .define('A', Items.COBBLESTONE)
                .define('B', Items.STONE)
                .define('C', Tags.Items.BUCKETS_WATER)
                .define('D', Tags.Items.BUCKETS_LAVA)
                .unlockedBy("has_stone_cobblegen_water", has(Tags.Items.BUCKETS_WATER))
                .unlockedBy("has_stone_cobblegen_lava", has(Tags.Items.BUCKETS_LAVA))
                .save(output);
        // 铜原石刷石机
        ShapedRecipeBuilder.shaped(itemRegistryLookup, RecipeCategory.MISC, CGBlocks.COPPER_COBBLEGEN.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', CGBlocks.STONE_COBBLEGEN)
                .define('B', Tags.Items.INGOTS_COPPER)
                .unlockedBy("has_copper_cobblegen", has(CGBlocks.STONE_COBBLEGEN))
                .save(output);
        // 铁原石刷石机
        ShapedRecipeBuilder.shaped(itemRegistryLookup, RecipeCategory.MISC, CGBlocks.IRON_COBBLEGEN.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', CGBlocks.COPPER_COBBLEGEN)
                .define('B', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_iron_cobblegen", has(CGBlocks.IRON_COBBLEGEN))
                .save(output);
        // 金原石刷石机
        ShapedRecipeBuilder.shaped(itemRegistryLookup, RecipeCategory.MISC, CGBlocks.GOLD_COBBLEGEN.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', CGBlocks.IRON_COBBLEGEN)
                .define('B', Tags.Items.INGOTS_GOLD)
                .unlockedBy("has_gold_cobblegen", has(CGBlocks.GOLD_COBBLEGEN))
                .save(output);
        // 钻石原石刷石机
        ShapedRecipeBuilder.shaped(itemRegistryLookup, RecipeCategory.MISC, CGBlocks.DIAMOND_COBBLEGEN.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', CGBlocks.GOLD_COBBLEGEN)
                .define('B', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_diamond_cobblegen", has(CGBlocks.DIAMOND_COBBLEGEN))
                .save(output);
        // 绿宝石原石刷石机
        ShapedRecipeBuilder.shaped(itemRegistryLookup, RecipeCategory.MISC, CGBlocks.EMERALD_COBBLEGEN.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', CGBlocks.DIAMOND_COBBLEGEN)
                .define('B', Tags.Items.GEMS_EMERALD)
                .unlockedBy("has_emerald_cobblegen", has(CGBlocks.EMERALD_COBBLEGEN))
                .save(output);
        // 下界合金原石刷石机
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.of(CGBlocks.EMERALD_COBBLEGEN.get()),
                        Ingredient.of(Items.NETHERITE_INGOT),
                        RecipeCategory.MISC, CGBlocks.NETHERITE_COBBLEGEN.get().asItem()
                )
                .unlocks("has_netherite_cobblegen", has(CGBlocks.NETHERITE_COBBLEGEN.get()))
                .save(output, String.valueOf(CGBlocks.NETHERITE_COBBLEGEN.getId()));

        // 紫水晶原石刷石机
        ShapedRecipeBuilder.shaped(itemRegistryLookup, RecipeCategory.MISC, CGBlocks.AMETHYST_COBBLEGEN.get())
                .pattern("CBC")
                .pattern("BAB")
                .pattern("CBC")
                .define('A', CGBlocks.EMERALD_COBBLEGEN)
                .define('B', Tags.Items.GEMS_AMETHYST)
                .define('C', Items.AMETHYST_BLOCK)
                .unlockedBy("has_amethyst_cobblegen", has(CGBlocks.AMETHYST_COBBLEGEN))
                .save(output);
        // 红石原石刷石机
        ShapedRecipeBuilder.shaped(itemRegistryLookup, RecipeCategory.MISC, CGBlocks.REDSTONE_COBBLEGEN.get())
                .pattern("CBC")
                .pattern("BAB")
                .pattern("CBC")
                .define('A', CGBlocks.GOLD_COBBLEGEN)
                .define('B', Tags.Items.DUSTS_REDSTONE)
                .define('C', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .unlockedBy("has_redstone_cobblegen", has(CGBlocks.REDSTONE_COBBLEGEN))
                .save(output);
        // 荧石原石刷石机
        ShapedRecipeBuilder.shaped(itemRegistryLookup, RecipeCategory.MISC, CGBlocks.GLOWSTONE_COBBLEGEN.get())
                .pattern("CBC")
                .pattern("BAB")
                .pattern("CBC")
                .define('A', CGBlocks.GOLD_COBBLEGEN)
                .define('B', Tags.Items.DUSTS_GLOWSTONE)
                .define('C', Items.GLOWSTONE)
                .unlockedBy("has_glowstone_cobblegen", has(CGBlocks.GLOWSTONE_COBBLEGEN))
                .save(output);
        // 干草块原石刷石机
        ShapedRecipeBuilder.shaped(itemRegistryLookup, RecipeCategory.MISC, CGBlocks.HAYBLOCK_COBBLEGEN.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', CGBlocks.GOLD_COBBLEGEN)
                .define('B', Items.HAY_BLOCK)
                .unlockedBy("has_hayblock_cobblegen", has(CGBlocks.HAYBLOCK_COBBLEGEN))
                .save(output);
        // 幽匿原石刷石机
        ShapedRecipeBuilder.shaped(itemRegistryLookup, RecipeCategory.MISC, CGBlocks.SCULK_COBBLEGEN.get())
                .pattern("CBC")
                .pattern("BAB")
                .pattern("CBC")
                .define('A', CGBlocks.DIAMOND_COBBLEGEN)
                .define('B', Items.ECHO_SHARD)
                .define('C', Items.SCULK)
                .unlockedBy("has_sculk_cobblegen", has(CGBlocks.SCULK_COBBLEGEN))
                .save(output);
    }
}
