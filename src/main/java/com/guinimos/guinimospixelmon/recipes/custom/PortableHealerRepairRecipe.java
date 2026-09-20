package com.guinimos.guinimospixelmon.recipes.custom;

import com.guinimos.guinimospixelmon.item.ModItems;
import com.guinimos.guinimospixelmon.recipes.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class PortableHealerRepairRecipe extends CustomRecipe {
    private static final int REPAIR_PER_DIAMOND = 3;

    public PortableHealerRepairRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        ItemStack tool = ItemStack.EMPTY;
        int diamonds = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(ModItems.PORTABLE_HEALER.get()) && tool.isEmpty()) {
                tool = stack;
            } else if (stack.is(Items.DIAMOND)) {
                diamonds++;
            } else {
                return false;
            }
        }

        if (tool.isEmpty() || diamonds == 0 || tool.getDamageValue() == 0) return false;

        return diamonds > 0;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack result = ItemStack.EMPTY;
        int diamonds = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(ModItems.PORTABLE_HEALER.get())) {
                result = stack.copy();
            } else if (stack.is(Items.DIAMOND)) {
                diamonds++;
            }
        }

        result.setDamageValue(Math.max(0, result.getDamageValue() - diamonds * REPAIR_PER_DIAMOND));
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ModItems.PORTABLE_HEALER.toStack();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.PORTABLE_HEALER_REPAIR.get();
    }
}
