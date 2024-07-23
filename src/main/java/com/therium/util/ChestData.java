package com.therium.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class ChestData {
    private final BlockPos position;
    private final Map<Integer, ItemStack> items;

    public ChestData(BlockPos position, Map<Integer, ItemStack> items) {
        this.position = position;
        this.items = items;
    }

    public BlockPos getPosition() {
        return position;
    }

    public Map<Integer, ItemStack> getItems() {
        return items;
    }
}
