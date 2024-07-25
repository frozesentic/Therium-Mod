package com.therium.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.server.MinecraftServer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class ForceLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForceLoader.class);

    // Hardcoded file path
    private static final String FIXED_FILE_PATH = "C:/Users/creeh/OneDrive/Main/PROGRAMS/Therium-Mod/run/chests.json";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("loader")
                .requires(source -> source.hasPermissionLevel(4))  // Ensure the player has permission
                .executes(ForceLoader::execute)
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        MinecraftServer server = source.getServer();
        String resolvedPath = Paths.get(FIXED_FILE_PATH).toAbsolutePath().toString();

        LOGGER.info("Executing /loader command with fixed file path: {}", resolvedPath);

        try (BufferedReader reader = new BufferedReader(new FileReader(resolvedPath))) {
            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

            if (jsonArray == null) {
                LOGGER.error("Failed to parse JSON file: {}", resolvedPath);
                source.sendError(Text.literal("Failed to parse JSON file."));
                return 0; // Return 0 or another code to indicate an error
            }

            LOGGER.info("Found {} entries in the JSON file", jsonArray.size());

            for (var jsonElement : jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonObject posObject = jsonObject.getAsJsonObject("pos");
                if (posObject == null) {
                    LOGGER.error("Missing 'pos' object in JSON entry.");
                    source.sendError(Text.literal("Missing 'pos' object in JSON entry."));
                    continue;
                }

                int x;
                int z;
                try {
                    x = posObject.get("x").getAsInt();
                    z = posObject.get("z").getAsInt();
                } catch (Exception e) {
                    LOGGER.error("Error extracting x and z coordinates from JSON object: {}", posObject, e);
                    source.sendError(Text.literal("Error extracting coordinates from JSON object."));
                    continue;
                }

                String command = String.format("forceload add %d %d", x, z);
                LOGGER.info("Executing command: {}", command);

                try {
                    int result = server.getCommandManager().getDispatcher().execute(command, source);
                    if (result > 0) {
                        LOGGER.info("Successfully executed command: {}", command);
                        source.sendFeedback(() -> Text.literal("Successfully executed command: " + command), false);
                    } else {
                        LOGGER.warn("Command execution returned no result: {}", command);
                    }
                } catch (CommandSyntaxException e) {
                    LOGGER.error("Failed to execute command: {} due to: {}", command, e.getMessage());
                    source.sendError(Text.literal("Failed to execute command: " + command + " due to: " + e.getMessage()));
                }
            }
        } catch (IOException e) {
            LOGGER.error("An error occurred while reading the commands file: {}", resolvedPath, e);
            source.sendError(Text.literal("An error occurred while reading the commands file: " + e.getMessage()));
            return 0; // Return 0 or another code to indicate an error
        } catch (Exception e) {
            LOGGER.error("Unexpected error occurred while executing /loader command", e);
            source.sendError(Text.literal("An unexpected error occurred: " + e.getMessage()));
            return 0; // Return 0 or another code to indicate an error
        }

        return 1; // Return 1 to indicate successful execution
    }
}
