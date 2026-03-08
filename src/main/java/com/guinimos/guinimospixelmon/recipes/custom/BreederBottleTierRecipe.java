package com.guinimos.guinimospixelmon.recipes.custom;

import com.guinimos.guinimospixelmon.component.ModDataComponents;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import static com.guinimos.guinimospixelmon.recipes.ModRecipes.BREEDER_BOTTLE_SERIALIZER;

public record BreederBottleTierRecipe(Ingredient inputBottle, Ingredient inputItem, ItemStack output) implements CraftingRecipe {

    @Override
    public NonNullList<Ingredient> getIngredients(){
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(inputItem);
        list.add(inputBottle);
        return list;
    }

    //Crafting recipes and results
    @Override
    public boolean matches(CraftingInput input, Level level) {
        boolean hasTime = false;
        boolean hasIngot = false;

        ItemStack ingot = ItemStack.EMPTY;
        ItemStack bottle = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);

            if (inputBottle.test(stack))
                bottle = stack;

            if (inputItem.test(stack)) {
                ingot = stack;
                hasIngot = true;
            }
        }

        if(ingot.is(Items.COPPER_INGOT)) {
            if(bottle.getOrDefault(ModDataComponents.TIME,0) >= 10800) {
                hasTime = true;
            }
        } else if (ingot.is(Items.IRON_INGOT)) {
            if(bottle.getOrDefault(ModDataComponents.TIME,0) >= 27000) {
                hasTime = true;
            }
        } else if (ingot.is(Items.GOLD_INGOT)) {
            if(bottle.getOrDefault(ModDataComponents.TIME,0) >= 108000) {
                hasTime = true;
            }
        }

        return hasTime && hasIngot;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack bottle = ItemStack.EMPTY;
        ItemStack ingot = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);

            if (!stack.isEmpty()) {
                if (inputBottle.test(stack)) {
                    bottle = stack;
                } else if (inputItem.test(stack)) {
                    ingot = stack;
                }
            }
        }

        if(!bottle.isEmpty() && !ingot.isEmpty()){
            if (ingot.is(Items.COPPER_INGOT)) {
                return new ItemStack(
                        BuiltInRegistries.ITEM.get(
                                ResourceLocation.fromNamespaceAndPath("pixelmon", "copper_hourglass")
                        )
                );
            } else if (ingot.is(Items.IRON_INGOT)) {
                return new ItemStack(
                        BuiltInRegistries.ITEM.get(
                                ResourceLocation.fromNamespaceAndPath("pixelmon", "silver_hourglass")
                        )
                );
            } else if (ingot.is(Items.GOLD_INGOT)) {
                return new ItemStack(
                        BuiltInRegistries.ITEM.get(
                                ResourceLocation.fromNamespaceAndPath("pixelmon", "gold_hourglass")
                        )
                );
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        ItemStack bottle = ItemStack.EMPTY;
        ItemStack ingot = ItemStack.EMPTY;
        int temp_pos = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);

            if (!stack.isEmpty()) {
                if (inputBottle.test(stack)) {
                    temp_pos = i;
                    bottle = stack;
                } else if (inputItem.test(stack)) {
                    ingot = stack;
                }
            }
        }

        if(!bottle.isEmpty() && !ingot.isEmpty()){
            ItemStack bottle_copy = bottle.copy();
            if (ingot.is(Items.COPPER_INGOT)) {
                bottle_copy.set(ModDataComponents.TIME, Math.max(bottle.getOrDefault(ModDataComponents.TIME, 0) - 10800, 0)); //108000 = 1:30 hours
                remaining.set(temp_pos, bottle_copy);
            }

            if (ingot.is(Items.IRON_INGOT)) {
                bottle_copy.set(ModDataComponents.TIME, Math.max(bottle.getOrDefault(ModDataComponents.TIME, 0) - 27000, 0)); //108000 = 1:30 hours
                remaining.set(temp_pos, bottle_copy);
            }

            if (ingot.is(Items.GOLD_INGOT)) {
                bottle_copy.set(ModDataComponents.TIME, Math.max(bottle.getOrDefault(ModDataComponents.TIME, 0) - 108000, 0)); //108000 = 1:30 hours
                remaining.set(temp_pos, bottle_copy);
            }
        }

        return remaining;
    }

    //Type and Serializer
    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public RecipeSerializer<? extends Recipe<CraftingInput>> getSerializer() {
        return BREEDER_BOTTLE_SERIALIZER.get();
    }

    //Other stuff
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output.copy();
    }


    public Ingredient getInputItem() {
        return inputItem;
    }

    public Ingredient getInputBottle() {
        return inputBottle;
    }

    public ItemStack getResult() {
        return output;
    }

    public static class BreederBottleRecipeSerializer implements RecipeSerializer<BreederBottleTierRecipe>{
        public static final MapCodec<BreederBottleTierRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("inputBottle").forGetter(BreederBottleTierRecipe::getInputBottle),
                Ingredient.CODEC.fieldOf("inputItem").forGetter(BreederBottleTierRecipe::getInputItem),
                ItemStack.CODEC.optionalFieldOf("result", ItemStack.EMPTY).forGetter(BreederBottleTierRecipe::getResult)
        ).apply(inst, BreederBottleTierRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, BreederBottleTierRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, BreederBottleTierRecipe::getInputBottle,
                        Ingredient.CONTENTS_STREAM_CODEC, BreederBottleTierRecipe::getInputItem,
                        ItemStack.STREAM_CODEC, BreederBottleTierRecipe::getResult,
                        BreederBottleTierRecipe::new
                );

        @Override
        public MapCodec<BreederBottleTierRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BreederBottleTierRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

}
