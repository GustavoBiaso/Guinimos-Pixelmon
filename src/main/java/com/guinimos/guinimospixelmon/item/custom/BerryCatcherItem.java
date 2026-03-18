package com.guinimos.guinimospixelmon.item.custom;

import com.pixelmonmod.pixelmon.blocks.BerryLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BerryCatcherItem extends Item {
    private static final int range = 5;

    public BerryCatcherItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn (UseOnContext context){
        Level level = context.getLevel();
        BlockPos clickedBlock = context.getClickedPos();

        if(!level.isClientSide()){
            if(level.getBlockState(clickedBlock).getBlock() instanceof BerryLeavesBlock) {
                for (BlockPos pos : BlockPos.betweenClosed(clickedBlock.offset(-range, -range, -range), clickedBlock.offset(range, range, range))) {
                    BlockState state = level.getBlockState(pos);
                    if (state.getBlock() instanceof BerryLeavesBlock) {
                        state.useWithoutItem(level, context.getPlayer(), new BlockHitResult(Vec3.atCenterOf(pos), Direction.DOWN, pos, false));
                        AABB box = new AABB(pos).inflate(1);
                        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, box);
                        for (ItemEntity item : items) {
                            ItemStack stack = item.getItem();
                            if (context.getPlayer().getInventory().add(stack)) {
                                item.discard();
                            }
                        }
                    }
                }
                context.getItemInHand().hurtAndBreak(1, context.getPlayer(), EquipmentSlot.MAINHAND);
                level.playSound(null, clickedBlock, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

        return InteractionResult.PASS;
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        tooltipComponents.add(Component.literal("Picks all items around the berries leaves!"));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

}
