package com.guinimos.guinimospixelmon.item.custom;

import com.guinimos.guinimospixelmon.menuscreens.pokehunter.PokeHunterMenuProvider;
import com.guinimos.guinimospixelmon.pokehunt.PokeHuntSavedData;
import com.pixelmonmod.api.registry.RegistryValue;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class PokeHunterItem extends Item {
    public PokeHunterItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            List<RegistryValue<Species>> currentHunt = PokeHuntSavedData.get(Objects.requireNonNull(player.getServer())).getCurrentHunt();
            PokeHunterMenuProvider provider = new PokeHunterMenuProvider(currentHunt);

            player.openMenu(provider, buf -> {
                List<ItemStack> items = provider.getMenuItems();
                buf.writeVarInt(items.size());
                for (ItemStack photos : items) {
                    ItemStack.STREAM_CODEC.encode(buf, photos);
                }
            });
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
