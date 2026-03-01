package com.guinimos.guinimospixelmon.recipes;

import com.guinimos.guinimospixelmon.GuinimosPixelmon;
import com.guinimos.guinimospixelmon.recipes.custom.BreederBottleIronRecipe;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BreederBottleRecipeSerializer implements RecipeSerializer<BreederBottleIronRecipe>{
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, GuinimosPixelmon.MODID);

    public static final Supplier<RecipeSerializer<BreederBottleIronRecipe>> BREEDER_BOTTLE_SERIALIZER =
            RECIPE_SERIALIZERS.register("breeder_bottle_serializer", BreederBottleRecipeSerializer::new);

    public static final MapCodec<BreederBottleIronRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("inputBottle").forGetter(BreederBottleIronRecipe::getInputBottle),
            Ingredient.CODEC.fieldOf("inputItem").forGetter(BreederBottleIronRecipe::getInputItem),
            ItemStack.CODEC.fieldOf("result").forGetter(BreederBottleIronRecipe::getResult)
    ).apply(inst, BreederBottleIronRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BreederBottleIronRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, BreederBottleIronRecipe::getInputBottle,
                    Ingredient.CONTENTS_STREAM_CODEC, BreederBottleIronRecipe::getInputItem,
                    ItemStack.STREAM_CODEC, BreederBottleIronRecipe::getResult,
                    BreederBottleIronRecipe::new
            );

    @Override
    public MapCodec<BreederBottleIronRecipe> codec() {
        return null;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, BreederBottleIronRecipe> streamCodec() {
        return null;
    }
}
