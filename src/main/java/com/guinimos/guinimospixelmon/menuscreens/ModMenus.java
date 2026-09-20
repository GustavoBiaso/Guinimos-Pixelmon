package com.guinimos.guinimospixelmon.menuscreens;

import com.guinimos.guinimospixelmon.GuinimosPixelmon;
import com.guinimos.guinimospixelmon.menuscreens.pokehunter.PokeHunterMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, GuinimosPixelmon.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<PokeHunterMenu>> POKEHUNTER_MENU = MENUS.register("pokehunter_menu",
            () -> IMenuTypeExtension.create(PokeHunterMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
