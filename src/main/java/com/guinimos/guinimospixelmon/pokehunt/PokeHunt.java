package com.guinimos.guinimospixelmon.pokehunt;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.pixelmonmod.api.registry.RegistryValue;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class PokeHunt {

    private static final int RARITIES = 5;
    private static final int PER_RARITY = 5;

    public static final PokeHunt INSTANCE = new PokeHunt();

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();

    private static final String DEFAULT_RESOURCE_PATH = "/guinimospixelmon/rarity.json";

    private final Map<Integer, List<RegistryValue<Species>>> pokemonByDifficulty = new HashMap<>();

    private PokeHunt() {}

    public void load() {
        Path file = FMLPaths.CONFIGDIR.get().resolve("guinimospixelmon").resolve("rarity.json");
        try {
            if (Files.notExists(file)) {
                Files.createDirectories(file.getParent());
                copyDefaultTo(file);
            }

            pokemonByDifficulty.clear();
            int total = 0;
            int skipped = 0;

            try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();
                for (JsonElement el : array) {
                    JsonObject obj = el.getAsJsonObject();
                    String name = obj.get("name").getAsString();
                    int difficulty = obj.get("difficulty").getAsInt();

                    RegistryValue<Species> species = PixelmonSpecies.fromName(name);
                    if (species == null || species.get() == null) {
                        LOGGER.warn("Poke Hunt: espécie '{}' não encontrada, ignorando", name);
                        skipped++;
                        continue;
                    }
                    if (difficulty < 1 || difficulty > RARITIES) {
                        LOGGER.warn("Poke Hunt: dificuldade inválida ({}) para '{}', ignorando", difficulty, name);
                        skipped++;
                        continue;
                    }

                    pokemonByDifficulty.computeIfAbsent(difficulty, d -> new ArrayList<>()).add(species);
                    total++;
                }
            }

            LOGGER.info("Poke Hunt: {} pokémons carregados de {} ({} ignorados)", total, file, skipped);
        } catch (IOException | RuntimeException e) {
            LOGGER.error("Poke Hunt: falha ao ler {}", file, e);
        }
    }

    private void copyDefaultTo(Path target) throws IOException {
        try (InputStream in = PokeHunt.class.getResourceAsStream(DEFAULT_RESOURCE_PATH)) {
            if (in == null) {
                throw new IOException("Recurso padrão " + DEFAULT_RESOURCE_PATH + " não encontrado no jar");
            }
            Files.copy(in, target);
        }
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