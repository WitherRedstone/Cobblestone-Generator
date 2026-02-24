package com.chinaex123.cobblestone_generator;

import com.chinaex123.cobblestone_generator.block.CobblestoneGeneratorBlock;
import com.chinaex123.cobblestone_generator.block.ModBlocks;
import com.chinaex123.cobblestone_generator.block.entity.CobblestoneGeneratorBlockEntity;
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
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// 这里的值应与 META-INF/neoforge.mods.toml 文件中的条目对应
@Mod(CobblestoneGenerator.MOD_ID)
public class CobblestoneGenerator {
    // 在公共位置定义模组ID，供所有地方引用
    public static final String MOD_ID = "cobblestone_generator";
    // 直接引用 slf4j 日志记录器
    public static final Logger LOGGER = LogUtils.getLogger();

    // 模组类的构造函数是模组加载时运行的第一段代码
    // FML 会自动识别某些参数类型（如 IEventBus 或 ModContainer）并传入
    public CobblestoneGenerator(IEventBus modEventBus, ModContainer modContainer) {
        // 为模组加载注册 commonSetup 方法
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerRedstoneTick);
        NeoForge.EVENT_BUS.register(this);

        ModCreativeTabs.register(modEventBus); // 创造模式物品栏

        ModBlocks.register(modEventBus);      // 注册方块
        ModItems.register(modEventBus);        // 注册物品
        ModBlockEntities.register(modEventBus); // 注册方块实体

        // 注册服务端配置
        modContainer.registerConfig(ModConfig.Type.COMMON, CobblestoneGeneratorConfig.SPEC);

        // 注册能力
        modEventBus.addListener(CobblestoneGeneratorBlockEntity::registerCapabilities);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // 配置加载完成后刷新缓存
        event.enqueueWork(CobblestoneGeneratorConfig::onConfigReload);
    }

    // 红石tick注册
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
                    }// 可以在这里添加额外的红石逻辑
                }
            });
        }
    }

    // 可以使用 @SubscribeEvent 并让事件总线自动发现要调用的方法
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}
}
