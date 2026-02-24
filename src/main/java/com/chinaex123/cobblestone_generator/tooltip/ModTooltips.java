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

            // 添加分割线
            tooltip.add(Component.literal(""));

            // 获取配置的数值
            int outputAmount = CobblestoneGeneratorConfig.getOutputAmount(tier);
            int generationTicks = CobblestoneGeneratorConfig.getGenerationTicks(tier);
            double speedMultiplier = CobblestoneGeneratorConfig.getSpeedMultiplier();

            // 计算实际生成间隔（考虑速度倍数）
            int actualTicks = (int) Math.max(1, generationTicks / speedMultiplier);

            // 使用游戏内本地化获取圆石名称
            ItemStack cobblestoneStack = new ItemStack(Items.COBBLESTONE);
            String cobblestoneName = cobblestoneStack.getHoverName().getString();

            // 显示生成数量
            tooltip.add(Component.translatable("tooltip.cobblestone_generator.output_amount")
                    .append(": " + outputAmount + " " + cobblestoneName));

            // 显示tick间隔
            tooltip.add(Component.translatable("tooltip.cobblestone_generator.generation_time")
                    .append(": " + actualTicks + " tick"));

            // 如果有速度倍数调整，显示原始速度
            if (Math.abs(speedMultiplier - 1.0) > 0.01) {
                tooltip.add(Component.translatable("tooltip.cobblestone_generator.speed_multiplier")
                        .append(": " + String.format("%.1fx", speedMultiplier)));
            }
        }
    }
}
