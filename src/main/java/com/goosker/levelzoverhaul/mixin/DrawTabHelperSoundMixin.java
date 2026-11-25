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


    private static final java.util.Map<class_437, net.libz.api.InventoryTab> GOOSKER_HOVERED_TABS =
        new java.util.WeakHashMap<>();

    @Inject(method = "drawTab", at = @At("RETURN"), remap = false)
    private static void goosker$allTabHoverSounds(class_310 client, class_332 context,
        class_437 screen, int x, int y, int mouseX, int mouseY, CallbackInfo ci) {
        net.libz.api.InventoryTab hovered = null;
        if (client != null && client.field_1724 != null && net.libz.init.ConfigInit.CONFIG.inventoryButton
                && screen instanceof net.libz.api.Tab tabScreen) {
            Class<?> parent = tabScreen.getParentScreenClass();
            java.util.List<net.libz.api.InventoryTab> tabs = parent == null
                ? net.libz.LibzClient.inventoryTabs : net.libz.LibzClient.otherTabs.get(parent);
            int tabX = x;
            if (tabs != null) for (net.libz.api.InventoryTab tab : tabs) {
                if (!tab.shouldShow(client)) continue;
                boolean selected = tab.isSelectedScreen(screen.getClass());
                int top = selected ? y - 25 : y - 21;
                int bottom = y;
                if (!selected && tab.canClick(screen.getClass(), client)
                        && mouseX >= tabX && mouseX < tabX + 24 && mouseY >= top && mouseY < bottom) {
                    hovered = tab;
                }
                tabX += 25;
            }
        }
        net.libz.api.InventoryTab previous = GOOSKER_HOVERED_TABS.get(screen);
        if (previous != hovered) {
            if (previous != null) CompatUtil.satisfyingHoverChanged(false, true);
            if (hovered != null) CompatUtil.satisfyingHoverChanged(true, false);
        }
        if (hovered == null) GOOSKER_HOVERED_TABS.remove(screen);
        else GOOSKER_HOVERED_TABS.put(screen, hovered);
    }

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

    @Redirect(method = "drawTab", at = @At(value = "INVOKE",
        target = "Lnet/libz/api/InventoryTab;getTitle()Lnet/minecraft/class_2561;"),
        require = 1, remap = false)
    private static net.minecraft.class_2561 goosker$inventoryTitle(net.libz.api.InventoryTab tab) {
        return tab instanceof net.levelz.screen.widget.VanillaInventoryTab
            ? net.minecraft.class_2561.method_30163("Inventory") : tab.getTitle();
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
