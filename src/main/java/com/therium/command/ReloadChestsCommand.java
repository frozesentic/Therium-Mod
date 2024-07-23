package com.therium.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import com.therium.util.ChestData;
import com.therium.util.DataManager;

import java.util.List;
import java.util.Map;

public class ReloadChestsCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("rchests")
                .executes(context -> {
                    List<ChestData> chestDataList = DataManager.loadChestData();

                    for (ChestData chestData : chestDataList) {
                        placeChestWithItems(context.getSource(), chestData.getPosition(), chestData.getItems());
                    }

                    context.getSource().sendFeedback(() -> Text.literal("Chests restored from data"), false);
                    return 1;
                })
        );
    }

    private static void placeChestWithItems(ServerCommandSource source, BlockPos pos, Map<Integer, ItemStack> items) {
        source.getWorld().setBlockState(pos, Blocks.CHEST.getDefaultState());

        var blockEntity = source.getWorld().getBlockEntity(pos);
        if (blockEntity instanceof net.minecraft.block.entity.ChestBlockEntity chestEntity) {
            for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
                chestEntity.setStack(entry.getKey(), entry.getValue());
            }
            chestEntity.markDirty();
        }
    }
}
