package com.therium.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ItemStackAdapter extends TypeAdapter<ItemStack> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemStackAdapter.class);

    @Override
    public void write(JsonWriter out, ItemStack stack) throws IOException {
        LOGGER.info("Writing ItemStack: {}", stack);
        out.beginObject();
        out.name("item").value(Registries.ITEM.getId(stack.getItem()).toString());
        out.name("count").value(stack.getCount());
        // Serializing item components, if needed
        out.endObject();
        LOGGER.info("Finished writing ItemStack: {}", stack);
    }

    @Override
    public ItemStack read(JsonReader in) throws IOException {
        LOGGER.info("Reading ItemStack");
        in.beginObject();
        Item item = null;
        int count = 0;

        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "item" -> {
                    Identifier itemId = Identifier.of(in.nextString());
                    item = Registries.ITEM.get(itemId);
                    LOGGER.info("Read item: {}", itemId);
                }
                case "count" -> {
                    count = in.nextInt();
                    LOGGER.info("Read count: {}", count);
                }
                default -> LOGGER.warn("Unexpected name: {}", name);
            }
        }
        in.endObject();

        if (item == null) {
            IOException e = new IOException("Item not found during deserialization");
            LOGGER.error("Error during deserialization", e);
            throw e;
        }

        ItemStack stack = new ItemStack(item, count);
        LOGGER.info("Finished reading ItemStack: {}", stack);
        return stack;
    }
}
