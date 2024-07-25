package com.therium;

import com.therium.command.ForceLoader;
import com.therium.item.ModItems;
import com.therium.util.ModRegistries;
import net.fabricmc.api.ModInitializer;
import com.therium.event.ModEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.therium.command.ReloadChestsCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class Therium implements ModInitializer {
	public static final String MOD_ID = "therium";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(ForceLoader::register);
		CommandRegistrationCallback.EVENT.register(ReloadChestsCommand::register);
		ModEventHandler.registerEvents();
		ModItems.registerItems();
		ModRegistries.registerModStuffs();
		LOGGER.info("{} initialized.", MOD_ID);
	}
}
