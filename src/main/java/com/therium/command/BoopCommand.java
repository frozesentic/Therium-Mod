package com.therium.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class BoopCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("boop")
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayer();
                    if (player != null) {
                        // Broadcast the message as if the player sent it
                        Text message = Text.literal("<" + player.getName().getString() + "> boop");
                        context.getSource().getServer().getPlayerManager().broadcast(message, false);
                    } else {
                        context.getSource().sendFeedback(() -> Text.literal("This command can only be executed by a player"), false);
                    }
                    return 1;
                })
        );
    }
}
