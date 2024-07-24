package com.therium;

import com.therium.item.ModItems;
import com.therium.util.ModRegistries;
import com.therium.util.OptionalTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import com.therium.event.ModEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Therium implements ModInitializer {
	public static final String MOD_ID = "therium";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static Gson gson;

	@Override
	public void onInitialize() {
		// Initialize Gson with custom TypeAdapterFactory
		gson = new GsonBuilder()
				.registerTypeAdapterFactory(OptionalTypeAdapter.optionalFactory())
				.create();
;

		// Initialize other components
		ModEventHandler.registerEvents();
		ModItems.registerItems();
		ModRegistries.registerModStuffs();
		LOGGER.info("{} initialized.", MOD_ID);
	}

	// Provide access to the Gson instance
	public static Gson getGson() {
		return gson;
	}
}
