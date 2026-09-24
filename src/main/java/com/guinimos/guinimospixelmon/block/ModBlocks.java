package com.guinimos.guinimospixelmon.block;

import com.guinimos.guinimospixelmon.GuinimosPixelmon;
import com.guinimos.guinimospixelmon.block.custom.RecyclerBlock;
import com.guinimos.guinimospixelmon.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.guinimos.guinimospixelmon.item.ModItems.ITEMS;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(GuinimosPixelmon.MODID);

    public static final DeferredBlock<RecyclerBlock> RECYCLER = BLOCKS.registerBlock("recycler", RecyclerBlock::new, BlockBehaviour.Properties.of().strength(3.0F).requiresCorrectToolForDrops().sound(SoundType.METAL));
    public static final DeferredItem<BlockItem> RECYCLER_ITEM = ITEMS.registerSimpleBlockItem(RECYCLER);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
