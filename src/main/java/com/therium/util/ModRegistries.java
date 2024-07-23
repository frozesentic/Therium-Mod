package com.therium.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import com.therium.command.BoopCommand;

public class ModRegistries {
    public static void registerModStuffs() {
        registerCommands();
    }

    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(BoopCommand::register);
    }
}
