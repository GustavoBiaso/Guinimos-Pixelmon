package com.guinimos.guinimospixelmon.Events;

import com.guinimos.guinimospixelmon.item.custom.LureCrafterItem;
import com.pixelmonmod.pixelmon.api.events.lures.LureExpiredEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.items.LureItem;
import com.pixelmonmod.pixelmon.items.heldItems.BerryItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

    @SubscribeEvent
    public void onLureExpired(LureExpiredEvent expiredEvent){
        LureItem newLure = expiredEvent.lure;
        if(isLureOnThenRemoveBerries(expiredEvent.player)) {
            expiredEvent.setCanceled(true);
            expiredEvent.party.setLure(newLure);
            expiredEvent.player.sendSystemMessage(Component.literal("Your lure was re-crafted!"));
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
