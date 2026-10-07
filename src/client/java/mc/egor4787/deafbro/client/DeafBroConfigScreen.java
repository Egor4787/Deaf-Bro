package mc.egor4787.deafbro.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;

import java.util.Locale;

public class DeafBroConfigScreen extends Screen {
    private final Screen parent;

    public DeafBroConfigScreen(Screen parent) {
        super(Component.literal("Deaf Bro"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;

        addRenderableWidget(
                CycleButton.onOffBuilder(DeafBroConfig.enabled)
                        .create(
                                centerX - 100,
                                45,
                                200,
                                20,
                                Component.literal("Mod"),
                                (_, value) -> {
                                    DeafBroConfig.enabled = value;
                                    DeafBroConfig.save();
                                }
                        )
        );

        for (int i = 0; i < 4; i++) {
            int presetIndex = i;

            EditBox preset = new EditBox(
                    this.font,
                    centerX - 100 + i * 50,
                    80,
                    45,
                    20,
                    Component.literal("Preset " + (i + 1))
            );

            preset.setMaxLength(3);
            preset.setValue(
                    DeafBroConfig.presets[i] == -1
                            ? ""
                            : Integer.toString(DeafBroConfig.presets[i]));

            preset.setResponder(value -> {
                if (value.isEmpty()) {
                    DeafBroConfig.presets[presetIndex] = -1;
                    DeafBroConfig.save();
                    return;
                }

                if (value.matches("\\d{1,3}")) {
                    int number = Integer.parseInt(value);

                    if (number <= 100) {
                        DeafBroConfig.presets[presetIndex] = number;
                        DeafBroConfig.save();
                    }
                }
            });

            addRenderableWidget(preset);
        }

        addRenderableWidget(
                CycleButton.builder(
                                DeafBroConfigScreen::formatSource,
                                DeafBroConfig.soundSource
                        )
                        .withValues(SoundSource.values())
                        .create(
                                centerX - 100,
                                115,
                                200,
                                20,
                                Component.literal("Source"),
                                (_, value) -> {
                                    DeafBroConfig.soundSource = value;
                                    DeafBroConfig.save();
                                }
                        )
        );

        addRenderableWidget(
                Button.builder(
                                Component.literal("Done"),
                                _ -> onClose()
                        )
                        .bounds(centerX - 100, this.height - 40, 200, 20)
                        .build()
        );
    }

    private static Component formatSource(SoundSource source) {
        String name = source.name().toLowerCase(Locale.ROOT);

        String formatted = Character.toUpperCase(name.charAt(0))
                + name.substring(1);

        return Component.literal(formatted);
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(parent);
    }
}