package com.guinimos.guinimospixelmon.recipes.custom;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record BreederBottleIronInput(ItemStack bottle, ItemStack iron) implements RecipeInput {
    @Override
    public ItemStack getItem(int slot) {
        if(slot == 1){
            return this.bottle;
        }
        return this.iron;
    }

    @Override
    public int size() {
        return 2;
    }
}
