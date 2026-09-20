package com.guinimos.guinimospixelmon.network.payload;

import com.guinimos.guinimospixelmon.GuinimosPixelmon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public record PortableHealerPayload(int buttonId) implements CustomPacketPayload {

    public static final Type<PortableHealerPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GuinimosPixelmon.MODID, "portablehealer_payload"));

    public static final StreamCodec<ByteBuf, PortableHealerPayload> STREAM_CODEC =
            ByteBufCodecs.VAR_INT.map(PortableHealerPayload::new, PortableHealerPayload::buttonId);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
