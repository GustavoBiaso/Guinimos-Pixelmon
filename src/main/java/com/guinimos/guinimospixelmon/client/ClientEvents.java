package com.guinimos.guinimospixelmon.client;

import com.guinimos.guinimospixelmon.GuinimosPixelmon;
import com.guinimos.guinimospixelmon.menuscreens.pokehunter.PokeHunterScreen;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import static com.guinimos.guinimospixelmon.menuscreens.ModMenus.POKEHUNTER_MENU;

@EventBusSubscriber(value = Dist.CLIENT, modid = GuinimosPixelmon.MODID)
public class ClientEvents {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(POKEHUNTER_MENU.get(), PokeHunterScreen::new);
    }
}