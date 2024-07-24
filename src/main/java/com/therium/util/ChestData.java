package com.therium.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ChestData {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChestData.class);

    private BlockPos position;
    private Map<Integer, ItemStack> items;

    // Default constructor
    public ChestData() {
        LOGGER.info("ChestData default constructor called.");
    }

    // Constructor with parameters
    public ChestData(BlockPos position, Map<Integer, ItemStack> items) {
        this.position = position;
        this.items = items;
        LOGGER.info("ChestData created with position: {} and items: {}", position, items);
    }

    public BlockPos getPosition() {
        LOGGER.info("Getting position: {}", position);
        return position;
    }

    public void setPosition(BlockPos position) {
        this.position = position;
        LOGGER.info("Setting position to: {}", position);
    }

    public Map<Integer, ItemStack> getItems() {
        LOGGER.info("Getting items: {}", items);
        return items;
    }

    public void setItems(Map<Integer, ItemStack> items) {
        this.items = items;
        LOGGER.info("Setting items to: {}", items);
    }

    @Override
    public String toString() {
        return "ChestData{" +
                "position=" + position +
                ", items=" + items +
                '}';
    }
}
