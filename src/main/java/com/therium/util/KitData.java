package com.therium.util;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class KitData {

    public static void giveKit(ServerPlayerEntity player) {
        // Elytra
        ItemStack elytra = new ItemStack(Items.ELYTRA);
        player.getInventory().insertStack(elytra);

        // Firework Rockets (2 stacks)
        ItemStack rockets = new ItemStack(Items.FIREWORK_ROCKET, 64);
        ItemStack rockets2 = rockets.copy();
        player.getInventory().insertStack(rockets);
        player.getInventory().insertStack(rockets2);

        // Golden Apples (1 stack)
        ItemStack goldenApples = new ItemStack(Items.GOLDEN_APPLE, 64);
        player.getInventory().insertStack(goldenApples);

        // Netherite Pickaxe with custom name and enchantments
        ItemStack pickaxe = new ItemStack(Items.NETHERITE_PICKAXE);
        NbtCompound displayTag = new NbtCompound();
        displayTag.putString("Name", Text.Serializer.toJson(Text.literal("NoGrief").formatted(Formatting.RED)));
        NbtCompound tag = pickaxe.getOrCreateNbt();
        tag.put("display", displayTag);
        pickaxe.addEnchantment(Enchantments.EFFICIENCY, 1);
        pickaxe.addEnchantment(Enchantments.UNBREAKING, 1);
        pickaxe.addEnchantment(Enchantments.FORTUNE, 1);

        player.getInventory().insertStack(pickaxe);
    }
}
