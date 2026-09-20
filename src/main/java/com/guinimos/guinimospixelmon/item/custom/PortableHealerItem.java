package com.guinimos.guinimospixelmon.item.custom;

import com.guinimos.guinimospixelmon.client.ClientHooks;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class PortableHealerItem extends Item {
    public PortableHealerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(isUsable(player.getItemInHand(hand))){
            if (level.isClientSide()) {
                ClientHooks.openPortableHealerScreen();
            }
            player.getItemInHand(hand).hurtAndBreak(1, player, player.getItemInHand(hand).getEquipmentSlot());
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    public static boolean isUsable(ItemStack stack) {
        return stack.getDamageValue() < stack.getMaxDamage() - 1;
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        int room = stack.getMaxDamage() - 1 - stack.getDamageValue();
        return Math.max(0, Math.min(amount, room));
    }

    public static void HealParty(Player player) {
        PlayerPartyStorage party = StorageProxy.getPartyNow(player);

        player.sendSystemMessage(Component.nullToEmpty("Seus pokémons foram curados!"));

        if (party != null) {
            party.heal();
        }
    }

    public static void OpenPC(Player player) {
        PCStorage pc = StorageProxy.getPCForPlayerNow(player);

        player.sendSystemMessage(Component.nullToEmpty("PC aberto!"));

        if (pc != null) {
            pc.open((ServerPlayer) player);
        }
    }
}
