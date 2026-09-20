package com.guinimos.guinimospixelmon.menuscreens.pokehunter;

import com.guinimos.guinimospixelmon.menuscreens.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class PokeHunterMenu extends AbstractContainerMenu {
    private final List<ItemStack> items;

    public PokeHunterMenu(int containerId, Inventory inv, FriendlyByteBuf buf) {
        this(containerId, readItems(buf));
    }

    public PokeHunterMenu(int containerId, List<ItemStack> items) {
        super(ModMenus.POKEHUNTER_MENU.get(), containerId);
        this.items = items;
    }

    public List<ItemStack> getMenuItems() {
        return items;
    }

    private static List<ItemStack> readItems(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<ItemStack> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(ItemStack.STREAM_CODEC.decode((RegistryFriendlyByteBuf) buf));
        }
        return list;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
