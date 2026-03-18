package com.guinimos.guinimospixelmon.item.custom;

import com.guinimos.guinimospixelmon.component.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class LureCrafterItem extends Item {
    public LureCrafterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            boolean isActive = stack.get(ModDataComponents.ACTIVE.get()) != null ? stack.get(ModDataComponents.ACTIVE.get()) : false;

            stack.set(ModDataComponents.ACTIVE.get(), !isActive);

            player.sendSystemMessage(Component.literal((!isActive ? "On" : "Off")));
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal(isActive(stack) ? "On" : "Off"));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.ACTIVE.get(), false);
    }

    public static boolean isActive(ItemStack stack) {
        Boolean value = stack.get(ModDataComponents.ACTIVE.get());
        return value != null && value;
    }
}
