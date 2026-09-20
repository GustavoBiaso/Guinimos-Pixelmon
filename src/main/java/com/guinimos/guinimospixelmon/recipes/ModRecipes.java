package com.guinimos.guinimospixelmon.recipes;
import com.guinimos.guinimospixelmon.GuinimosPixelmon;
import com.guinimos.guinimospixelmon.recipes.custom.BreederBottleTierRecipe;
import com.guinimos.guinimospixelmon.recipes.custom.PortableHealerRepairRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, GuinimosPixelmon.MODID);

    public static final Supplier<RecipeSerializer<BreederBottleTierRecipe>> BREEDER_BOTTLE_SERIALIZER =
            RECIPE_SERIALIZERS.register("breeder_bottle_tier", BreederBottleTierRecipe.BreederBottleRecipeSerializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PortableHealerRepairRecipe>> PORTABLE_HEALER_REPAIR =
            RECIPE_SERIALIZERS.register("portable_healer_repair", () -> new SimpleCraftingRecipeSerializer<>(PortableHealerRepairRecipe::new));

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
