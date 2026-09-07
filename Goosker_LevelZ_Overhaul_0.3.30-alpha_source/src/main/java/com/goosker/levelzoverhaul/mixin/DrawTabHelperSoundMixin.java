package com.goosker.levelzoverhaul.mixin;

import com.goosker.levelzoverhaul.CompatUtil;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Adds a single UI click exactly when LibZ commits a tab switch. */
@Mixin(targets = "net.libz.util.DrawTabHelper", remap = false)
public abstract class DrawTabHelperSoundMixin {
    private static final class_2960 GOOSKER_EXCALIBUR_TABS =
        new class_2960("goosker_levelz_overhaul:textures/gui/tabs/excalibur_tabs.png");
    private static final class_2960 GOOSKER_INVENTORY_TAB_OPEN =
        new class_2960("goosker_levelz_overhaul:textures/gui/tabs/inventory.png");
    private static final class_2960 GOOSKER_INVENTORY_TAB_CLOSED =
        new class_2960("goosker_levelz_overhaul:textures/gui/tabs/inventory_closed.png");


    @ModifyVariable(method = "drawTab", at = @At("HEAD"), argsOnly = true, ordinal = 0, require = 1, remap = false)
    private static int goosker$shiftTabDrawX(int x) {
        return x + CompatUtil.tabXOffsetForCurrentScreen();
    }

    @ModifyVariable(method = "onTabButtonClick", at = @At("HEAD"), argsOnly = true, ordinal = 0, require = 1, remap = false)
    private static int goosker$shiftTabClickX(int x) {
        return x + CompatUtil.tabXOffsetForCurrentScreen();
    }

    @Redirect(
        method = "drawTab",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/class_332;method_25302(Lnet/minecraft/class_2960;IIIIII)V"
        ),
        require = 1,
        remap = false
    )
    private static void goosker$excaliburInventoryTabBackground(class_332 context, class_2960 texture,
                                                                 int x, int y, int u, int v,
                                                                 int width, int height) {
        context.method_25302(CompatUtil.useExcaliburInventoryTabs() ? GOOSKER_EXCALIBUR_TABS : texture,
            x, y, u, v, width, height);
    }
    @Redirect(
        method = "drawTab",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/class_332;method_25290(Lnet/minecraft/class_2960;IIFFIIII)V"
        ),
        require = 1,
        remap = false
    )
    private static void goosker$contextualInventoryTabIcon(class_332 context, class_2960 texture,
                                                             int x, int y, float u, float v,
                                                             int width, int height,
                                                             int textureWidth, int textureHeight) {
        class_2960 resolvedTexture = texture;
        if (GOOSKER_INVENTORY_TAB_OPEN.equals(texture) && !CompatUtil.isVanillaInventoryScreen()) {
            resolvedTexture = GOOSKER_INVENTORY_TAB_CLOSED;
        }
        context.method_25290(resolvedTexture, x, y, u, v, width, height, textureWidth, textureHeight);
    }

    @Inject(
        method = "onTabButtonClick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/libz/api/InventoryTab;onClick(Lnet/minecraft/class_310;)V",
            shift = At.Shift.BEFORE,
            remap = false
        ),
        require = 1,
        remap = false
    )
    private static void goosker$playTabSwitchClick(class_310 client, class_437 screenClass,
                                                    int x, int y, double mouseX, double mouseY,
                                                    boolean focused, CallbackInfo ci) {
        CompatUtil.playMenuClick();
    }
}
