package com.example.mixins;

import com.example.ExampleMod;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class ExampleMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private static void init(CallbackInfo ci) {
        ExampleMod.LOGGER.info("This is a mixin!");
    }
}
