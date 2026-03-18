package com.guinimos.guinimospixelmon.CreativeTabs;

import com.guinimos.guinimospixelmon.GuinimosPixelmon;
import com.guinimos.guinimospixelmon.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GuinimosPixelmon.MODID);

    public static final Supplier<CreativeModeTab> GUINIMOS_PIXELMON_TAB = CREATIVE_MODE_TAB.register("guinimos_pixelmon_item_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.BERRY_CATCHER.get()))
                    .title(Component.translatable("creativetab.guinimospixelmon.items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.BERRY_CATCHER);
                        output.accept(ModItems.BREEDER_BOTTLE);
                        output.accept(ModItems.LURE_CRAFTER);
                    }).build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
