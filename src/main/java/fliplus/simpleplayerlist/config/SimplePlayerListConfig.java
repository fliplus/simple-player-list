package fliplus.simpleplayerlist.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static fliplus.simpleplayerlist.SimplePlayerList.LOGGER;
import static fliplus.simpleplayerlist.SimplePlayerList.MOD_ID;

public class SimplePlayerListConfig {
    private static final Path path = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json");
    private static final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

    public static SimplePlayerListConfig INSTANCE = new SimplePlayerListConfig();
    public static List<ServerPlayer> disabledPlayers = new ArrayList<>();

    public boolean enableMod;
    public String header;
    public String footer;
    public int tickUpdateInterval;

    private void loadDefaults() {
        enableMod = true;
        header = "§lDefault Config§r\n\n§7To change this, edit\nconfig/simpleplayerlist.json\n\n§r§b§m                                        §r";
        footer = "§r§b§m                                        §r\n\n§7TPS: %tps%§7/%mspt% §8-§7 Ping: %ping%\n§7Day: §b%day%";
        tickUpdateInterval = 20;
    }

    public void load(CommandSourceStack source) {
        if (!Files.exists(path)) {
            loadDefaults();
            create(INSTANCE);
            return;
        }

        try (FileReader file = new FileReader(path.toFile())) {
            SimplePlayerListConfig config = gson.fromJson(file, SimplePlayerListConfig.class);

            boolean firstLoad = this.header == null;
            if (isValidConfig(config)) {
                this.enableMod = config.enableMod;
                this.header = config.header;
                this.footer = config.footer;
                this.tickUpdateInterval = config.tickUpdateInterval;

                LOGGER.info("Config {}loaded", firstLoad ? "" : "re");
                if (!(source == null)) {
                    String message = Objects.requireNonNull(source.getPlayer()).getName().getString() + " reloaded player list config";
                    source.sendSuccess(() -> Component.literal(message), true);
                }
            } else {
                String message = "Config is missing required fields, loading default config";
                LOGGER.error(message);
                if (!(source == null)) {
                    source.sendFailure(Component.literal(message));
                }
                loadDefaults();
            }
        } catch (JsonSyntaxException | IOException e) {
            String message = "Failed to load config due to syntax error or IO exception, loading default config";
            LOGGER.error(message, e);
            if (!(source == null)) {
                source.sendFailure(Component.literal(message));
            }
            loadDefaults();
        }
    }

    private void create(SimplePlayerListConfig config) {
        try (FileWriter file = new FileWriter(path.toFile())) {
            file.write(gson.toJson(config));

            LOGGER.info("Config created");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidConfig(SimplePlayerListConfig config) {
        return config != null && config.header != null && config.footer != null;
    }
}
