package com.guinimos.guinimospixelmon.item;

import com.guinimos.guinimospixelmon.GuinimosPixelmon;
import com.guinimos.guinimospixelmon.component.ModDataComponents;
import com.guinimos.guinimospixelmon.item.custom.BerryCatcherItem;
import com.guinimos.guinimospixelmon.item.custom.BreederBottleItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GuinimosPixelmon.MODID);

    public static final DeferredItem<Item>  BERRY_CATCHER = ITEMS.register("berry_catcher",
            () -> new BerryCatcherItem(new BerryCatcherItem.Properties().stacksTo(1).durability(365)));

    public static final DeferredItem<Item>  BREEDER_BOTTLE = ITEMS.register("breeder_bottle",
            () -> new BreederBottleItem(new BreederBottleItem.Properties().stacksTo(1).component(ModDataComponents.TIME, 0)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
