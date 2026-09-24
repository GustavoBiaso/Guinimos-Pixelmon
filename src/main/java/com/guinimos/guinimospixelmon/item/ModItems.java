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

    public static final DeferredItem<Item> CELEBI_MAP = ITEMS.register("celebi_temple_map",
            () -> new TempleMapsItem(new Item.Properties(), ModTags.CELEBI_TEMPLE, MapDecorationTypes.RED_X, "Templo do Celebi"));

    public static final DeferredItem<Item> ARCEUS_MAP = ITEMS.register("arceus_temple_map",
            () -> new TempleMapsItem(new Item.Properties(), ModTags.ARCEUS_TEMPLE, MapDecorationTypes.RED_X, "Templo do Arceus"));

    public static final DeferredItem<Item> DRAGON_GYM_MAP = ITEMS.register("dragon_gym_map",
            () -> new TempleMapsItem(new Item.Properties(), ModTags.DRAGON_GYM, MapDecorationTypes.RED_X, "Ginásio Dragão"));

    public static final DeferredItem<Item> ELECTRIC_GYM_MAP = ITEMS.register("electric_gym_map",
            () -> new TempleMapsItem(new Item.Properties(), ModTags.ELECTRIC_GYM, MapDecorationTypes.RED_X, "Ginásio Elétrico"));

    public static final DeferredItem<Item> FAIRY_GYM_MAP = ITEMS.register("fairy_gym_map",
            () -> new TempleMapsItem(new Item.Properties(), ModTags.FAIRY_GYM, MapDecorationTypes.RED_X, "Ginásio Fada"));

    public static final DeferredItem<Item> FIRE_GYM_MAP = ITEMS.register("fire_gym_map",
            () -> new TempleMapsItem(new Item.Properties(), ModTags.FIRE_GYM, MapDecorationTypes.RED_X, "Ginásio Fogo"));

    public static final DeferredItem<Item> GHOST_GYM_MAP = ITEMS.register("ghost_gym_map",
            () -> new TempleMapsItem(new Item.Properties(), ModTags.GHOST_GYM, MapDecorationTypes.RED_X, "Ginásio Fantasma"));

    public static final DeferredItem<Item> GRASS_GYM_MAP = ITEMS.register("grass_gym_map",
            () -> new TempleMapsItem(new Item.Properties(), ModTags.GRASS_GYM, MapDecorationTypes.RED_X, "Ginásio Grama"));

    public static final DeferredItem<Item> GROUND_GYM_MAP = ITEMS.register("ground_gym_map",
            () -> new TempleMapsItem(new Item.Properties(), ModTags.GROUND_GYM, MapDecorationTypes.RED_X, "Ginásio Terra"));

    public static final DeferredItem<Item> ICE_GYM_MAP = ITEMS.register("ice_gym_map",
            () -> new TempleMapsItem(new Item.Properties(), ModTags.ICE_GYM, MapDecorationTypes.RED_X, "Ginásio Gelo"));

    public static final DeferredItem<Item> STEEL_GYM_MAP = ITEMS.register("steel_gym_map",
            () -> new TempleMapsItem(new Item.Properties(), ModTags.STEEL_GYM, MapDecorationTypes.RED_X, "Ginásio Ferro"));

    public static final DeferredItem<Item> WATER_GYM_MAP = ITEMS.register("water_gym_map",
            () -> new TempleMapsItem(new Item.Properties(), ModTags.WATER_GYM, MapDecorationTypes.RED_X, "Ginásio Água"));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
