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
    public static SoundSource source = SoundSource.MASTER;

    public static float previousVolume = 0.5f;

    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("deafbro.properties");

    public static void load() {
        if (!Files.exists(FILE)) {
            return;
        }

        Properties properties = new Properties();

        try (Reader reader = Files.newBufferedReader(FILE)) {
            properties.load(reader);

            enabled = Boolean.parseBoolean(properties.getProperty("enabled", "true"));

            try {
                source = SoundSource.valueOf(properties.getProperty("source", SoundSource.MASTER.name()));
            } catch (IllegalArgumentException ignored) {
                source = SoundSource.MASTER;
            }
        } catch (IOException ignored) {}
    }

    public static void save() {
        Properties properties = new Properties();

        properties.setProperty("enabled", Boolean.toString(enabled));

        properties.setProperty("source", source.name());

        try {
            Files.createDirectories(FILE.getParent());

            try (Writer writer = Files.newBufferedWriter(FILE)) {
                properties.store(writer, "DeafBro");
            }
        } catch (IOException ignored) {}
    }
}
