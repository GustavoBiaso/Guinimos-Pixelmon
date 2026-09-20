package com.guinimos.guinimospixelmon.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class TempleMapsItem extends Item {

    private final TagKey<Structure> target;
    private final Holder<MapDecorationType> decoration;
    private final String mapNameKey;

    public TempleMapsItem(Properties properties, TagKey<Structure> target, Holder<MapDecorationType> decoration, String mapNameKey) {
        super(properties);
        this.target = target;
        this.decoration = decoration;
        this.mapNameKey = mapNameKey;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResultHolder.success(stack);
        }

        BlockPos pos = serverLevel.findNearestMapStructure(target, player.blockPosition(), 1000, false);
        if (pos == null) {
            player.displayClientMessage(
                    Component.translatable("A estrutura não foi encontrada, ela também pode estar em outra dimensão!"), true);
            return InteractionResultHolder.fail(stack);
        }

        ItemStack map = MapItem.create(serverLevel, pos.getX(), pos.getZ(), (byte) 2, true, true);
        MapItem.renderBiomePreviewMap(serverLevel, map);
        MapItemSavedData.addTargetDecoration(map, pos, "+", decoration);
        map.set(DataComponents.ITEM_NAME, Component.translatable(mapNameKey));

        return InteractionResultHolder.success(ItemUtils.createFilledResult(stack, player, map));
    }
}