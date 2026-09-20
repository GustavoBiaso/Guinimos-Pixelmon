package com.guinimos.guinimospixelmon.pokehunt;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pixelmonmod.api.registry.RegistryValue;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.*;

public class PokeHunt extends SimpleJsonResourceReloadListener {

    private static final int RARITIES = 5;
    private static final int PER_RARITY = 5;

    public static final PokeHunt INSTANCE = new PokeHunt();

    private PokeHunt() {
        super(new Gson(), "hunt_rarity");
    }

    private final Map<Integer, List<RegistryValue<Species>>> pokemonByDifficulty = new HashMap<>();

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        pokemonByDifficulty.clear();

        for (Map.Entry<ResourceLocation, JsonElement> fileEntry : object.entrySet()) {

            JsonArray array = fileEntry.getValue().getAsJsonArray();
            for (JsonElement entry : array) {
                JsonObject obj = entry.getAsJsonObject();

                String name = obj.get("name").getAsString();
                int difficulty = obj.get("difficulty").getAsInt();

                RegistryValue<Species> species = PixelmonSpecies.fromName(name);
                if (species != null && species.get() != null) {
                    pokemonByDifficulty.computeIfAbsent(difficulty, d -> new ArrayList<>()).add(species);
                }
            }
        }

        int total = pokemonByDifficulty.values().stream().mapToInt(List::size).sum();
    }

    public List<RegistryValue<Species>> rollHuntList() {
        List<RegistryValue<Species>> result = new ArrayList<>(RARITIES * PER_RARITY);
        RandomSource random = RandomSource.create();

        for (int difficulty = 1; difficulty <= RARITIES; difficulty++) {
            List<RegistryValue<Species>> pool = pokemonByDifficulty.get(difficulty);

            if (pool == null || pool.isEmpty()) {
                continue;
            }

            List<RegistryValue<Species>> shuffled = new ArrayList<>(pool);
            Collections.shuffle(shuffled, new Random(random.nextLong()));

            int amount = Math.min(PER_RARITY, shuffled.size());
            result.addAll(shuffled.subList(0, amount));
        }

        return result;
    }

    public static List<ItemStack> toPhotoItemStacks(List<RegistryValue<Species>> speciesList) {
        List<ItemStack> result = new ArrayList<>(speciesList.size());

        Item photoItem = BuiltInRegistries.ITEM.get(ResourceLocation.parse("pixelmon:pixelmon_sprite"));

        for (RegistryValue<Species> registryValue : speciesList) {
            Species species = registryValue.get();

            ItemStack photo = new ItemStack(photoItem);

            CompoundTag tag = new CompoundTag();
            tag.putShort("ndex", (short) species.getDex());
            tag.putString("form", "base");
            tag.putByte("gender", (byte) 0);
            tag.putString("palette", "none");

            photo.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            result.add(photo);
        }

        return result;
    }

    public Integer getDifficultyOf(Species species) {
        for (Map.Entry<Integer, List<RegistryValue<Species>>> entry : pokemonByDifficulty.entrySet()) {
            for (RegistryValue<Species> rv : entry.getValue()) {
                Species candidate = rv.get();
                if (candidate != null && candidate.equals(species)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public RegistryValue<Species> pickRandomExcluding(int difficulty, List<String> excludeNames) {
        List<RegistryValue<Species>> pool = pokemonByDifficulty.get(difficulty);
        if (pool == null) return null;

        List<RegistryValue<Species>> candidates = new ArrayList<>();
        for (RegistryValue<Species> rv : pool) {
            Species candidate = rv.get();
            if (candidate != null && !excludeNames.contains(candidate.getName())) {
                candidates.add(rv);
            }
        }
        if (candidates.isEmpty()) return null;

        Collections.shuffle(candidates);
        return candidates.get(0);
    }
}
