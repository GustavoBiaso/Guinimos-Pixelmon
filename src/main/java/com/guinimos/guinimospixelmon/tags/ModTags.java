package com.guinimos.guinimospixelmon.tags;

import com.guinimos.guinimospixelmon.GuinimosPixelmon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ModTags {
    public static final TagKey<Structure> MOLTRES_TEMPLE = structure("moltres_temple");
    public static final TagKey<Structure> ZAPDOS_TEMPLE = structure("zapdos_temple");
    public static final TagKey<Structure> ARTICUNO_TEMPLE = structure("articuno_temple");
    public static final TagKey<Structure> CELEBI_TEMPLE = structure("celebi_temple");
    public static final TagKey<Structure> ARCEUS_TEMPLE = structure("arceus_temple");

    public static final TagKey<Structure> DRAGON_GYM = structure("dragon_gym");
    public static final TagKey<Structure> ELECTRIC_GYM = structure("electric_gym");
    public static final TagKey<Structure> FAIRY_GYM = structure("fairy_gym");
    public static final TagKey<Structure> FIRE_GYM = structure("fire_gym");
    public static final TagKey<Structure> GHOST_GYM = structure("ghost_gym");
    public static final TagKey<Structure> GRASS_GYM = structure("grass_gym");
    public static final TagKey<Structure> GROUND_GYM = structure("ground_gym");
    public static final TagKey<Structure> ICE_GYM = structure("ice_gym");
    public static final TagKey<Structure> STEEL_GYM = structure("steel_gym");
    public static final TagKey<Structure> WATER_GYM = structure("water_gym");

    private static TagKey<Structure> structure(String name) {
        return TagKey.create(Registries.STRUCTURE,
                ResourceLocation.fromNamespaceAndPath(GuinimosPixelmon.MODID, name));
    }
}