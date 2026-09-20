package com.guinimos.guinimospixelmon.creativetabs;

import com.guinimos.guinimospixelmon.GuinimosPixelmon;
import com.guinimos.guinimospixelmon.block.ModBlocks;
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

    public static final Supplier<CreativeModeTab> GUINIMOS_PIXELMON_TAB = CREATIVE_MODE_TAB.register("guinimos_pixelmon_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.BERRY_CATCHER.get()))
                    .title(Component.translatable("creativetab.guinimospixelmon.items"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.BERRY_CATCHER);
                        output.accept(ModItems.BREEDER_BOTTLE);
                        output.accept(ModItems.LURE_CRAFTER);
                        output.accept(ModItems.POKE_HUNTER);
                        output.accept(ModItems.DYNAMAX_QUEST_GIVER);
                        output.accept(ModItems.PORTABLE_HEALER);
                        output.accept(ModItems.MOLTRES_MAP);
                        output.accept(ModItems.ZAPDOS_MAP);
                        output.accept(ModItems.ARTICUNO_MAP);
                        output.accept(ModBlocks.RECYCLER);
                    }).build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
