package com.guinimos.guinimospixelmon.menuscreens.portablehealer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class PortableHealerButton extends AbstractWidget {

    public static final int SIZE = 32;

    private final ResourceLocation texture;
    private final Runnable onPress;

    public PortableHealerButton(int x, int y, ResourceLocation texture, Component message, Runnable onPress) {
        super(x, y, SIZE, SIZE, message);
        this.texture = texture;
        this.onPress = onPress;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        float shade = this.isHoveredOrFocused() ? 1.0f : 0.85f;

        RenderSystem.enableBlend();
        graphics.setColor(shade, shade, shade, 1.0f);
        graphics.blit(this.texture, this.getX(), this.getY(), 0, 0, SIZE, SIZE, SIZE, SIZE);
        graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.onPress.run();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        this.defaultButtonNarrationText(output);
    }
}