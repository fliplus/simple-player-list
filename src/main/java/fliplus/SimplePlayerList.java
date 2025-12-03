package fliplus;

import fliplus.config.Settings;
import fliplus.config.SimplePlayerListConfig;
import fliplus.server.TabList;

import java.util.logging.Logger;

public class SimplePlayerList {
    public static final String MOD_ID = "simpleplayerlist";
    public static final Logger LOGGER = Logger.getLogger(MOD_ID);

    public static Settings CONFIG;

    public static void initialize() {
        CONFIG = SimplePlayerListConfig.loadConfig();
        TabList.registerPlaceholders();
    }
}
