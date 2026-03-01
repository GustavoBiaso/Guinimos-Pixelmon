package com.guinimos.guinimospixelmon.recipes.custom;

import com.guinimos.guinimospixelmon.GuinimosPixelmon;
import com.guinimos.guinimospixelmon.component.ModDataComponents;
import com.guinimos.guinimospixelmon.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.guinimos.guinimospixelmon.recipes.BreederBottleRecipeSerializer.BREEDER_BOTTLE_SERIALIZER;

public class BreederBottleIronRecipe implements Recipe<BreederBottleIronInput> {
    private final Ingredient inputBottle;
    private final Ingredient inputItem;
    private final ItemStack result;

    public BreederBottleIronRecipe(Ingredient inputBottle, Ingredient inputItem, ItemStack result) {
        this.inputBottle = inputBottle;
        this.inputItem = inputItem;
        this.result = result;
    }

    //Crafting recipes and results
    @Override
    public boolean matches(BreederBottleIronInput input, Level level) {
        boolean hasTime = false;
        boolean hasIron = false;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);

            if (stack.is(ModItems.BREEDER_BOTTLE.get())){
                if(stack.get(ModDataComponents.TIME) >= 108000) {
                    hasTime = true;
                }
            }

            if (stack.is(Items.IRON_INGOT))
                hasIron = true;
        }

        return hasTime && hasIron;
    }

    @Override
    public ItemStack assemble(BreederBottleIronInput input, HolderLookup.Provider registries) {
        return new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("pixelmon", "silver_hourglass"))); //copper_hourglass & gold_hourglass
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(BreederBottleIronInput input) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(ModItems.BREEDER_BOTTLE.get())) {
                ItemStack bottle = stack.copy();
                bottle.set(ModDataComponents.TIME, stack.get(ModDataComponents.TIME) - 108000); //108000 = 1:30 hours
                remaining.set(i, bottle);
            }
        }
        return remaining;
    }

    //Defining Type
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, GuinimosPixelmon.MODID);

    public static final Supplier<RecipeType<BreederBottleIronRecipe>> BREEDER_BOTTLE_TYPE =
            RECIPE_TYPES.register(
                    "breeder_bottle",
                    // Creates the recipe type, setting `toString` to the registry name of the type
                    RecipeType::simple
            );

    @Override
    public RecipeType<? extends Recipe<BreederBottleIronInput>> getType() {
        return BREEDER_BOTTLE_TYPE.get();
    }

    //Other stuff
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return null;
    }

    @Override
    public RecipeSerializer<? extends Recipe<BreederBottleIronInput>> getSerializer() {
        return BREEDER_BOTTLE_SERIALIZER.get();
    }

    public Ingredient getInputItem() {
        return inputItem;
    }

    public Ingredient getInputBottle() {
        return inputBottle;
    }

    public ItemStack getResult() {
        return result;
    }
}
