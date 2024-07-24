package com.therium.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataManager.class);
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(BlockPos.class, new BlockPosAdapter())
            .registerTypeAdapter(ItemStack.class, new ItemStackAdapter())
            .setPrettyPrinting() // Enable pretty printing
            .create();
    private static final Type CHEST_DATA_LIST_TYPE = new TypeToken<List<ChestData>>(){}.getType();
    private static final String FILE_PATH = "chests.json";

    public static void saveChestData(List<ChestData> newChestDataList) {
        List<ChestData> existingChestDataList = loadChestData();

        if (existingChestDataList == null) {
            existingChestDataList = new ArrayList<>();
        }

        existingChestDataList.addAll(newChestDataList);

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            GSON.toJson(existingChestDataList, writer);
        } catch (IOException e) {
            LOGGER.error("Failed to save chest data to file", e);
        }
    }

    public static List<ChestData> loadChestData() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return new ArrayList<>(); // Return an empty list if the file does not exist
        }

        try (FileReader reader = new FileReader(file)) {
            return GSON.fromJson(reader, CHEST_DATA_LIST_TYPE);
        } catch (IOException e) {
            LOGGER.error("Failed to load chest data from file", e);
            return new ArrayList<>(); // Return an empty list if there is an error
        }
    }
}
