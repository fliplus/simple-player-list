package fliplus.simpleplayerlist;

import fliplus.simpleplayerlist.config.Settings;
import fliplus.simpleplayerlist.config.SimplePlayerListConfig;
import fliplus.simpleplayerlist.server.TabList;

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
