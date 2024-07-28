package com.therium.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
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

        ItemStack pickaxe = new ItemStack(Items.NETHERITE_PICKAXE);
        MutableText NoGrief = Text.literal("NoGrief").formatted(Formatting.RED);
        pickaxe.set(DataComponentTypes.CUSTOM_NAME, NoGrief);

        // Apply enchantments using RegistryEntry
        DynamicRegistryManager registryManager = player.getWorld().getRegistryManager();
        RegistryEntry.Reference<Enchantment> efficiency = registryManager.getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.EFFICIENCY);
        RegistryEntry.Reference<Enchantment> unbreaking = registryManager.getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.UNBREAKING);
        RegistryEntry.Reference<Enchantment> fortune = registryManager.getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);

        pickaxe.addEnchantment(efficiency, 1);
        pickaxe.addEnchantment(unbreaking, 1);
        pickaxe.addEnchantment(fortune, 1);

        player.getInventory().insertStack(pickaxe);
    }
}
