package com.chinaex123.cobblestone_generator.block;

import com.chinaex123.cobblestone_generator.CobblestoneGenerator;
import com.chinaex123.cobblestone_generator.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    // 创建方块注册器实例
    public static final DeferredRegister.Blocks BLOCK_REGISTER =
            DeferredRegister.createBlocks(CobblestoneGenerator.MOD_ID);


    public static final DeferredBlock<Block> STONE_GEN = registerBlocks("stone_gen",
            () -> new CobblestoneGeneratorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .sound(SoundType.STONE)
                    .strength(3.0f, 6.0f)
                    .noOcclusion()
                    .requiresCorrectToolForDrops(), CobblestoneGeneratorTier.STONE));

    public static final DeferredBlock<Block> COPPER_GEN = registerBlocks("copper_gen",
            () -> new CobblestoneGeneratorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .sound(SoundType.STONE)
                    .strength(3.0f, 6.0f)
                    .noOcclusion()
                    .requiresCorrectToolForDrops(), CobblestoneGeneratorTier.COPPER));

    public static final DeferredBlock<Block> IRON_GEN = registerBlocks("iron_gen",
            () -> new CobblestoneGeneratorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.STONE)
                    .strength(3.0f, 6.0f)
                    .noOcclusion()
                    .requiresCorrectToolForDrops(), CobblestoneGeneratorTier.IRON));

    public static final DeferredBlock<Block> GOLD_GEN = registerBlocks("gold_gen",
            () -> new CobblestoneGeneratorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .sound(SoundType.STONE)
                    .strength(3.0f, 6.0f)
                    .noOcclusion()
                    .requiresCorrectToolForDrops(), CobblestoneGeneratorTier.GOLD));

    public static final DeferredBlock<Block> DIAMOND_GEN = registerBlocks("diamond_gen",
            () -> new CobblestoneGeneratorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIAMOND)
                    .sound(SoundType.STONE)
                    .strength(3.0f, 6.0f)
                    .noOcclusion()
                    .requiresCorrectToolForDrops(), CobblestoneGeneratorTier.DIAMOND));

    public static final DeferredBlock<Block> EMERALD_GEN = registerBlocks("emerald_gen",
            () -> new CobblestoneGeneratorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.EMERALD)
                    .sound(SoundType.STONE)
                    .strength(3.0f, 6.0f)
                    .noOcclusion()
                    .requiresCorrectToolForDrops(), CobblestoneGeneratorTier.EMERALD));

    public static final DeferredBlock<Block> NETHERITE_GEN = registerBlocks("netherite_gen",
            () -> new CobblestoneGeneratorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .sound(SoundType.STONE)
                    .strength(3.0f, 6.0f)
                    .noOcclusion()
                    .requiresCorrectToolForDrops(), CobblestoneGeneratorTier.NETHERITE));


    /**
     * 为指定方块注册对应的物品形式
     *
     * @param <T> 方块类型参数
     * @param name 方块名称，用于物品注册
     * @param block 延迟方块对象
     */
    private static <T extends Block> void registerBlockItems(String name, DeferredBlock<T> block) {
        ModItems.ITEMS_REGISTER.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    /**
     * 注册方块及其对应的物品形式
     *
     * @param <T> 方块类型参数
     * @param name 方块的注册名称
     * @param block 方块供应器
     * @return 注册的延迟方块对象
     */
    private static <T extends Block> DeferredBlock<T> registerBlocks(String name, Supplier<T> block) {
        DeferredBlock<T> blocks = BLOCK_REGISTER.register(name, block);
        registerBlockItems(name, blocks);
        return blocks;
    }

    // 向指定事件总线注册所有物品
    public static void register(IEventBus eventBus) {
        BLOCK_REGISTER.register(eventBus);
    }
}
