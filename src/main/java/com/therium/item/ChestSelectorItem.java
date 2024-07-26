package com.therium.item;

import com.therium.util.DataManager;
import com.therium.util.ChestData;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

        if (world.getBlockEntity(pos) instanceof LootableContainerBlockEntity containerEntity) {
            if (player instanceof ServerPlayerEntity) {
                ChestData chestData = getChestData(pos, containerEntity);
                DataManager.saveChestData(List.of(chestData)); // Save the chest data
                player.sendMessage(Text.literal("Container data saved!"), false);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    public boolean onLeftClick(PlayerEntity player, World world, BlockPos pos, Hand hand) {
        if (world.getBlockEntity(pos) instanceof LootableContainerBlockEntity containerEntity) {
            if (player instanceof ServerPlayerEntity) {
                ChestData chestData = getChestData(pos, containerEntity);
                DataManager.saveChestData(List.of(chestData));
                player.sendMessage(Text.literal("Container data saved!"), false);
                return true;
            }
        }
        return false;
    }

    private ChestData getChestData(BlockPos pos, LootableContainerBlockEntity containerEntity) {
        Map<Integer, ItemStack> items = new HashMap<>();
        for (int i = 0; i < containerEntity.size(); i++) {
            items.put(i, containerEntity.getStack(i));
        }

        String containerType;
        if (containerEntity instanceof ChestBlockEntity) {
            containerType = "chest";
        } else if (containerEntity instanceof ShulkerBoxBlockEntity) {
            containerType = "shulker";
        } else if (containerEntity instanceof BarrelBlockEntity) {
            containerType = "barrel";
        } else {
            containerType = "unknown";
        }

        return new ChestData(pos, items, containerType);
    }
}
