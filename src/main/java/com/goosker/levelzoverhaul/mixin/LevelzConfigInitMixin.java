package com.goosker.levelzoverhaul.mixin;

import com.goosker.levelzoverhaul.GooskerLevelZOverhaul;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** LevelZ may initialize after the addon, replacing the config object we first saw. */
@Mixin(targets = "net.levelz.init.ConfigInit", remap = false)
public abstract class LevelzConfigInitMixin {
    @Inject(method = "init", at = @At("TAIL"), remap = false)
    private static void goosker$applyAfterConfigLoad(CallbackInfo ci) {
        GooskerLevelZOverhaul.applyBonuses();
    }
}
