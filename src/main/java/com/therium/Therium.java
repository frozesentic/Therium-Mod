package com.therium;

import com.therium.item.ModItems;
import com.therium.util.ModRegistries;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Therium implements ModInitializer {
	public static final String MOD_ID = "therium";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerItems();
		ModRegistries.registerModStuffs();
		LOGGER.info("{} initialized.", MOD_ID);
	}
}
