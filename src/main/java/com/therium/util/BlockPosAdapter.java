package com.therium.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BlockPosAdapter extends TypeAdapter<BlockPos> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockPosAdapter.class);

    @Override
    public void write(JsonWriter out, BlockPos pos) throws IOException {
        out.beginObject();
        out.name("x").value(pos.getX());
        out.name("y").value(pos.getY());
        out.name("z").value(pos.getZ());
        out.endObject();
    }

    @Override
    public BlockPos read(JsonReader in) throws IOException {
        in.beginObject();
        int x = 0, y = 0, z = 0;
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "x" -> {
                    x = in.nextInt();
                }
                case "y" -> {
                    y = in.nextInt();
                }
                case "z" -> {
                    z = in.nextInt();
                }
                default -> LOGGER.warn("Unexpected name: {}", name);
            }
        }
        in.endObject();
        BlockPos pos = new BlockPos(x, y, z);
        LOGGER.info("Finished reading BlockPos: {}", pos);
        return pos;
    }
}
