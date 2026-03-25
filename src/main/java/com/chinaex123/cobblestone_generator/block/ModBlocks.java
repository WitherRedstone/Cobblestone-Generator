package com.chinaex123.cobblestone_generator.block;

import com.chinaex123.cobblestone_generator.CobblestoneGenerator;
import com.chinaex123.cobblestone_generator.block.entity.NormalGeneratorBlockEntity;
import com.chinaex123.cobblestone_generator.block.entity.SpecialGeneratorBlockEntity;
import com.chinaex123.cobblestone_generator.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    // 创建方块注册器实例
    public static final DeferredRegister.Blocks BLOCK_REGISTER =
            DeferredRegister.createBlocks(CobblestoneGenerator.MOD_ID);

    private static Block createNormalGenerator(BlockBehaviour.Properties properties, CobblestoneGeneratorTier tier) {
        return new CobblestoneGeneratorBlock(properties, tier) {
            @Override
            public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
                return new NormalGeneratorBlockEntity(pos, state);
            }
        };
    }

    private static Block createSpecialGenerator(BlockBehaviour.Properties properties, CobblestoneGeneratorTier tier) {
        return new CobblestoneGeneratorBlock(properties, tier) {
            @Override
            public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
                return new SpecialGeneratorBlockEntity(pos, state);
            }
        };
    }

    // ==================== 普通圆石生成器 ====================
    // 石质圆石生成器
    public static final DeferredBlock<Block> STONE_COBBLEGEN =
            registerBlock("stone_cobblegen",
                    properties -> createNormalGenerator(properties, CobblestoneGeneratorTier.STONE),
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .sound(SoundType.STONE)
                            .strength(3.0f, 6.0f)
                            .noOcclusion().requiresCorrectToolForDrops());

    // 铜圆石生成器
    public static final DeferredBlock<Block> COPPER_COBBLEGEN =
            registerBlock("copper_cobblegen",
                    properties -> createNormalGenerator(properties, CobblestoneGeneratorTier.COPPER),
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_ORANGE)
                            .sound(SoundType.STONE)
                            .strength(3.0f, 6.0f)
                            .noOcclusion().requiresCorrectToolForDrops());

    // 铁圆石生成器
    public static final DeferredBlock<Block> IRON_COBBLEGEN =
            registerBlock("iron_cobblegen",
                    properties -> createNormalGenerator(properties, CobblestoneGeneratorTier.IRON),
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .sound(SoundType.STONE)
                            .strength(3.0f, 6.0f)
                            .noOcclusion().requiresCorrectToolForDrops());

    // 金圆石生成器
    public static final DeferredBlock<Block> GOLD_COBBLEGEN =
            registerBlock("gold_cobblegen",
                    properties -> createNormalGenerator(properties, CobblestoneGeneratorTier.GOLD),
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.GOLD)
                            .sound(SoundType.STONE)
                            .strength(3.0f, 6.0f)
                            .noOcclusion().requiresCorrectToolForDrops());

    // 钻石圆石生成器
    public static final DeferredBlock<Block> DIAMOND_COBBLEGEN =
            registerBlock("diamond_cobblegen",
                    properties -> createNormalGenerator(properties, CobblestoneGeneratorTier.DIAMOND),
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DIAMOND)
                            .sound(SoundType.STONE)
                            .strength(3.0f, 6.0f)
                            .noOcclusion().requiresCorrectToolForDrops());

    // 绿宝石圆石生成器
    public static final DeferredBlock<Block> EMERALD_COBBLEGEN =
            registerBlock("emerald_cobblegen",
                    properties -> createNormalGenerator(properties, CobblestoneGeneratorTier.EMERALD),
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.EMERALD)
                            .sound(SoundType.STONE)
                            .strength(3.0f, 6.0f)
                            .noOcclusion().requiresCorrectToolForDrops());

    // 下界合金圆石生成器
    public static final DeferredBlock<Block> NETHERITE_COBBLEGEN =
            registerBlock("netherite_cobblegen",
                    properties -> createNormalGenerator(properties, CobblestoneGeneratorTier.NETHERITE),
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .sound(SoundType.STONE)
                            .strength(3.0f, 6.0f)
                            .noOcclusion().requiresCorrectToolForDrops());

    // ==================== 特殊圆石生成器 ====================
    // 紫水晶圆石生成器
    public static final DeferredBlock<Block> AMETHYST_COBBLEGEN =
            registerBlock("amethyst_cobblegen",
                    properties -> createSpecialGenerator(properties, CobblestoneGeneratorTier.AMETHYST),
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .sound(SoundType.AMETHYST_CLUSTER)
                            .strength(3.0f, 6.0f)
                            .noOcclusion());

    // 红石圆石生成器
    public static final DeferredBlock<Block> REDSTONE_COBBLEGEN =
            registerBlock("redstone_cobblegen",
                    properties -> createSpecialGenerator(properties, CobblestoneGeneratorTier.REDSTONE),
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_RED)
                            .sound(SoundType.STONE)
                            .strength(3.0f, 6.0f)
                            .noOcclusion()
                            .lightLevel(state -> 9)
                            .isRedstoneConductor((state, level, pos) -> true));

    // 荧石圆石生成器
    public static final DeferredBlock<Block> GLOWSTONE_COBBLEGEN =
            registerBlock("glowstone_cobblegen",
                    properties -> createSpecialGenerator(properties, CobblestoneGeneratorTier.GLOWSTONE),
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_RED)
                            .sound(SoundType.GLASS)
                            .strength(3.0f, 6.0f)
                            .noOcclusion()
                            .lightLevel(state -> 15));

    // 干草块圆石生成器
    public static final DeferredBlock<Block> HAYBLOCK_COBBLEGEN =
            registerBlock("hayblock_cobblegen",
                    properties -> createSpecialGenerator(properties, CobblestoneGeneratorTier.HAYBLOCK),
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_RED)
                            .sound(SoundType.GRASS)
                            .strength(3.0f, 6.0f)
                            .noOcclusion());

    // 幽匿圆石生成器
    public static final DeferredBlock<Block> SCULK_COBBLEGEN =
            registerBlock("sculk_cobblegen",
                    properties -> createSpecialGenerator(properties, CobblestoneGeneratorTier.SCULK),
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_RED)
                            .sound(SoundType.SCULK)
                            .strength(3.0f, 6.0f)
                            .noOcclusion());

    private static <T extends Block> DeferredBlock<T> registerBlock(String name,
                                                                    Function<BlockBehaviour.Properties, T> func,
                                                                    Supplier<BlockBehaviour.Properties> properties) {
        DeferredBlock<T> block = BLOCK_REGISTER.registerBlock(name, func, properties);
        ModItems.ITEMS_REGISTER.registerSimpleBlockItem(name, block);
        return block;
    }

    // 向指定事件总线注册所有物品
    public static void register(IEventBus eventBus) {
        BLOCK_REGISTER.register(eventBus);
    }
}
