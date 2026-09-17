package com.chinaex123.cobblestone_generator;

import com.chinaex123.cobblestone_generator.block.CobblestoneGeneratorBlock;
import com.chinaex123.cobblestone_generator.init.CGBlocks;
import com.chinaex123.cobblestone_generator.block.entity.BaseGeneratorBlockEntity;
import com.chinaex123.cobblestone_generator.init.CGBlockEntities;
import com.chinaex123.cobblestone_generator.config.CGServerConfig;
import com.chinaex123.cobblestone_generator.init.CGCreativeTabs;
import com.chinaex123.cobblestone_generator.init.CGItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;

@Mod(CobblestoneGenerator.MOD_ID)
public class CobblestoneGenerator {
    public static final String MOD_ID = "cobblestone_generator";

    public CobblestoneGenerator(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerRedstoneTick);

        CGCreativeTabs.register(modEventBus);
        CGBlocks.register(modEventBus);
        CGItems.register(modEventBus);
        CGBlockEntities.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, CGServerConfig.SPEC);
        modEventBus.addListener(BaseGeneratorBlockEntity::registerCapabilities);
    }

    /**
     * 注册红石相关的tick事件监听器。
     * 该方法在方块注册事件中注册邻居通知事件监听器，用于处理红石信号相关的逻辑。
     *
     * @param event 注册事件对象，包含注册的资源信息
     */
    private void registerRedstoneTick(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.BLOCK) {
            // 注册红石信号衰减的tick回调
            NeoForge.EVENT_BUS.addListener((BlockEvent.NeighborNotifyEvent neighborEvent) -> {
                BlockState state = neighborEvent.getState();
                if (state.getBlock() instanceof CobblestoneGeneratorBlock) {
                    BlockPos pos = neighborEvent.getPos();
                    Level level = (Level) neighborEvent.getLevel();

                    // 当方块状态改变时检查是否需要处理红石信号
                    if (state.hasProperty(CobblestoneGeneratorBlock.POWER)) {
                        state.getValue(CobblestoneGeneratorBlock.POWER);
                    }
                }
            });
        }
    }
}
