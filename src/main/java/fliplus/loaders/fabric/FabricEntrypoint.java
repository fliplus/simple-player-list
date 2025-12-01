//? if fabric {
package fliplus.loaders.fabric;

import fliplus.SimplePlayerList;
import net.fabricmc.api.ModInitializer;

public class FabricEntrypoint implements ModInitializer {

    @Override
    public void onInitialize() {
        SimplePlayerList.initialize();
    }
}
//?}