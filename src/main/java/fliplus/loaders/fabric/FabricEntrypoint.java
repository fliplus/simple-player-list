//? if fabric {
package fliplus.loaders.fabric;

import fliplus.SimplePlayerList;
import fliplus.commands.SimplePlayerListCommand;
import fliplus.server.TabList;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class FabricEntrypoint implements ModInitializer {

    @Override
    public void onInitialize() {
        SimplePlayerList.initialize();
        ServerTickEvents.END_SERVER_TICK.register(TabList::tick);
        CommandRegistrationCallback.EVENT.register((dispatcher, context, selection) ->
            SimplePlayerListCommand.register(dispatcher)
        );
        ServerPlayerEvents.JOIN.register(TabList::updateTabList);
    }
}
//?}