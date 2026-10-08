package mc.egor4787.deafbro.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.sounds.SoundSource;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class DeafBroConfig {
    public static boolean enabled = true;
    public static int currentPreset = -1;
    public static SoundSource soundSource = SoundSource.MASTER;
    public static boolean overlay = true;
    public static boolean beep = true;

    public static int[] presets = {0, 25, 50, 100};

    private static final Path FILE = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("deafbro.properties");

    public static void load() {
        if (!Files.exists(FILE)) {
            return;
        }

        Properties properties = new Properties();

        try (Reader reader = Files.newBufferedReader(FILE)) {
            properties.load(reader);

            enabled = Boolean.parseBoolean(properties.getProperty("enabled", "true"));

            try {
                currentPreset = Integer.parseInt(properties.getProperty("currentPreset", "-1"));
            } catch (NumberFormatException ignored) {
                currentPreset = -1;
            }

            for (int i = 0; i < presets.length; i++) {
                try {
                    int value = Integer.parseInt(properties.getProperty("preset" + i, Integer.toString(presets[i])));

                    if (value >= -1 && value <= 100) {
                        presets[i] = value;
                    }
                } catch (NumberFormatException ignored) {}
            }

            try {
                soundSource = SoundSource.valueOf(properties.getProperty("source", SoundSource.MASTER.name()));
            } catch (IllegalArgumentException ignored) {
                soundSource = SoundSource.MASTER;
            }

            overlay = Boolean.parseBoolean(properties.getProperty("overlay", "true"));
            beep = Boolean.parseBoolean(properties.getProperty("beep", "true"));
        } catch (IOException ignored) {}
    }

    public static void save() {
        Properties properties = new Properties();

        properties.setProperty("enabled", Boolean.toString(enabled));

        properties.setProperty("currentPreset", Integer.toString(currentPreset));

        for (int i = 0; i < presets.length; i++) {
            properties.setProperty("preset" + i, Integer.toString(presets[i]));
        }

        properties.setProperty("source", soundSource.name());

        properties.setProperty("overlay", Boolean.toString(overlay));
        properties.setProperty("beep", Boolean.toString(beep));

        try {
            Files.createDirectories(FILE.getParent());

            try (Writer writer = Files.newBufferedWriter(FILE)) {
                properties.store(writer, "DeafBro");
            }
        } catch (IOException ignored) {}
    }
}
