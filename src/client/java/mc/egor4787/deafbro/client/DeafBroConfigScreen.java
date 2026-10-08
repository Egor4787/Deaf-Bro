package mc.egor4787.deafbro.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;

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
                                Component.translatable("options.key.toggle"),
                                (_, value) -> {
                                    DeafBroConfig.enabled = value;
                                    DeafBroConfig.save();
                                }
                        )
        );

        int presetWidth = 44;
        int presetGap = 6;
        int totalWidth = presetWidth * 4 + presetGap * 3;
        int startX = centerX - totalWidth / 2;

        for (int i = 0; i < 4; i++) {
            int presetIndex = i;

            EditBox preset = new EditBox(
                    this.font,
                    startX + i * (presetWidth + presetGap),
                    72,
                    presetWidth,
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
                                100,
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
                CycleButton.onOffBuilder(DeafBroConfig.overlay)
                        .create(
                                centerX - 100,
                                130,
                                98,
                                20,
                                Component.literal("Overlay"),
                                (_, value) -> {
                                    DeafBroConfig.overlay = value;
                                    DeafBroConfig.save();
                                }
                        )
        );

        addRenderableWidget(
                CycleButton.onOffBuilder(DeafBroConfig.beep)
                        .create(
                                centerX + 2,
                                130,
                                98,
                                20,
                                Component.literal("Beep"),
                                (_, value) -> {
                                    DeafBroConfig.beep = value;
                                    DeafBroConfig.save();
                                }
                        )
        );

        addRenderableWidget(
                Button.builder(
                                Component.literal("Defaults"),
                                _ -> {
                                    DeafBroConfig.presets = new int[]{0, 25, 50, 100};
                                    DeafBroConfig.currentPreset = -1;
                                    DeafBroConfig.soundSource = SoundSource.MASTER;
                                    DeafBroConfig.overlay = true;
                                    DeafBroConfig.beep = true;
                                    DeafBroConfig.save();

                                    this.minecraft.gui.setScreen(
                                            new DeafBroConfigScreen(parent)
                                    );
                                }
                        )
                        .bounds(centerX - 100, this.height - 40, 98, 20)
                        .build()
        );

        addRenderableWidget(
                Button.builder(
                                Component.translatable("gui.done"),
                                _ -> onClose()
                        )
                        .bounds(centerX + 2, this.height - 40, 98, 20)
                        .build()
        );
    }

    private static Component formatSource(SoundSource source) {
        return Component.translatable("soundCategory." + source.getName());
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(parent);
    }
}