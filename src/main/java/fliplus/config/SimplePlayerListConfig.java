package fliplus.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import fliplus.SimplePlayerList;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static fliplus.SimplePlayerList.MOD_ID;

public class SimplePlayerListConfig {
    private static final Path configDirectory = FabricLoader.getInstance().getConfigDir();
    private static final Path configFile = configDirectory.resolve(MOD_ID + ".json");

    private static final Gson GSON = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .create();

    public static Settings loadConfig() {
        return loadConfig(null);
    }

    public static Settings loadConfig(CommandSourceStack source) {
        Settings defaultConfig = new Settings();

        if (!Files.exists(configFile)) {
            saveConfig(defaultConfig);

            sendSuccessMessage(source);

            return defaultConfig;
        }

        try (FileReader reader = new FileReader(configFile.toFile())) {
            Settings config = GSON.fromJson(reader, Settings.class);
            if (config == null) config = defaultConfig;

            saveConfig(config);

            sendSuccessMessage(source);

            return config;
        } catch (IOException | JsonSyntaxException e) {
            String failedToLoadMessage = "Failed to load " + configFile.getFileName();
            String loadingDefaultConfigMessage = "Using default configuration instead";

            SimplePlayerList.LOGGER.warning(failedToLoadMessage + ":\n" + e.getMessage());
            SimplePlayerList.LOGGER.warning(loadingDefaultConfigMessage);

            if (source != null) {
                source.sendFailure(Component.literal(failedToLoadMessage));
                source.sendFailure(Component.literal(loadingDefaultConfigMessage));
            }

            return defaultConfig;
        }
    }

    private static void sendSuccessMessage(CommandSourceStack source) {
        if (source != null) {
            source.sendSuccess(
                () -> Component.literal("Successfully reloaded the configuration file")
                    .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY),
                true
            );
        }
    }

    public static void saveConfig(Settings config) {
        try {
            if (!Files.exists(configDirectory)) Files.createDirectories(configDirectory);
            Files.writeString(configFile, GSON.toJson(config));
        } catch (IOException e) {
            SimplePlayerList.LOGGER.warning("Failed to write configuration file:\n" + e.getMessage());
        }
    }
}
