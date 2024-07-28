package com.therium.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.therium.util.KitData;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.UUID;

import static net.minecraft.server.command.CommandManager.literal;

public class KitCommand implements ModInitializer {

    private static final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_TIME = 60 * 1000; // 60 seconds in milliseconds

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(KitCommand::register);
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("kit")
                .then(CommandManager.literal("hermitcraft")
                        .executes(KitCommand::giveKit))
        );
    }

    private static int giveKit(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (player == null) {
            context.getSource().sendError(Text.literal("Player not found!"));
            return 0;
        }

        UUID playerUUID = player.getUuid();
        long currentTime = System.currentTimeMillis();

        if (cooldowns.containsKey(playerUUID)) {
            long lastUsedTime = cooldowns.get(playerUUID);
            if ((currentTime - lastUsedTime) < COOLDOWN_TIME) {
                long timeLeft = (COOLDOWN_TIME - (currentTime - lastUsedTime)) / 1000;
                context.getSource().sendFeedback(() -> Text.literal("You must wait " + timeLeft + " seconds before using this command again.").formatted(Formatting.RED), false);
                return 0;
            }
        }

        if (player.getInventory().getEmptySlot() == -1) {
            context.getSource().sendFeedback(() -> Text.literal("Your inventory is full!").formatted(Formatting.RED), false);
            return 0;
        }

        KitData.giveKit(player);
        cooldowns.put(playerUUID, currentTime);
        context.getSource().sendFeedback(() -> Text.literal("Hermitcraft kit given!").formatted(Formatting.GREEN), false);
        return 1;
    }
}
