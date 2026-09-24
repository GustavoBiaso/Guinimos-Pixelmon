package com.guinimos.guinimospixelmon.events;

import com.guinimos.guinimospixelmon.item.custom.LureCrafterItem;
import com.guinimos.guinimospixelmon.menuscreens.pokehunter.PokeHunterMenu;
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
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;


public class EventHandler {
    private static final long RESET_INTERVAL_MS = 30L * 60L * 1000L;

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        PokeHunt.INSTANCE.load();
        PokeHuntSavedData data = PokeHuntSavedData.get(event.getServer());
        Recycler.INSTANCE.load();

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

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        if (server.getTickCount() % 20 != 0) return;

        PokeHuntSavedData data = PokeHuntSavedData.get(event.getServer());
        long now = System.currentTimeMillis();

        if (data.getLastRoll() == 0L) {
            data.setLastRoll(now);
            return;
        }
        if (now - data.getLastRoll() < RESET_INTERVAL_MS) return;

        PokeHunt.INSTANCE.rollHuntList();
        data.setLastRoll(now);

        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            if (p.containerMenu instanceof PokeHunterMenu){
                p.closeContainer();
                server.getPlayerList().broadcastSystemMessage(Component.literal("A lista do Poke Hunter foi renovada!"), false);
            }
        }
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
