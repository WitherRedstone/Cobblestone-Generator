package com.chinaex123.cobblestone_generator.block.functions;

import com.chinaex123.cobblestone_generator.config.CobblestoneGeneratorConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class SculkCobblegen {

    /**
     * 处理幽匿圆石生成器的特殊功能
     * 将周围指定范围内的目标方块转换为幽匿块
     *
     * @param level 当前世界对象
     * @param generatorPos 幽匿圆石生成器的位置
     */
    public static void handleSculkFunction(Level level, BlockPos generatorPos) {
        // 只在服务端执行
        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) {
            return;
        }

        // 每20游戏刻执行一次（1秒），控制执行频率
        if (serverLevel.getGameTime() % 20 != 0) {
            return;
        }

        // 获取配置
        List<String> targetBlocks = CobblestoneGeneratorConfig.getSculkTargetBlocks();
        int radius = CobblestoneGeneratorConfig.getSculkConversionRadius();
        int chance = CobblestoneGeneratorConfig.getSculkConversionChance();

        // 使用正确的随机数生成器类型
        RandomSource random = serverLevel.random;

        // 遍历周围区域
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    // 跳过生成器本身位置
                    if (x == 0 && y == 0 && z == 0) continue;

                    BlockPos targetPos = generatorPos.offset(x, y, z);
                    BlockState targetState = serverLevel.getBlockState(targetPos);

                    // 检查是否为目标方块
                    if (isTargetBlock(serverLevel, targetState, targetBlocks)) {
                        // 调整概率计算：将配置的概率作为整体成功率
                        // 例如：配置10%概率，在26个方块中平均约2-3个会被转换
                        if (random.nextInt(1000) < chance * 10) {  // 将概率精度提高到千分之一
                            // 转换为幽匿块
                            serverLevel.setBlock(targetPos, Blocks.SCULK.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
    }

    /**
     * 检查指定方块是否为目标方块列表中的成员。
     * 支持两种格式的目标方块定义：方块标签（以#开头）和具体方块ID。
     *
     * @param level 当前服务器世界对象，用于访问注册表等信息
     * @param state 要检查的方块状态
     * @param targetBlocks 目标方块列表，支持方块标签（如"#minecraft:base_stone_overworld"）和具体方块ID（如"minecraft:stone"）
     * @return 如果方块匹配任一目标则返回true，否则返回false
     */
    private static boolean isTargetBlock(ServerLevel level, BlockState state, List<String> targetBlocks) {
        Block block = state.getBlock();

        // 遍历所有目标方块定义
        for (String target : targetBlocks) {
            // 处理tag格式 (#tag_name)
            if (target.startsWith("#")) {
                String tagName = target.substring(1);
                ResourceLocation tagLocation = ResourceLocation.tryParse(tagName);
                if (tagLocation != null) {
                    // 使用BlockState的is方法检查方块是否属于指定标签
                    if (state.is(TagKey.create(Registries.BLOCK, tagLocation))) {
                        return true;
                    }
                }
            }
            // 处理方块ID格式 (namespace:path)
            else {
                ResourceLocation blockLocation = ResourceLocation.tryParse(target);
                if (blockLocation != null) {
                    Block targetBlock = BuiltInRegistries.BLOCK.get(blockLocation);
                    // 检查方块是否与目标方块完全匹配
                    if (block == targetBlock) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
