package com.therium.item;

import com.therium.Therium;
import com.therium.util.DataManager;
import com.therium.util.ChestData;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.ItemUsageContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChestSelectorItem extends Item {

    public ChestSelectorItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();

        Therium.LOGGER.info("ChestSelectorItem used at {}", pos);

        if (world.getBlockEntity(pos) instanceof ChestBlockEntity chestEntity) {
            if (player != null) {
                ChestData chestData = getChestData(pos, chestEntity);
                DataManager.saveChestData(List.of(chestData)); // Save the chest data
                player.sendMessage(Text.literal("Chest data saved!"), false);
                Therium.LOGGER.info("Chest data saved at {}", pos);
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }


    private ChestData getChestData(BlockPos pos, ChestBlockEntity chestEntity) {
        Map<Integer, ItemStack> items = new HashMap<>();
        for (int i = 0; i < chestEntity.size(); i++) {
            items.put(i, chestEntity.getStack(i));
        }
        return new ChestData(pos, items);
    }
}
