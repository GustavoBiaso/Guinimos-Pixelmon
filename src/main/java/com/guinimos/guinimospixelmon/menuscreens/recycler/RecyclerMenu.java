package com.guinimos.guinimospixelmon.menuscreens.recycler;

import com.guinimos.guinimospixelmon.recycler.Recycler;
import com.guinimos.guinimospixelmon.recycler.RecyclerMoney;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class RecyclerMenu extends AbstractContainerMenu {

    private static final int SHOP_SLOTS = 45;
    private static final int SLOT_PREV = 45;
    private static final int SLOT_INFO = 49;
    private static final int SLOT_NEXT = 53;
    private static final int PLAYER_SLOTS_START = 54;

    private static final String CURRENCY = "₽";    // só estético

    private final Container shop = new SimpleContainer(54);
    private final ContainerLevelAccess access;
    private final Block block;

    private final List<Recycler.Entry> entries = Recycler.INSTANCE.entries();
    private int page = 0;

    public RecyclerMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access, Block block) {
        super(MenuType.GENERIC_9x6, containerId);
        this.access = access;
        this.block = block;

        for (int i = 0; i < 54; i++) {
            addSlot(new Slot(shop, i, 0, 0));
        }
        for (int i = 9; i < 36; i++) {
            addSlot(new Slot(playerInventory, i, 0, 0));
        }
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 0, 0));
        }

        refresh();
    }

    private int maxPage() {
        return Math.max(0, (entries.size() - 1) / SHOP_SLOTS);
    }

    private void refresh() {
        shop.clearContent();

        int start = page * SHOP_SLOTS;
        for (int i = 0; i < SHOP_SLOTS; i++) {
            int idx = start + i;
            if (idx >= entries.size()) break;
            shop.setItem(i, display(entries.get(idx)));
        }

        ItemStack filler = new ItemStack(Items.GRAY_STAINED_GLASS_PANE);
        filler.set(DataComponents.CUSTOM_NAME, Component.literal(" "));
        for (int i = SHOP_SLOTS; i < 54; i++) {
            shop.setItem(i, filler.copy());
        }
        if (page > 0) shop.setItem(SLOT_PREV, named(Items.ARROW, "Página anterior"));
        if (page < maxPage()) shop.setItem(SLOT_NEXT, named(Items.ARROW, "Próxima página"));

        ItemStack info = named(Items.PAPER, "Página " + (page + 1) + "/" + (maxPage() + 1));
        info.set(DataComponents.LORE, new ItemLore(List.of(
                line("Clique: 1 unidade", ChatFormatting.GRAY),
                line("Shift+clique: 1 stack", ChatFormatting.GRAY),
                line("Clique num item do seu inventário para vender", ChatFormatting.YELLOW)
        )));
        shop.setItem(SLOT_INFO, info);
    }

    private static ItemStack display(Recycler.Entry entry) {
        ItemStack stack = new ItemStack(entry.item());
        List<Component> lore = new java.util.ArrayList<>();
        lore.add(line("Comprar: " + entry.buyPrice() + " " + CURRENCY, ChatFormatting.GREEN));
        if (entry.sellPrice() > 0) {
            lore.add(line("Vender: " + entry.sellPrice() + " " + CURRENCY, ChatFormatting.GOLD));
        } else {
            lore.add(line("Não compramos este item", ChatFormatting.RED));
        }
        stack.set(DataComponents.LORE, new ItemLore(lore));
        return stack;
    }

    private static ItemStack named(net.minecraft.world.item.Item item, String name) {
        ItemStack stack = new ItemStack(item);
        stack.set(DataComponents.CUSTOM_NAME, Component.literal(name).withStyle(s -> s.withItalic(false).withColor(ChatFormatting.WHITE)));
        return stack;
    }

    private static Component line(String text, ChatFormatting color) {
        return Component.literal(text).withStyle(color).withStyle(s -> s.withItalic(false));
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (player instanceof ServerPlayer sp
                && (clickType == ClickType.PICKUP || clickType == ClickType.QUICK_MOVE)
                && slotId >= 0 && slotId < slots.size()) {

            boolean shift = clickType == ClickType.QUICK_MOVE;

            if (slotId < SHOP_SLOTS) {
                int idx = page * SHOP_SLOTS + slotId;
                if (idx < entries.size()) buy(sp, entries.get(idx), shift);
            } else if (slotId == SLOT_PREV) {
                if (page > 0) { page--; refresh(); }
            } else if (slotId == SLOT_NEXT) {
                if (page < maxPage()) { page++; refresh(); }
            } else if (slotId >= PLAYER_SLOTS_START) {
                sell(sp, slots.get(slotId), shift);
            }
        }

        sendAllDataToRemote();
    }

    private void buy(ServerPlayer sp, Recycler.Entry entry, boolean fullStack) {
        int qty = fullStack ? new ItemStack(entry.item()).getMaxStackSize() : 1;
        long total = (long) entry.buyPrice() * qty;

        if (!RecyclerMoney.take(sp, total)) {
            sp.displayClientMessage(
                    Component.literal("Saldo insuficiente (" + total + " " + CURRENCY + ")")
                            .withStyle(ChatFormatting.RED), true);
            return;
        }

        int remaining = qty;
        int maxStack = new ItemStack(entry.item()).getMaxStackSize();
        while (remaining > 0) {
            ItemStack give = new ItemStack(entry.item(), Math.min(remaining, maxStack));
            remaining -= give.getCount();
            sp.getInventory().add(give);
            if (!give.isEmpty()) {
                sp.drop(give, false);
            }
        }

        sp.playNotifySound(SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5F, 1.0F);
        sp.displayClientMessage(
                Component.literal("Comprou " + qty + "x por " + total + " " + CURRENCY)
                        .withStyle(ChatFormatting.GREEN), true);
    }

    private void sell(ServerPlayer sp, Slot slot, boolean all) {
        ItemStack stack = slot.getItem();
        if (stack.isEmpty()) return;

        if (!stack.getComponentsPatch().isEmpty()) {
            sp.displayClientMessage(
                    Component.literal("Item modificado não pode ser vendido").withStyle(ChatFormatting.RED), true);
            return;
        }

        var entry = Recycler.INSTANCE.find(stack.getItem());
        if (entry.isEmpty() || entry.get().sellPrice() <= 0) {
            sp.displayClientMessage(
                    Component.literal("A máquina não compra este item").withStyle(ChatFormatting.RED), true);
            return;
        }

        int qty = all ? stack.getCount() : 1;
        long total = (long) entry.get().sellPrice() * qty;

        stack.shrink(qty);
        slot.setChanged();
        RecyclerMoney.give(sp, total);

        sp.playNotifySound(SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5F, 1.4F);
        sp.displayClientMessage(
                Component.literal("Vendeu " + qty + "x por " + total + " " + CURRENCY)
                        .withStyle(ChatFormatting.GOLD), true);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, block);
    }

}
