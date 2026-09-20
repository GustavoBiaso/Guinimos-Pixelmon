package com.guinimos.guinimospixelmon.pokehunt;

import com.pixelmonmod.pixelmon.api.pokemon.item.pokeball.PokeBall;
import com.pixelmonmod.pixelmon.api.pokemon.item.pokeball.PokeBallRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PokeHunterRewards {
    private PokeHunterRewards() {}

    private static List<ItemStack> rewardsFor(int difficulty) {
        return switch (difficulty) {
            case 1 -> List.of(pokeballItem("ultra_ball", 4), pixelmonItem("rare_candy", 1), pixelmonItem("nugget", 3));
            case 2 -> List.of(pokeballItem("ultra_ball", 8), pixelmonItem("quick_ball", 4), pixelmonItem("rare_candy", 3), pixelmonItem("pearl", 3));
            case 3 -> List.of(pokeballItem("ultra_ball", 32), pokeballItem("quick_ball", 16), pixelmonItem("rare_candy", 5), pixelmonItem("pearl_string", 6));
            case 4 -> List.of(pokeballItem("ultra_ball", 64), pokeballItem("quick_ball", 64), pixelmonItem("rare_candy", 10), pixelmonItem("big_nugget", 10));
            case 5 -> List.of(pokeballItem("ultra_ball", 64), pokeballItem("master_ball", 1), pixelmonItem("rare_candy", 20), pixelmonItem("big_nugget", 40));
            default -> List.of();
        };
    }

    public static void give(ServerPlayer player, int difficulty) {
        for (ItemStack reward : rewardsFor(difficulty)) {
            if (reward.isEmpty()) continue;

            if (!player.getInventory().add(reward)) {
                player.drop(reward, false);
            }
        }
    }

    private static ItemStack pixelmonItem(String id, int count) {
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse("pixelmon:" + id));
        return new ItemStack(item, count);
    }

    private static ItemStack pokeballItem(String id, int count) {
        PokeBall pokeBall = PokeBallRegistry.getPokeBall(id).get();

        return pokeBall.getBallItem(count);
    }
}
