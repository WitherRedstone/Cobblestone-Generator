package com.chinaex123.cobblestone_generator.init;

import com.chinaex123.cobblestone_generator.CobblestoneGenerator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS_REGISTER = DeferredRegister.createItems(CobblestoneGenerator.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS_REGISTER.register(eventBus);
    }
}
