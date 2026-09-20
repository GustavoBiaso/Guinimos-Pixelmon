package com.guinimos.guinimospixelmon.menuscreens.portablehealer;

import com.guinimos.guinimospixelmon.GuinimosPixelmon;
import com.guinimos.guinimospixelmon.network.payload.PortableHealerPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

public class PortableHealerScreen extends Screen {

    public PortableHealerScreen() {
        super(Component.nullToEmpty("Portable Healer"));
    }

    private static final ResourceLocation TEX_A =
            ResourceLocation.fromNamespaceAndPath(GuinimosPixelmon.MODID, "textures/gui/portable_healer/healer.png");
    private static final ResourceLocation TEX_B =
            ResourceLocation.fromNamespaceAndPath(GuinimosPixelmon.MODID, "textures/gui/portable_healer/pc.png");

    @Override
    protected void init() {
        int gap = 8; // espaço entre os botões
        int startX = (this.width - (PortableHealerButton.SIZE * 2 + gap)) / 2;
        int y = (this.height - PortableHealerButton.SIZE) / 2;

        this.addRenderableWidget(new PortableHealerButton(startX, y, TEX_A, Component.literal("Healer"),
                () -> PortableHealerPayloadCaller(0)));

        this.addRenderableWidget(new PortableHealerButton(startX + PortableHealerButton.SIZE + gap, y, TEX_B, Component.literal("PC"),
                () -> PortableHealerPayloadCaller(1)));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 40, 0xFFFFFF);
    }

    private void PortableHealerPayloadCaller(int buttonId){
        PacketDistributor.sendToServer(new PortableHealerPayload(buttonId));
        if(buttonId == 0){
            this.onClose();
        }
    }
}
