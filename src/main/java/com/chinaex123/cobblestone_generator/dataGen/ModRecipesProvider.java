package com.chinaex123.cobblestone_generator.dataGen;

import com.chinaex123.cobblestone_generator.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipesProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,
                        ModBlocks.STONE_COBBLEGEN.get())
                .pattern("BBB")
                .pattern("CAD")
                .pattern("BBB")
                .define('A', Items.CRAFTING_TABLE)
                .define('B', Items.COBBLESTONE)
                .define('C', Items.WATER_BUCKET)
                .define('D', Items.LAVA_BUCKET)
                .unlockedBy("has_stone_gen", has(Tags.Items.INGOTS_IRON))
                .save(recipeOutput);
    }
}
