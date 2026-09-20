package com.guinimos.guinimospixelmon.menuscreens.pokehunter;

import com.pixelmonmod.api.registry.RegistryValue;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.guinimos.guinimospixelmon.pokehunt.PokeHunt.toPhotoItemStacks;

public class PokeHunterMenuProvider implements MenuProvider {
    private final List<ItemStack> menuItems;

    public PokeHunterMenuProvider(List<RegistryValue<Species>> currentHunt) {
        this.menuItems = toPhotoItemStacks(currentHunt);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Poke Hunter");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new PokeHunterMenu(containerId, menuItems);
    }

    public List<ItemStack> getMenuItems() {
        return menuItems;
    }
}
