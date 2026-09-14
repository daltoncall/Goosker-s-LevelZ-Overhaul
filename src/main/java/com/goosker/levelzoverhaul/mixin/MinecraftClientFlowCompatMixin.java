package com.goosker.levelzoverhaul.mixin;

import com.goosker.levelzoverhaul.CompatUtil;
import net.minecraft.class_310;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Keeps Flow normally, but disables the redundant animation only while tabbing Inventory <-> LevelZ. */
@Mixin(targets = "net.minecraft.class_310", remap = false)
public abstract class MinecraftClientFlowCompatMixin {
    @Shadow public class_437 field_1755;

    @Inject(method = "method_1507", at = @At("HEAD"), remap = false)
    private void goosker$flowCrossScreenCompat(class_437 nextScreen, CallbackInfo ci) {
        if (field_1755 == null || nextScreen == null) return;
        String current = field_1755.getClass().getName();
        String next = nextScreen.getClass().getName();

        boolean currentLevelZ = current.equals("net.levelz.screen.SkillScreen")
            || current.equals("net.levelz.screen.SkillInfoScreen");
        boolean nextLevelZ = next.equals("net.levelz.screen.SkillScreen")
            || next.equals("net.levelz.screen.SkillInfoScreen");
        boolean currentInventory = current.equals("net.minecraft.class_490");
        boolean nextInventory = next.equals("net.minecraft.class_490");

        if ((currentLevelZ && nextInventory) || (currentInventory && nextLevelZ)) {
            CompatUtil.suppressNextFlowEaseIn();
        }
    }
}
