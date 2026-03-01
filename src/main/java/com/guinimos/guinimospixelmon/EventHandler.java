package com.guinimos.guinimospixelmon;

import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public class EventHandler {
    @SubscribeEvent
    public void onPokemonSpawn(EntityJoinLevelEvent event){
        if(event.getLevel().isClientSide()) return;
        if(event.getEntity() instanceof PixelmonEntity pokemon){
            if(pokemon.isBossPokemon()) {
                Player nearest = pokemon.level().getNearestPlayer(pokemon, 200.0D);
                nearest.sendSystemMessage(Component.literal("A Boss Pokemon spawned next to you!").withStyle(ChatFormatting.RED));
            }
        }
    }
}
