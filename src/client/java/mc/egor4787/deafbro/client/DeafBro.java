package mc.egor4787.deafbro.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.sounds.SoundSource;

public class DeafBro implements ClientModInitializer {
	private static final KeyMapping DEAF_TOGGLE = KeyMappingHelper.registerKeyMapping(
			new KeyMapping(
					"Deaf Toggle",
					InputConstants.Type.KEYSYM,
					InputConstants.KEY_MINUS,
					KeyMapping.Category.MISC
			)
	);

	private double previousVolume = 0.5;

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (DEAF_TOGGLE.consumeClick()) {
				float currentVolume =
						client.options.getSoundSourceVolume(SoundSource.MASTER);

                if (currentVolume > 0.0f) {
					previousVolume = currentVolume;

					client.options.getSoundSourceOptionInstance(SoundSource.MASTER).set(0.0);
				} else {
					client.options.getSoundSourceOptionInstance(SoundSource.MASTER).set(previousVolume);
				}
			}
		});
	}
}