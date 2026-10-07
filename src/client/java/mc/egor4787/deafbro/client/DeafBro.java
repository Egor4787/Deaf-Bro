package mc.egor4787.deafbro.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;

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

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (!DeafBroConfig.enabled) {
				return;
			}

			while (DEAF_TOGGLE.consumeClick()) {
				float currentVolume =
						client.options.getSoundSourceVolume(DeafBroConfig.source);

                if (currentVolume > 0.0f) {
					DeafBroConfig.previousVolume = currentVolume;

					client.options.getSoundSourceOptionInstance(DeafBroConfig.source).set(0.0);
				} else {
					if (DeafBroConfig.previousVolume <= 0.0f) {
						DeafBroConfig.previousVolume = 0.5f;
					}
					client.options.getSoundSourceOptionInstance(DeafBroConfig.source).set((double) DeafBroConfig.previousVolume);
				}
			}
		});
	}
}