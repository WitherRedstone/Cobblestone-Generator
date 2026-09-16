package com.chinaex123.cobblestone_generator.tooltip;

import com.chinaex123.cobblestone_generator.block.CobblestoneGeneratorBlock;
import com.chinaex123.cobblestone_generator.block.CobblestoneGeneratorTier;
import com.chinaex123.cobblestone_generator.config.CobblestoneGeneratorConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

@EventBusSubscriber(modid = "cobblestone_generator", value = Dist.CLIENT)
public class ModTooltips {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        // 检查是否模组的方块物品
        if (stack.getItem() instanceof BlockItem blockItem &&
                blockItem.getBlock() instanceof CobblestoneGeneratorBlock generatorBlock) {

            List<Component> tooltip = event.getToolTip();
            CobblestoneGeneratorTier tier = generatorBlock.getTier();

            // 获取配置的数值
            int outputCount = CobblestoneGeneratorConfig.getOutputCount(tier);
            int generationTicks = CobblestoneGeneratorConfig.getGenerationTicks(tier);
            double speedMultiplier = CobblestoneGeneratorConfig.getSpeedMultiplier();

            // 计算实际生成间隔（考虑速度倍数）
            int actualTicks = (int) Math.max(1, generationTicks / speedMultiplier);

            // 使用游戏内本地化获取圆石名称
            ItemStack cobblestoneStack = new ItemStack(Items.COBBLESTONE);
            String cobblestoneName = cobblestoneStack.getHoverName().getString();

            // 显示生成数量
            tooltip.add(Component.translatable("tooltip.cobblestone_generator.output_count")
                    .append(": " + outputCount + " " + cobblestoneName));

            // 显示tick间隔
            tooltip.add(Component.translatable("tooltip.cobblestone_generator.generation_time")
                    .append(": " + actualTicks + " tick"));

            // 如果有速度倍数调整，显示原始速度
            if (Math.abs(speedMultiplier - 1.0) > 0.01) {
                tooltip.add(Component.translatable("tooltip.cobblestone_generator.speed_multiplier")
                        .append(": " + String.format("%.1fx", speedMultiplier)));
            }

            // 为红石等级添加特殊tooltip
            if (tier == CobblestoneGeneratorTier.REDSTONE) {
                tooltip.add(Component.literal(""));
                tooltip.add(Component.translatable("tooltip.cobblestone_generator.redstone_description"));

                // 显示红石信号模式
                CobblestoneGeneratorConfig.RedstoneSignalMode signalMode = CobblestoneGeneratorConfig.getRedstoneSignalMode();
                tooltip.add(Component.translatable("tooltip.cobblestone_generator.redstone_mode")
                        .append(": ").append(Component.translatable("redstone_signal_mode." + signalMode.name().toLowerCase())));

                // 如果是INTERVAL模式，显示间隔tick
                if (signalMode == CobblestoneGeneratorConfig.RedstoneSignalMode.INTERVAL) {
                    int interval = CobblestoneGeneratorConfig.getRedstoneSignalInterval();
                    tooltip.add(Component.translatable("tooltip.cobblestone_generator.redstone_interval")
                            .append(": " + interval + " tick"));
                }
            }

            // 为紫水晶等级添加特殊tooltip
            if (tier == CobblestoneGeneratorTier.AMETHYST) {
                tooltip.add(Component.literal(""));
                tooltip.add(Component.translatable("tooltip.cobblestone_generator.amethyst_description"));

                // 显示紫水晶加速倍数
                double growthMultiplier = CobblestoneGeneratorConfig.getAmethystGrowthSpeedMultiplier();
                tooltip.add(Component.translatable("tooltip.cobblestone_generator.amethyst_growth_boost")
                        .append(": " + String.format("%.1f", growthMultiplier) + "x"));
            }

            // 为荧石等级添加特殊tooltip
            if (tier == CobblestoneGeneratorTier.GLOWSTONE) {
                tooltip.add(Component.literal(""));
                tooltip.add(Component.translatable("tooltip.cobblestone_generator.glowstone_description"));
            }

            // 为干草块等级添加特殊tooltip
            if (tier == CobblestoneGeneratorTier.HAYBLOCK) {
                tooltip.add(Component.literal(""));
                tooltip.add(Component.translatable("tooltip.cobblestone_generator.hayblock_description"));
            }

            // 为幽匿等级添加特殊tooltip
            if (tier == CobblestoneGeneratorTier.SCULK) {
                tooltip.add(Component.literal(""));
                tooltip.add(Component.translatable("tooltip.cobblestone_generator.sculk_description"));

                // 显示转换范围
                int radius = CobblestoneGeneratorConfig.getSculkConversionRadius();
                tooltip.add(Component.translatable("tooltip.cobblestone_generator.sculk_radius")
                        .append(": " + radius + " ")
                        .append(Component.translatable("tooltip.cobblestone_generator.blocks")));

                // 显示转换概率
                int chance = CobblestoneGeneratorConfig.getSculkConversionChance();
                tooltip.add(Component.translatable("tooltip.cobblestone_generator.sculk_chance")
                        .append(": " + chance + "%"));
            }
        }
    }
}
