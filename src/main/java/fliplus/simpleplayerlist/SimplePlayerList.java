package fliplus.simpleplayerlist;

import fliplus.simpleplayerlist.commands.PlayerListCommand;
import fliplus.simpleplayerlist.config.SimplePlayerListConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimplePlayerList implements ModInitializer {
    public static final String MOD_ID = "simpleplayerlist";
	public static final Logger LOGGER = LoggerFactory.getLogger("simpleplayerlist");

	@Override
	public void onInitialize() {
		SimplePlayerListConfig.INSTANCE.load(null);
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> PlayerListCommand.register(dispatcher)));
	}
}