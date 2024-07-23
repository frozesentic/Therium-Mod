package com.therium.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class BoopCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment dedicated) {
        dispatcher.register(CommandManager.literal("boop")
                .executes(context -> {
                    context.getSource().sendFeedback(() -> Text.literal("boop"), false);
                    return 1;
                })
        );
    }
}
