package com.guinimos.guinimospixelmon.pokehunt;

import com.pixelmonmod.api.registry.RegistryValue;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class PokeHuntSavedData extends SavedData {
    private static final String KEY = "guinimospixelmon_hunt";

    private List<String> pokemonNames = new ArrayList<>();

    public static PokeHuntSavedData create() {
        return new PokeHuntSavedData();
    }

    public static PokeHuntSavedData load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        PokeHuntSavedData data = create();
        ListTag list = tag.getList("pokemon", Tag.TAG_STRING);
        for (Tag t : list) {
            data.pokemonNames.add(t.getAsString());
        }
        return data;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        ListTag list = new ListTag();
        for (String name : pokemonNames) {
            list.add(StringTag.valueOf(name));
        }
        tag.put("pokemon", list);
        return tag;
    }

    public static PokeHuntSavedData get(MinecraftServer server) {
        DimensionDataStorage storage = server.overworld().getDataStorage();
        return storage.computeIfAbsent(
                new SavedData.Factory<>(PokeHuntSavedData::create, PokeHuntSavedData::load),
                KEY
        );
    }

    public List<RegistryValue<Species>> getCurrentHunt() {
        List<RegistryValue<Species>> result = new ArrayList<>(pokemonNames.size());
        for (String name : pokemonNames) {
            RegistryValue<Species> species = PixelmonSpecies.fromName(name);
            if (species != null && species.get() != null) {
                result.add(species);
            }
        }
        return result;
    }

    public void setHunt(List<RegistryValue<Species>> hunt) {
        this.pokemonNames = new ArrayList<>();
        for (RegistryValue<Species> rv : hunt) {
            this.pokemonNames.add(rv.get().getName()); // confira o nome exato desse getter
        }
        this.setDirty();
    }

    public OptionalInt onCaptured(Species captured) {
        int index = pokemonNames.indexOf(captured.getName());
        if (index == -1) return OptionalInt.empty();

        Integer difficulty = PokeHunt.INSTANCE.getDifficultyOf(captured);
        if (difficulty == null) return OptionalInt.empty();

        RegistryValue<Species> replacement = PokeHunt.INSTANCE.pickRandomExcluding(difficulty, pokemonNames);
        if (replacement != null) {
            pokemonNames.set(index, replacement.get().getName());
        } else {
            pokemonNames.remove(index);
        }

        setDirty();
        return OptionalInt.of(difficulty);
    }
}
