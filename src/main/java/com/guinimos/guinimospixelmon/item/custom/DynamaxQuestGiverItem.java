package com.guinimos.guinimospixelmon.item.custom;

import com.pixelmonmod.pixelmon.api.research.Research;
import com.pixelmonmod.pixelmon.api.storage.research.ResearchStorage;
import com.pixelmonmod.pixelmon.api.storage.research.ResearchStorageProxy;
import com.pixelmonmod.pixelmon.init.registry.PixelmonRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DynamaxQuestGiverItem extends Item {
    public DynamaxQuestGiverItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                    "pixelmon",
                    "utility/dynamax_band"
            );

            Research research = player.getServer()
                    .registryAccess()
                    .registryOrThrow(PixelmonRegistry.RESEARCH_REGISTRY)
                    .get(id);


            ResearchStorage researchStorage = ResearchStorageProxy.getStorageNow((ServerPlayer) player);
            if (researchStorage != null) {
                researchStorage.startResearch(research);
                stack.consume(1, null);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
