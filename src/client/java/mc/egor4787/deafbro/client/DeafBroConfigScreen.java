package mc.egor4787.deafbro.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
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
                                60,
                                200,
                                20,
                                Component.literal("Mod"),
                                (_, value) -> {
                                    DeafBroConfig.enabled = value;
                                    DeafBroConfig.save();
                                }
                        )
        );

        addRenderableWidget(
                CycleButton.builder(
                                DeafBroConfigScreen::formatSource,
                                DeafBroConfig.source
                        )
                        .withValues(SoundSource.values())
                        .create(
                                centerX - 100,
                                90,
                                200,
                                20,
                                Component.literal("Source"),
                                (_, value) -> {
                                    DeafBroConfig.source = value;
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