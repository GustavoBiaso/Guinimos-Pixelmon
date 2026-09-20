package com.guinimos.guinimospixelmon.network;

import com.guinimos.guinimospixelmon.GuinimosPixelmon;
import com.guinimos.guinimospixelmon.item.custom.PortableHealerItem;
import com.guinimos.guinimospixelmon.network.payload.PortableHealerPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static com.guinimos.guinimospixelmon.item.custom.PortableHealerItem.HealParty;
import static com.guinimos.guinimospixelmon.item.custom.PortableHealerItem.OpenPC;

@EventBusSubscriber(modid = GuinimosPixelmon.MODID)
public class ModNetwork {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(PortableHealerPayload.TYPE, PortableHealerPayload.STREAM_CODEC, ModNetwork::handlePortableHealer);
    }

    private static void handlePortableHealer(PortableHealerPayload payload, IPayloadContext context) {
        Player player = context.player();

        boolean hasItem = player.getMainHandItem().getItem() instanceof PortableHealerItem || player.getOffhandItem().getItem() instanceof PortableHealerItem;
        if (!hasItem) return;

        switch (payload.buttonId()) {
            case 0 -> HealParty(player);
            case 1 -> OpenPC(player);
            default -> throw new IllegalStateException("Unexpected value: " + payload.buttonId());
        };
    }
}