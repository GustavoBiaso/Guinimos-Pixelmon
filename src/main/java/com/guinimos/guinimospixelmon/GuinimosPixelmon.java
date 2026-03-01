package com.guinimos.guinimospixelmon;

import com.guinimos.guinimospixelmon.CreativeTabs.ModCreativeTabs;
import com.guinimos.guinimospixelmon.component.ModDataComponents;
import com.guinimos.guinimospixelmon.item.ModItems;
import com.guinimos.guinimospixelmon.recipes.BreederBottleRecipeSerializer;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(GuinimosPixelmon.MODID)
public class GuinimosPixelmon {
    public static final String MODID = "guinimospixelmon";
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public GuinimosPixelmon(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        NeoForge.EVENT_BUS.register(new EventHandler());

        ModDataComponents.register(modEventBus);


        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code

    }
}
