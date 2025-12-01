//? if fabric {
package com.example.loaders.fabric;

import com.example.ExampleMod;
import net.fabricmc.api.ModInitializer;

public class FabricEntrypoint implements ModInitializer {

    @Override
    public void onInitialize() {
        ExampleMod.initialize();
    }
}
//?}