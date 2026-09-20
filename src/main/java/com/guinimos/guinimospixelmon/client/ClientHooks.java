package com.guinimos.guinimospixelmon.client;

import com.guinimos.guinimospixelmon.menuscreens.portablehealer.PortableHealerScreen;
import net.minecraft.client.Minecraft;

public class ClientHooks {
    public static void openPortableHealerScreen() {
        Minecraft.getInstance().setScreen(new PortableHealerScreen());
    }
}