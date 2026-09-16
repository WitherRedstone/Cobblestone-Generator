package com.chinaex123.cobblestone_generator.data;

import com.chinaex123.cobblestone_generator.init.CGBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootTablesProvider extends BlockLootSubProvider {
    public ModBlockLootTablesProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    /**
     * 生成方块战利品表
     * <p>
     * 此方法用于定义模组中各个方块被破坏时的掉落物规则。
     * 目前仅配置了运输箱方块的基础掉落规则。
     */
    @Override
    protected void generate() {
        dropSelf(CGBlocks.STONE_COBBLEGEN.get());
        dropSelf(CGBlocks.COPPER_COBBLEGEN.get());
        dropSelf(CGBlocks.IRON_COBBLEGEN.get());
        dropSelf(CGBlocks.GOLD_COBBLEGEN.get());
        dropSelf(CGBlocks.DIAMOND_COBBLEGEN.get());
        dropSelf(CGBlocks.EMERALD_COBBLEGEN.get());
        dropSelf(CGBlocks.NETHERITE_COBBLEGEN.get());
        dropSelf(CGBlocks.AMETHYST_COBBLEGEN.get());
        dropSelf(CGBlocks.REDSTONE_COBBLEGEN.get());
        dropSelf(CGBlocks.GLOWSTONE_COBBLEGEN.get());
        dropSelf(CGBlocks.HAYBLOCK_COBBLEGEN.get());
        dropSelf(CGBlocks.SCULK_COBBLEGEN.get());
    }


    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return CGBlocks.BLOCK_REGISTER.getEntries().stream().map(Holder::value)::iterator;
    }
}
