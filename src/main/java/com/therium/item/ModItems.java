package com.therium.item;

import com.therium.Therium;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item CHEST_SELECTOR = register("chest_selector", new ChestSelectorItem(new Item.Settings()));

    private static void addItemsToItemGroup(FabricItemGroupEntries entries) {
        entries.add(CHEST_SELECTOR);
    }

    public static <T extends Item> Item register(String name, T item) {
        return Registry.register(Registries.ITEM, Identifier.of(Therium.MOD_ID, name), item);
    }

    public static void registerItems() {
        Therium.LOGGER.info("Registering Mod Items for " + Therium.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ModItems::addItemsToItemGroup);
    }
}
