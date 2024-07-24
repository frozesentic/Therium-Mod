package com.therium.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.therium.util.ChestData;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class ReloadChestsCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReloadChestsCommand.class);
    private static final Gson GSON = new GsonBuilder().create();
    private static final Type CHEST_DATA_LIST_TYPE = new TypeToken<List<ChestData>>(){}.getType();
    private static final String FILE_PATH = "C:/Users/creeh/OneDrive/Main/PROGRAMS/Therium-Mod/run/chests.json";

    static {
        // Log the file path and check if the file exists
        File file = new File(FILE_PATH);
        if (file.exists()) {
            LOGGER.info("File path is correctly set. File exists at: {}", FILE_PATH);
        } else {
            LOGGER.warn("File does not exist at path: {}", FILE_PATH);
        }
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("rchests")
                .executes(ReloadChestsCommand::execute)
        );
        LOGGER.info("Registered /rchests command.");
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        World world = source.getWorld();
        boolean hasErrors = false;

        LOGGER.info("Executing /rchests command.");

        // Load the chest data
        List<ChestData> chestDataList = loadChestDataFromFile();
        if (chestDataList.isEmpty()) {
            LOGGER.warn("No chest data found in the file.");
            source.sendFeedback(() -> Text.literal("No chest data found."), true);
            return Command.SINGLE_SUCCESS;
        }

        LOGGER.info("Loaded {} chest data entries.", chestDataList.size());

        for (ChestData chestData : chestDataList) {
            BlockPos pos = chestData.getPosition();
            Map<Integer, ItemStack> items = chestData.getItems();

            LOGGER.info("Processing chest at position: {}", pos);

            try {
                placeChestWithItems(world, pos, items);
            } catch (Exception e) {
                LOGGER.error("Failed to place chest at position {}", pos, e);
                source.sendFeedback(() -> Text.literal("Failed to place chest at position " + pos.toShortString()), true);
                hasErrors = true; // Set flag to true if there's an error
            }
        }

        if (hasErrors) {
            source.sendFeedback(() -> Text.literal("Some chests failed to reload."), true);
            return 1; // Non-zero value indicates an error
        }

        source.sendFeedback(() -> Text.literal("Chests reloaded successfully!"), true);
        return Command.SINGLE_SUCCESS;
    }

    private static List<ChestData> loadChestDataFromFile() {
        LOGGER.info("Loading chest data from file: {}", FILE_PATH);
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            LOGGER.error("Chest data file does not exist at path: {}", FILE_PATH);
            return List.of();
        }

        try (FileReader reader = new FileReader(file)) {
            // Read the file into a String
            StringBuilder jsonBuilder = new StringBuilder();
            char[] buffer = new char[1024];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                jsonBuilder.append(buffer, 0, bytesRead);
            }
            String jsonString = jsonBuilder.toString();
            LOGGER.info("Raw JSON data: {}", jsonString);

            // Deserialize the JSON data
            List<ChestData> chestData = GSON.fromJson(jsonString, CHEST_DATA_LIST_TYPE);
            LOGGER.info("Loaded {} chest data entries from file.", chestData.size());
            return chestData;
        } catch (IOException e) {
            LOGGER.error("Failed to read chest data from file", e);
            return List.of();
        } catch (JsonSyntaxException e) {
            LOGGER.error("Failed to parse JSON from file", e);
            return List.of();
        } catch (Exception e) {
            LOGGER.error("Unexpected error while loading chest data from file", e);
            return List.of();
        }
    }

    private static void placeChestWithItems(World world, BlockPos position, Map<Integer, ItemStack> items) {
        LOGGER.info("Placing chest at position: {}", position);

        // Ensure the chunk is loaded
        try {
            world.getChunkManager().getChunk(position.getX() >> 4, position.getZ() >> 4, ChunkStatus.FULL, true);
            LOGGER.info("Chunk loaded for position: {}", position);
        } catch (Exception e) {
            LOGGER.error("Failed to load chunk for position: {}", position, e);
            throw e; // Re-throw exception to be caught in the execute method
        }

        // Place the chest block
        try {
            world.setBlockState(position, Blocks.CHEST.getDefaultState());
            LOGGER.info("Chest block placed at position: {}", position);
        } catch (Exception e) {
            LOGGER.error("Failed to place chest block at position: {}", position, e);
            throw e; // Re-throw exception to be caught in the execute method
        }

        // Get the block entity and check if it is a ChestBlockEntity
        try {
            if (world.getBlockEntity(position) instanceof ChestBlockEntity chestEntity) {
                LOGGER.info("Found ChestBlockEntity at position: {}", position);

                // Populate the chest with items
                for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
                    chestEntity.setStack(entry.getKey(), entry.getValue());
                    LOGGER.info("Set slot {} with item {}", entry.getKey(), entry.getValue());
                }
                chestEntity.markDirty(); // Notify the chest block entity that its data has changed
                LOGGER.info("ChestBlockEntity marked as dirty at position: {}", position);
            } else {
                LOGGER.warn("No ChestBlockEntity found at position: {}", position);
            }
        } catch (Exception e) {
            LOGGER.error("Error processing ChestBlockEntity at position: {}", position, e);
            throw e; // Re-throw exception to be caught in the execute method
        }
    }
}
