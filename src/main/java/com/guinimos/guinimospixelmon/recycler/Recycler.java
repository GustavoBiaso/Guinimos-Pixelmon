package com.guinimos.guinimospixelmon.recycler;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class Recycler {

    public static final Recycler INSTANCE = new Recycler();

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String DEFAULT_RESOURCE_PATH = "/guinimospixelmon/shoplist.json";

    public record Entry(Item item, int buyPrice) {
        public int sellPrice() {
            return buyPrice / 2;
        }
    }

    private volatile List<Entry> entries = List.of();

    private Recycler() {}

    public List<Entry> entries() {
        return entries;
    }

    public Optional<Entry> find(Item item) {
        for (Entry e : entries) {
            if (e.item() == item) return Optional.of(e);
        }
        return Optional.empty();
    }

    public void load() {
        Path file = FMLPaths.CONFIGDIR.get().resolve("guinimospixelmon").resolve("shoplist.json");
        try {
            if (Files.notExists(file)) {
                Files.createDirectories(file.getParent());
                copyDefaultTo(file);
            }

            List<Entry> loaded = new ArrayList<>();
            try (Reader reader = Files.newBufferedReader(file)) {
                JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();
                for (JsonElement el : array) {
                    parseEntry(el).ifPresent(loaded::add);
                }
            }
            this.entries = List.copyOf(loaded);
            LOGGER.info("Vending machine: {} itens carregados de {}", loaded.size(), file);
        } catch (IOException | RuntimeException e) {
            LOGGER.error("Vending machine: falha ao ler {}", file, e);
        }
    }

    private void copyDefaultTo(Path target) throws IOException {
        try (InputStream in = Recycler.class.getResourceAsStream(DEFAULT_RESOURCE_PATH)) {
            if (in == null) {
                throw new IOException("Recurso padrão " + DEFAULT_RESOURCE_PATH + " não encontrado no jar");
            }
            Files.copy(in, target);
        }
    }


    private Optional<Entry> parseEntry(JsonElement el) {
        try {
            JsonObject obj = el.getAsJsonObject();
            String id = obj.get("item").getAsString();
            int price = obj.get("price").getAsInt();

            ResourceLocation rl = ResourceLocation.tryParse(id);
            if (rl == null) {
                LOGGER.warn("Vending machine: id inválido '{}'", id);
                return Optional.empty();
            }
            Optional<Item> item = BuiltInRegistries.ITEM.getOptional(rl);
            if (item.isEmpty() || item.get() == net.minecraft.world.item.Items.AIR) {
                LOGGER.warn("Vending machine: item '{}' não existe, ignorando", id);
                return Optional.empty();
            }
            if (price <= 0) {
                LOGGER.warn("Vending machine: preço inválido ({}) para '{}', ignorando", price, id);
                return Optional.empty();
            }
            return Optional.of(new Entry(item.get(), price));
        } catch (RuntimeException e) {
            LOGGER.warn("Vending machine: entrada malformada {}", el, e);
            return Optional.empty();
        }
    }
}
