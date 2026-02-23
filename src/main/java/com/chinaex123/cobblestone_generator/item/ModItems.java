package com.chinaex123.cobblestone_generator.item;

import com.chinaex123.cobblestone_generator.CobblestoneGenerator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModItems {
    public static final DeferredRegister.Items ITEMS_REGISTER =
            DeferredRegister.createItems(CobblestoneGenerator.MOD_ID);

    // 向指定事件总线注册所有物品
    public static void register(IEventBus eventBus) {
        ITEMS_REGISTER.register(eventBus);
    }
}
