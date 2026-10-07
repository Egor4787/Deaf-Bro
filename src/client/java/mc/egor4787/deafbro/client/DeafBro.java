package mc.egor4787.deafbro.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;

public class DeafBro implements ClientModInitializer {
	private static final KeyMapping DEAF_TOGGLE = KeyMappingHelper.registerKeyMapping(
			new KeyMapping(
					"Deaf Toggle",
					InputConstants.Type.KEYSYM,
					InputConstants.KEY_MINUS,
					KeyMapping.Category.MISC
			)
	);

	@Override
	public void onInitializeClient() {
		DeafBroConfig.load();

		final boolean[] firstTick = {true};

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (firstTick[0]) {
				firstTick[0] = false;

				if (DeafBroConfig.enabled
						&& DeafBroConfig.currentPreset >= 0
						&& DeafBroConfig.currentPreset < DeafBroConfig.presets.length
						&& DeafBroConfig.presets[DeafBroConfig.currentPreset] >= 0) {

					float volume = DeafBroConfig.presets[DeafBroConfig.currentPreset] / 100.0f;

					client.options.getSoundSourceOptionInstance(DeafBroConfig.soundSource)
							.set((double) volume);
				}
			}

			while (DEAF_TOGGLE.consumeClick()) {
				if (!DeafBroConfig.enabled) {
					continue;
				}

				int nextPreset = DeafBroConfig.currentPreset;

				for (int i = 0; i < DeafBroConfig.presets.length; i++) {
					nextPreset = (nextPreset + 1) % DeafBroConfig.presets.length;

					if (DeafBroConfig.presets[nextPreset] >= 0) {
						DeafBroConfig.currentPreset = nextPreset;

						float volume = DeafBroConfig.presets[nextPreset] / 100.0f;

						client.options.getSoundSourceOptionInstance(DeafBroConfig.soundSource)
								.set((double) volume);

						DeafBroConfig.save();
						// Will be changed to a popup later
                        assert client.player != null;
                        client.player.sendSystemMessage(Component.literal("Volume: "+ volume));
						break;
					}
				}
			}
		});
	}
}