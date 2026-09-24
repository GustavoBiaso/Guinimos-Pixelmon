package com.guinimos.guinimospixelmon.recycler;

import com.pixelmonmod.pixelmon.api.economy.BankAccount;
import com.pixelmonmod.pixelmon.api.extensions.PixelmonPlayerExtension;
import net.minecraft.server.level.ServerPlayer;

import java.math.BigDecimal;

public class RecyclerMoney {
    private RecyclerMoney() {}

    private static BankAccount account(ServerPlayer player) {
        return ((PixelmonPlayerExtension) player).getBankAccountNow();
    }

    public static boolean take(ServerPlayer player, long amount) {
        BankAccount acc = account(player);
        if (acc == null) return false;

        BigDecimal value = BigDecimal.valueOf(amount);
        if (acc.getBalance().compareTo(value) < 0) return false;

        acc.take(value);
        return true;
    }

    public static void give(ServerPlayer player, long amount) {
        BankAccount acc = account(player);
        if (acc == null) return;
        acc.add(BigDecimal.valueOf(amount));
    }

}
