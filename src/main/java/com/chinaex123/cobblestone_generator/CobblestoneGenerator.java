package com.chinaex123.cobblestone_generator;

import com.chinaex123.cobblestone_generator.block.ModBlocks;
import com.chinaex123.cobblestone_generator.block.entity.CobblestoneGeneratorBlockEntity;
import com.chinaex123.cobblestone_generator.block.entity.ModBlockEntities;
import com.chinaex123.cobblestone_generator.item.ModItems;
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
        NeoForge.EVENT_BUS.register(this);

        // 注册网络数据包处理器
        //modEventBus.addListener(ShippingBoxNetworking::register);

        ModCreativeTabs.register(modEventBus); // 创造模式物品栏

        ModBlocks.register(modEventBus);      // 注册方块
        ModItems.register(modEventBus);        // 注册物品
        ModBlockEntities.register(modEventBus); // 注册方块实体

        // 注册能力
        modEventBus.addListener(CobblestoneGeneratorBlockEntity::registerCapabilities);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {}

    // 可以使用 @SubscribeEvent 并让事件总线自动发现要调用的方法
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}
}
