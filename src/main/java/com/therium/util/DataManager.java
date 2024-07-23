package com.therium.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class DataManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataManager.class);
    private static final Gson GSON = new Gson();
    private static final Type CHEST_DATA_LIST_TYPE = new TypeToken<List<ChestData>>(){}.getType();
    private static final String FILE_PATH = "chests.json";

    public static void saveChestData(List<ChestData> chestDataList) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            GSON.toJson(chestDataList, writer);
        } catch (IOException e) {
            LOGGER.error("Failed to save chest data to file", e);
        }
    }

    public static List<ChestData> loadChestData() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            return GSON.fromJson(reader, CHEST_DATA_LIST_TYPE);
        } catch (IOException e) {
            LOGGER.error("Failed to load chest data from file", e);
            return List.of(); // Return an empty list if there is an error
        }
    }
}
