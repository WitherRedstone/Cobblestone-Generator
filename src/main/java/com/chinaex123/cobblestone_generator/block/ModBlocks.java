package com.chinaex123.cobblestone_generator.block;

import com.chinaex123.cobblestone_generator.CobblestoneGenerator;
import com.chinaex123.cobblestone_generator.block.entity.ModBlockEntities;
import com.chinaex123.cobblestone_generator.block.entity.NormalGeneratorBlockEntity;
import com.chinaex123.cobblestone_generator.block.entity.SpecialGeneratorBlockEntity;
import com.chinaex123.cobblestone_generator.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    // 创建方块注册器实例
    public static final DeferredRegister.Blocks BLOCK_REGISTER =
            DeferredRegister.createBlocks(CobblestoneGenerator.MOD_ID);

    /**
     * 注册普通类型的圆石生成器方块。
     * 创建具有指定属性和等级的普通生成器方块，并为其指定普通生成器方块实体。
     *
     * @param name 方块的注册名称
     * @param properties 方块的基本属性
     * @param tier 圆石生成器的等级类型
     * @return 注册的延迟方块对象
     */
    private static DeferredBlock<Block> registerNormalGenerator(String name, BlockBehaviour.Properties properties, CobblestoneGeneratorTier tier) {
        return registerBlocks(name, () -> new CobblestoneGeneratorBlock(properties, tier) {
            @Override
            public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
                return new NormalGeneratorBlockEntity(pos, state);
            }
        });
    }

    /**
     * 注册特殊类型的圆石生成器方块。
     * 创建具有指定属性和等级的特殊生成器方块，并为其指定特殊生成器方块实体。
     *
     * @param name 方块的注册名称
     * @param properties 方块的基本属性
     * @param tier 圆石生成器的等级类型
     * @return 注册的延迟方块对象
     */
    private static DeferredBlock<Block> registerSpecialGenerator(String name, BlockBehaviour.Properties properties, CobblestoneGeneratorTier tier) {
        return registerBlocks(name, () -> new CobblestoneGeneratorBlock(properties, tier) {
            @Override
            public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
                return new SpecialGeneratorBlockEntity(pos, state);
            }
        });
    }

    // ==================== 普通圆石生成器 ====================
    // 石质圆石生成器
    public static final DeferredBlock<Block> STONE_COBBLEGEN = registerNormalGenerator("stone_cobblegen",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .sound(SoundType.STONE)
                    .strength(3.0f, 6.0f)
                    .noOcclusion()
                    .requiresCorrectToolForDrops(), CobblestoneGeneratorTier.STONE);
    // 铜圆石生成器
    public static final DeferredBlock<Block> COPPER_COBBLEGEN = registerNormalGenerator("copper_cobblegen",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .sound(SoundType.STONE)
                    .strength(3.0f, 6.0f)
                    .noOcclusion()
                    .requiresCorrectToolForDrops(), CobblestoneGeneratorTier.COPPER);
    //  铁圆石生成器
    public static final DeferredBlock<Block> IRON_COBBLEGEN = registerNormalGenerator("iron_cobblegen",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.STONE)
                    .strength(3.0f, 6.0f)
                    .noOcclusion()
                    .requiresCorrectToolForDrops(), CobblestoneGeneratorTier.IRON);
    // 金圆石生成器
    public static final DeferredBlock<Block> GOLD_COBBLEGEN = registerNormalGenerator("gold_cobblegen",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .sound(SoundType.STONE)
                    .strength(3.0f, 6.0f)
                    .noOcclusion()
                    .requiresCorrectToolForDrops(), CobblestoneGeneratorTier.GOLD);
    // 钻石圆石生成器
    public static final DeferredBlock<Block> DIAMOND_COBBLEGEN = registerNormalGenerator("diamond_cobblegen",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIAMOND)
                    .sound(SoundType.STONE)
                    .strength(3.0f, 6.0f)
                    .noOcclusion()
                    .requiresCorrectToolForDrops(), CobblestoneGeneratorTier.DIAMOND);
    //  绿宝石圆石生成器
    public static final DeferredBlock<Block> EMERALD_COBBLEGEN = registerNormalGenerator("emerald_cobblegen",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.EMERALD)
                    .sound(SoundType.STONE)
                    .strength(3.0f, 6.0f)
                    .noOcclusion()
                    .requiresCorrectToolForDrops(), CobblestoneGeneratorTier.EMERALD);
    // 下界合金圆石生成器
    public static final DeferredBlock<Block> NETHERITE_COBBLEGEN = registerNormalGenerator("netherite_cobblegen",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .sound(SoundType.STONE)
                    .strength(3.0f, 6.0f)
                    .noOcclusion()
                    .requiresCorrectToolForDrops(), CobblestoneGeneratorTier.NETHERITE);

    // ==================== 特殊圆石生成器 ====================
    // 紫水晶圆石生成器
    public static final DeferredBlock<Block> AMETHYST_COBBLEGEN = registerSpecialGenerator("amethyst_cobblegen",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .sound(SoundType.AMETHYST_CLUSTER)
                    .strength(3.0f, 6.0f)
                    .noOcclusion()
                    .requiresCorrectToolForDrops(), CobblestoneGeneratorTier.AMETHYST);
    // 红石圆石生成器
    public static final DeferredBlock<Block> REDSTONE_COBBLEGEN = registerSpecialGenerator("redstone_cobblegen",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .sound(SoundType.STONE)
                    .strength(3.0f, 6.0f)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 9)
                    .isRedstoneConductor((state, level, pos) -> true), CobblestoneGeneratorTier.REDSTONE);
    // 荧石圆石生成器
    public static final DeferredBlock<Block> GLOWSTONE_COBBLEGEN = registerSpecialGenerator("glowstone_cobblegen",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .sound(SoundType.GLASS)
                    .strength(3.0f, 6.0f)
                    .noOcclusion()
                    .lightLevel(state -> 15)
                    .requiresCorrectToolForDrops(), CobblestoneGeneratorTier.GLOWSTONE);
    //  干草块圆石生成器
    public static final DeferredBlock<Block> HAYBLOCK_COBBLEGEN = registerSpecialGenerator("hayblock_cobblegen",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .sound(SoundType.GRASS)
                    .strength(3.0f, 6.0f)
                    .noOcclusion()
                    .requiresCorrectToolForDrops(), CobblestoneGeneratorTier.HAYBLOCK);
    // 幽匿圆石生成器
    public static final DeferredBlock<Block> SCULK_COBBLEGEN = registerSpecialGenerator("sculk_cobblegen",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .sound(SoundType.SCULK)
                    .strength(3.0f, 6.0f)
                    .noOcclusion()
                    .requiresCorrectToolForDrops(), CobblestoneGeneratorTier.SCULK);

    /**
     * 为指定方块注册对应的物品形式
     */
    private static <T extends Block> void registerBlockItems(String name, DeferredBlock<T> block) {
        ModItems.ITEMS_REGISTER.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    /**
     * 注册方块及其对应的物品形式
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
