package com.guinimos.guinimospixelmon.item.custom;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class BerryCatcherItem extends Item {
    private static final int range = 10;

    public BerryCatcherItem(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn (UseOnContext context){
        Level level = context.getLevel();
        Block clickedBlock = level.getBlockState(context.getClickedPos()).getBlock();

        /*if(!level.isClientSide()){
            if(clickedBlock instanceof BerryLeavesBlock berryLeavesBlock){
                berryLeavesBlock.
            }

            for (int i = 0; i < range; i++) {
                for (int j = 0; j < range; j++) {
                    for (int k = 0; k < range; k++) {
                        if();
                    }
                }
            }
        }*/

        return super.useOn(context);
    }
}
