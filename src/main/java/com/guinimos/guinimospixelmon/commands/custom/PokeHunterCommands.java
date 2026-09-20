package com.guinimos.guinimospixelmon.commands.custom;

import com.guinimos.guinimospixelmon.pokehunt.PokeHunt;
import com.guinimos.guinimospixelmon.pokehunt.PokeHuntSavedData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

public class PokeHunterCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("pokehunter")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("reset")
                                .executes(PokeHunterCommands::resetHunt))
        );
    }

    private static int resetHunt(CommandContext<CommandSourceStack> ctx) {
        MinecraftServer server = ctx.getSource().getServer();
        PokeHuntSavedData data = PokeHuntSavedData.get(server);

        data.setHunt(PokeHunt.INSTANCE.rollHuntList());

        ctx.getSource().sendSuccess(() -> Component.literal("Hunt resetada com sucesso."), true);
        return 1;
    }
}
