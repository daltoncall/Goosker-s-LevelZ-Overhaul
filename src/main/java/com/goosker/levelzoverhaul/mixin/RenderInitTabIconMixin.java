package com.goosker.levelzoverhaul.mixin;

import net.minecraft.class_2960;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Replaces only LevelZ's two tab icon textures with the requested Excalibur assets. */
@Mixin(targets = "net.levelz.init.RenderInit", remap = false)
public abstract class RenderInitTabIconMixin {
    private static final class_2960 GOOSKER_INVENTORY_TAB =
        new class_2960("goosker_levelz_overhaul:textures/gui/tabs/inventory.png");
    private static final class_2960 GOOSKER_LEVELZ_TAB =
        new class_2960("goosker_levelz_overhaul:textures/gui/tabs/levelz.png");

    @Redirect(
        method = "init",
        at = @At(
            value = "FIELD",
            target = "Lnet/levelz/init/RenderInit;BAG_TAB_ICON:Lnet/minecraft/class_2960;"
        ),
        require = 1,
        remap = false
    )
    private static class_2960 goosker$inventoryTabIcon() {
        return GOOSKER_INVENTORY_TAB;
    }

    @Redirect(
        method = "init",
        at = @At(
            value = "FIELD",
            target = "Lnet/levelz/init/RenderInit;SKILL_TAB_ICON:Lnet/minecraft/class_2960;"
        ),
        require = 1,
        remap = false
    )
    private static class_2960 goosker$levelzTabIcon() {
        return GOOSKER_LEVELZ_TAB;
    }
}
