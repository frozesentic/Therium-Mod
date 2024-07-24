package com.therium.event;

import com.therium.item.ChestSelectorItem;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.util.ActionResult;
import net.minecraft.item.ItemStack;

public class ModEventHandler {

    public static void registerEvents() {
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem() instanceof ChestSelectorItem item) {
                if (item.onLeftClick(player, world, pos, hand)) {
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.PASS;
        });
    }
}


