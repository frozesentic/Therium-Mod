package com.therium.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import com.therium.command.BoopCommand;
import com.therium.command.ReloadChestsCommand;

public class  ModRegistries {
    public static void registerModStuffs() {
        registerCommands();
    }

    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(BoopCommand::register);
        CommandRegistrationCallback.EVENT.register(ReloadChestsCommand::register);
    }
}
