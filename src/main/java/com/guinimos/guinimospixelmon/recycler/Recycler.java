package com.guinimos.guinimospixelmon.recycler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;

import java.util.*;

public class Recycler extends SimpleJsonResourceReloadListener {
    public Recycler() {
        super(new Gson(), "shop_list");
    }

    private static final Logger LOGGER = LogUtils.getLogger();
    private static volatile Map<Item, ShopEntry> entries = Map.of();

    public static List<ShopEntry> entries() {
        return List.copyOf(entries.values());
    }

    /** Busca pelo item (usada para validar transações no servidor). */
    public static Optional<ShopEntry> find(Item item) {
        return Optional.ofNullable(entries.get(item));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> files, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<Item, ShopEntry> loaded = new LinkedHashMap<>();

        // TreeMap dá uma ordem estável entre arquivos
        for (Map.Entry<ResourceLocation, JsonElement> file : new TreeMap<>(files).entrySet()) {
            ResourceLocation id = file.getKey();
            JsonElement root = file.getValue();

            if (!root.isJsonObject()) {
                LOGGER.error("[Vending] {} ignorado: o arquivo precisa ser um objeto JSON", id);
                continue;
            }
            JsonElement itemsJson = root.getAsJsonObject().get("items");
            if (itemsJson == null || !itemsJson.isJsonArray()) {
                LOGGER.error("[Vending] {} ignorado: campo 'items' ausente ou não é uma lista", id);
                continue;
            }

            // Cada entrada é lida sozinha: uma linha errada não derruba as outras
            for (JsonElement element : itemsJson.getAsJsonArray()) {
                DataResult<ShopEntry> result = ShopEntry.CODEC.parse(JsonOps.INSTANCE, element);
                result.error().ifPresentOrElse(
                        error -> LOGGER.error("[Vending] Entrada ignorada em {}: {}", id, error.message()),
                        () -> result.result().ifPresent(entry -> loaded.put(entry.item(), entry)));
            }
        }

        entries = Collections.unmodifiableMap(loaded);
        LOGGER.info("[Vending] {} itens carregados na máquina de vendas", loaded.size());
    }
}
