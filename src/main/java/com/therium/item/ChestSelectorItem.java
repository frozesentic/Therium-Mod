package com.therium.item;

import com.therium.Therium;
import com.therium.util.DataManager;
import com.therium.util.ChestData;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
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
        PlayerEntity player = context.getPlayer();

        Therium.LOGGER.info("ChestSelectorItem used on block at {}", pos);

        if (world.getBlockEntity(pos) instanceof ChestBlockEntity chestEntity) {
            Therium.LOGGER.info("ChestBlockEntity found at {}", pos);
            if (player instanceof ServerPlayerEntity) {
                ChestData chestData = getChestData(pos, chestEntity);
                DataManager.saveChestData(List.of(chestData)); // Save the chest data
                player.sendMessage(Text.literal("Chest data saved!"), false);
                Therium.LOGGER.info("Chest data saved at {}", pos);
                return ActionResult.SUCCESS;
            } else {
                Therium.LOGGER.warn("Player is not a ServerPlayerEntity at {}", pos);
            }
        } else {
            Therium.LOGGER.warn("Block entity at {} is not a ChestBlockEntity", pos);
        }

        return ActionResult.PASS;
    }

    public boolean onLeftClick(PlayerEntity player, World world, BlockPos pos, Hand hand) {
        Therium.LOGGER.info("ChestSelectorItem left-clicked on block at {}", pos);

        if (world.getBlockEntity(pos) instanceof ChestBlockEntity chestEntity) {
            Therium.LOGGER.info("ChestBlockEntity found at {}", pos);
            if (player instanceof ServerPlayerEntity) {
                ChestData chestData = getChestData(pos, chestEntity);
                DataManager.saveChestData(List.of(chestData));
                player.sendMessage(Text.literal("Chest data saved!"), false);
                Therium.LOGGER.info("Chest data saved at {}", pos);
                return true;
            } else {
                Therium.LOGGER.warn("Player is not a ServerPlayerEntity at {}", pos);
            }
        } else {
            Therium.LOGGER.warn("Block entity at {} is not a ChestBlockEntity", pos);
        }

        return false;
    }

    private ChestData getChestData(BlockPos pos, ChestBlockEntity chestEntity) {
        Map<Integer, ItemStack> items = new HashMap<>();
        for (int i = 0; i < chestEntity.size(); i++) {
            items.put(i, chestEntity.getStack(i));
        }
        ChestData chestData = new ChestData(pos, items);
        Therium.LOGGER.info("Creating ChestData: {}", chestData);
        return chestData;
    }
}
