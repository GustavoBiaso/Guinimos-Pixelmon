package com.guinimos.guinimospixelmon.item;

import com.guinimos.guinimospixelmon.GuinimosPixelmon;
import com.guinimos.guinimospixelmon.component.ModDataComponents;
import com.guinimos.guinimospixelmon.item.custom.*;
import com.guinimos.guinimospixelmon.tags.ModTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GuinimosPixelmon.MODID);

    public static final DeferredItem<Item>  BERRY_CATCHER = ITEMS.register("berry_catcher",
            () -> new BerryCatcherItem(new BerryCatcherItem.Properties().stacksTo(1).durability(365)));

    public static final DeferredItem<Item>  BREEDER_BOTTLE = ITEMS.register("breeder_bottle",
            () -> new BreederBottleItem(new BreederBottleItem.Properties().stacksTo(1).component(ModDataComponents.TIME, 0)));

    public static final DeferredItem<Item>  LURE_CRAFTER = ITEMS.register("lure_crafter",
            () -> new LureCrafterItem(new LureCrafterItem.Properties().stacksTo(1).component(ModDataComponents.ACTIVE, false)));

    public static final DeferredItem<Item>  POKE_HUNTER = ITEMS.register("poke_hunter",
            () -> new PokeHunterItem(new PokeHunterItem.Properties().stacksTo(1)));

    public static final DeferredItem<Item>  DYNAMAX_QUEST_GIVER = ITEMS.register("dynamax_quest_giver",
            () -> new DynamaxQuestGiverItem(new DynamaxQuestGiverItem.Properties().stacksTo(16)));

    public static final DeferredItem<Item>  PORTABLE_HEALER = ITEMS.register("portable_healer",
            () -> new PortableHealerItem(new PortableHealerItem.Properties().stacksTo(1).durability(360)));

    public static final DeferredItem<Item> MOLTRES_MAP = ITEMS.register("moltres_temple_map",
            () -> new TempleMapsItem(new Item.Properties(), ModTags.MOLTRES_TEMPLE, MapDecorationTypes.RED_X, "Templo do Moltres"));

    public static final DeferredItem<Item> ZAPDOS_MAP = ITEMS.register("zapdos_temple_map",
            () -> new TempleMapsItem(new Item.Properties(), ModTags.ZAPDOS_TEMPLE, MapDecorationTypes.RED_X, "Templo do Zapdos"));

    public static final DeferredItem<Item> ARTICUNO_MAP = ITEMS.register("articuno_temple_map",
            () -> new TempleMapsItem(new Item.Properties(), ModTags.ARTICUNO_TEMPLE, MapDecorationTypes.RED_X, "Templo do Articuno"));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
