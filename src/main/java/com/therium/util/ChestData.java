package com.therium.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class ChestData {
    private final BlockPos pos;
    private final Map<Integer, ItemStack> items;
    private final String containerType;

    public ChestData(BlockPos pos, Map<Integer, ItemStack> items, String containerType) {
        this.pos = pos;
        this.items = items;
        this.containerType = containerType;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Map<Integer, ItemStack> getItems() {
        return items;
    }

    public String getContainerType() {
        return containerType;
    }

    @Override
    public String toString() {
        return "ChestData{" +
                "pos=" + pos +
                ", items=" + items +
                ", containerType='" + containerType + '\'' +
                '}';
    }
}
