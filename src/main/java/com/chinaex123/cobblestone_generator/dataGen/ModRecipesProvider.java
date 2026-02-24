package com.chinaex123.cobblestone_generator.dataGen;

import com.chinaex123.cobblestone_generator.CobblestoneGenerator;
import com.chinaex123.cobblestone_generator.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipesProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {

        // 石原石刷石机
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,
                        ModBlocks.STONE_COBBLEGEN.get())
                .pattern("BBB")
                .pattern("CAD")
                .pattern("BBB")
                .define('A', Items.COBBLESTONE)
                .define('B', Items.STONE)
                .define('C', Items.WATER_BUCKET)
                .define('D', Items.LAVA_BUCKET)
                .unlockedBy("has_stone_cobblegen_water", has(Items.WATER_BUCKET))
                .unlockedBy("has_stone_cobblegen_lava", has(Items.LAVA_BUCKET))
                .save(recipeOutput);
        // 铜原石刷石机
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,
                        ModBlocks.COPPER_COBBLEGEN.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', ModBlocks.STONE_COBBLEGEN)
                .define('B', Tags.Items.INGOTS_COPPER)
                .unlockedBy("has_copper_cobblegen", has(ModBlocks.STONE_COBBLEGEN))
                .save(recipeOutput);
        // 铁原石刷石机
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,
                        ModBlocks.IRON_COBBLEGEN.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', ModBlocks.COPPER_COBBLEGEN)
                .define('B', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_iron_cobblegen", has(ModBlocks.IRON_COBBLEGEN))
                .save(recipeOutput);
        // 金原石刷石机
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,
                        ModBlocks.GOLD_COBBLEGEN.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', ModBlocks.IRON_COBBLEGEN)
                .define('B', Tags.Items.INGOTS_GOLD)
                .unlockedBy("has_gold_cobblegen", has(ModBlocks.GOLD_COBBLEGEN))
                .save(recipeOutput);
        // 钻石原石刷石机
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,
                        ModBlocks.DIAMOND_COBBLEGEN.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', ModBlocks.GOLD_COBBLEGEN)
                .define('B', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_diamond_cobblegen", has(ModBlocks.DIAMOND_COBBLEGEN))
                .save(recipeOutput);
        // 绿宝石原石刷石机
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,
                        ModBlocks.EMERALD_COBBLEGEN.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', ModBlocks.DIAMOND_COBBLEGEN)
                .define('B', Tags.Items.GEMS_EMERALD)
                .unlockedBy("has_emerald_cobblegen", has(ModBlocks.EMERALD_COBBLEGEN))
                .save(recipeOutput);
        // 下界合金原石刷石机
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.of(ModBlocks.EMERALD_COBBLEGEN.get()),
                        Ingredient.of(Items.NETHERITE_INGOT),
                        RecipeCategory.MISC, ModBlocks.NETHERITE_COBBLEGEN.get().asItem()
                )
                .unlocks("has_netherite_cobblegen", has(ModBlocks.NETHERITE_COBBLEGEN.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(CobblestoneGenerator.MOD_ID, "netherite_cobblegen"));

        // 紫水晶原石刷石机
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,
                        ModBlocks.AMETHYST_COBBLEGEN.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', ModBlocks.EMERALD_COBBLEGEN)
                .define('B', Tags.Items.GEMS_AMETHYST)
                .unlockedBy("has_amethyst_cobblegen", has(ModBlocks.AMETHYST_COBBLEGEN))
                .save(recipeOutput);
        // 红石原石刷石机
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,
                        ModBlocks.REDSTONE_COBBLEGEN.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', ModBlocks.GOLD_COBBLEGEN)
                .define('B', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_redstong_cobblegen", has(ModBlocks.REDSTONE_COBBLEGEN))
                .save(recipeOutput);
    }
}
