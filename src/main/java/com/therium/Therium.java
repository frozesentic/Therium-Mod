package com.therium;

import net.fabricmc.api.ModInitializer;
import com.therium.util.ModRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Therium implements ModInitializer {
	public static final String 	MOD_ID = "therium";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModRegistries.registerModStuffs();
		LOGGER.info("Hello Fabric world!");
	}
}