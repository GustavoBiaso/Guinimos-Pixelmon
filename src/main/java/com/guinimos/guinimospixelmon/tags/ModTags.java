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

    private static TagKey<Structure> structure(String name) {
        return TagKey.create(Registries.STRUCTURE,
                ResourceLocation.fromNamespaceAndPath(GuinimosPixelmon.MODID, name));
    }
}