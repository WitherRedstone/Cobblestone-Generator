package com.chinaex123.cobblestone_generator;

import com.chinaex123.cobblestone_generator.block.CobblestoneGeneratorBlock;
import com.chinaex123.cobblestone_generator.block.ModBlocks;
import com.chinaex123.cobblestone_generator.block.entity.BaseGeneratorBlockEntity;
import com.chinaex123.cobblestone_generator.block.entity.ModBlockEntities;
import com.chinaex123.cobblestone_generator.config.CobblestoneGeneratorConfig;
import com.chinaex123.cobblestone_generator.item.ModItems;
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
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(CobblestoneGenerator.MOD_ID)
public class CobblestoneGenerator {
    // 在公共位置定义模组ID，供所有地方引用
    public static final String MOD_ID = "cobblestone_generator";

    public CobblestoneGenerator(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerRedstoneTick);

        ModCreativeTabs.register(modEventBus); // 创造模式物品栏
        ModBlocks.register(modEventBus);      // 注册方块
        ModItems.register(modEventBus);        // 注册物品
        ModBlockEntities.register(modEventBus); // 注册方块实体
        // 注册服务端配置
        modContainer.registerConfig(ModConfig.Type.COMMON, CobblestoneGeneratorConfig.SPEC);
        // 注册能力
        modEventBus.addListener(BaseGeneratorBlockEntity::registerCapabilities);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // 配置加载完成后刷新缓存
        event.enqueueWork(CobblestoneGeneratorConfig::onConfigReload);
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
