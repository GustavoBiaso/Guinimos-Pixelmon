package com.guinimos.guinimospixelmon.menuscreens.pokehunter;

import com.guinimos.guinimospixelmon.GuinimosPixelmon;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PokeHunterScreen extends AbstractContainerScreen<PokeHunterMenu> {

    private static final int SLOT_SIZE = 31;
    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(GuinimosPixelmon.MODID, "textures/gui/poke_hunter/window.png");
    private static final ResourceLocation SLOT_TEXTURE = ResourceLocation.fromNamespaceAndPath(GuinimosPixelmon.MODID, "textures/gui/poke_hunter/slot.png");


    public PokeHunterScreen(PokeHunterMenu  menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos - SLOT_SIZE, this.topPos - SLOT_SIZE, 0, 0, 252, 201, 252, 201);

        int startX = leftPos - SLOT_SIZE + (252 - (5 * SLOT_SIZE)) / 2;
        int startY = topPos - SLOT_SIZE + 28;

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                guiGraphics.blit(
                        SLOT_TEXTURE,
                        startX + col * SLOT_SIZE,
                        startY + row * SLOT_SIZE,
                        0,
                        0,
                        SLOT_SIZE,
                        SLOT_SIZE,
                        SLOT_SIZE,
                        SLOT_SIZE
                );
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        List<ItemStack> items = this.menu.getMenuItems();
        int startX = leftPos - SLOT_SIZE + (252 - (5 * SLOT_SIZE)) / 2;
        int startY = topPos - SLOT_SIZE + 28;

        for (int i = 0; i < items.size(); i++) {
            int x = startX + 8 + (i / 5) * SLOT_SIZE;
            int y = startY + 6 + (i % 5) * SLOT_SIZE;

            ItemStack stack = items.get(i);
            graphics.renderItem(stack, x, y);
        }

        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, -SLOT_SIZE + 8, -SLOT_SIZE + 6, 0x750606);
    }
}
