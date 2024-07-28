package com.therium.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.therium.util.KitData;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.literal;

public class KitCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("kit")
                .then(CommandManager.literal("hermitcraft")
                        .executes(KitCommand::giveKit))
        );
    }

    private static int giveKit(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();

        // Logic to give items to the player
        if (player != null) {
            KitData.giveKit(player);
            context.getSource().sendFeedback(() -> Text.literal("Hermitcraft kit given!").formatted(Formatting.GREEN), false);
        }

        return 1;
    }
}
