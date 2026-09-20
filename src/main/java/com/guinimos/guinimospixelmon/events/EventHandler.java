package com.guinimos.guinimospixelmon.events;

import com.guinimos.guinimospixelmon.item.custom.LureCrafterItem;
import com.guinimos.guinimospixelmon.pokehunt.PokeHunt;
import com.guinimos.guinimospixelmon.pokehunt.PokeHuntSavedData;
import com.guinimos.guinimospixelmon.pokehunt.PokeHunterRewards;
import com.guinimos.guinimospixelmon.recycler.Recycler;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.lures.LureExpiredEvent;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.items.LureItem;
import com.pixelmonmod.pixelmon.items.heldItems.BerryItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;


public class EventHandler {
    @SubscribeEvent
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(PokeHunt.INSTANCE);
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        PokeHuntSavedData data = PokeHuntSavedData.get(event.getServer());

        if (data.getCurrentHunt().isEmpty()) {
            data.setHunt(PokeHunt.INSTANCE.rollHuntList());
        }
    }

    @SubscribeEvent
    public void onPokemonSpawn(EntityJoinLevelEvent event){
        if(event.getLevel().isClientSide()) return;
        if(event.getEntity() instanceof PixelmonEntity pokemon){
            if(pokemon.isBossPokemon()) {
                Player nearest = pokemon.level().getNearestPlayer(pokemon, 200.0D);
                assert nearest != null;
                nearest.sendSystemMessage(Component.literal("A Boss Pokemon spawned next to you!").withStyle(ChatFormatting.RED));
            }
        }
    }

    @SubscribeEvent
    public void onLureExpired(LureExpiredEvent expiredEvent){
        LureItem newLure = expiredEvent.lure;
        if(isLureOnThenRemoveBerries(expiredEvent.player)) {
            expiredEvent.setCanceled(true);
            expiredEvent.party.setLure(newLure);
            expiredEvent.player.sendSystemMessage(Component.literal("Your lure was re-crafted!"));
        }
    }

    @SubscribeEvent
    public void onPokemonCaptured(CaptureEvent.SuccessfulCapture event){
        ServerPlayer player = event.getPlayer();
        Species species = event.getPokemon().getSpecies();

        PokeHuntSavedData data = PokeHuntSavedData.get(player.getServer());

        data.onCaptured(species).ifPresent(difficulty -> PokeHunterRewards.give(player, difficulty));
    }

    @SubscribeEvent
    public void onRaidPokemonCaptured(CaptureEvent.SuccessfulRaidCapture event){
        ServerPlayer player = event.getPlayer();
        Species species = event.getPokemon().getSpecies();

        PokeHuntSavedData data = PokeHuntSavedData.get(player.getServer());

        data.onCaptured(species).ifPresent(difficulty -> PokeHunterRewards.give(player, difficulty));
    }

    private static Boolean isLureOnThenRemoveBerries(Player player) {
        int remaining = 4;
        for (ItemStack stack : player.getInventory().items) {
            if(stack.getItem() instanceof LureCrafterItem){
                if(LureCrafterItem.isActive(stack)){
                    for (ItemStack berries : player.getInventory().items) {
                        if(berries.getItem() instanceof BerryItem){
                            int remove = Math.min(berries.getCount(), remaining);
                            berries.shrink(remove);
                            remaining -= remove;
                            if (remaining <= 0) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return false;
    }
}
