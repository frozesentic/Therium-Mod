package com.therium.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.server.MinecraftServer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReloadChestsCommand {

    private static final String COMMANDS_FILE_PATH = "C:/Users/creeh/OneDrive/Main/PROGRAMS/Therium-Mod/PythonModule/pythonscript/generated_commands";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("reloadchests")
                .requires(source -> source.hasPermissionLevel(4))  // Ensure the player has permission
                .executes(context -> {
                    executeCommandsFromFile(context.getSource());
                    return 1;
                })
        );
    }

    private static void executeCommandsFromFile(ServerCommandSource source) {
        MinecraftServer server = source.getServer();
        try (BufferedReader reader = new BufferedReader(new FileReader(COMMANDS_FILE_PATH))) {
            String command;
            while ((command = reader.readLine()) != null) {
                command = command.trim();
                if (!command.isEmpty()) {
                    final String finalCommand = command;  // Make command effectively final
                    try {
                        int result = server.getCommandManager().getDispatcher().execute(finalCommand, source);
                        // Optionally log or handle the result of command execution
                        if (result > 0) {
                            source.sendFeedback(() -> Text.literal("Successfully executed command: " + finalCommand), false);
                        }
                    } catch (CommandSyntaxException e) {
                        final String errorMessage = e.getMessage();  // Make errorMessage effectively final
                        source.sendError(Text.literal("Failed to execute command: " + finalCommand + " due to: " + errorMessage));
                    }
                }
            }
        } catch (IOException e) {
            final String errorMessage = e.getMessage();  // Make errorMessage effectively final
            source.sendError(Text.literal("An error occurred while reading the commands file: " + errorMessage));
        }
    }
}
