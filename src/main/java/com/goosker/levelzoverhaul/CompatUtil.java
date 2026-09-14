package com.goosker.levelzoverhaul;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.class_1109;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_490;
import net.minecraft.class_3414;

/** Optional compatibility helpers for Flow and Satisfying Buttons. */
public final class CompatUtil {
    private CompatUtil() {}


    private static final class_2960 EXCALIBUR_ACTIVE_MARKER =
        new class_2960("minecraft:textures/gui/container/inventory_effects_chivalry.png");

    /** True only when the actual vanilla Inventory screen is currently open. */
    public static boolean isVanillaInventoryScreen() {
        try {
            class_310 client = class_310.method_1551();
            return client != null && client.field_1755 instanceof class_490;
        } catch (Throwable ignored) {
            return false;
        }
    }

    /**
     * Detects Excalibur from the live resource manager rather than the pack repository.
     * inventory_effects_chivalry.png is an Excalibur-specific resource, so it only
     * resolves while Excalibur is actually enabled and loaded.
     *
     * Important: ResourceManager is an interface in 1.20.1. The compile stub must also
     * be an interface so this call is emitted as invokeinterface rather than invokevirtual.
     */
    public static boolean isExcaliburResourcePackActive() {
        try {
            class_310 client = class_310.method_1551();
            return client != null
                && client.method_1478() != null
                && client.method_1478().method_14486(EXCALIBUR_ACTIVE_MARKER).isPresent();
        } catch (Throwable ignored) {
            return false;
        }
    }

    /** Brown/+3px tabs are exclusive to the Excalibur-skinned vanilla inventory. */
    public static boolean useExcaliburInventoryTabs() {
        return isVanillaInventoryScreen() && isExcaliburResourcePackActive();
    }

    /** LibZ tab x offset: only the Excalibur-skinned vanilla inventory uses the approved +3px position. */
    public static int tabXOffsetForCurrentScreen() {
        return useExcaliburInventoryTabs() ? 3 : 0;
    }

    /** Suppress Flow's next transition without disabling Flow globally. */
    public static void suppressNextFlowEaseIn() {
        try {
            Class<?> api = Class.forName("dev.imb11.flow.api.FlowAPI");
            Field disabled = api.getField("DISABLE_TEMPORARILY");
            if (!disabled.getBoolean(null)) {
                api.getMethod("toggleTemporaryDisable").invoke(null);
            }
        } catch (Throwable ignored) {
        }
    }

    /** Uses Satisfying Buttons' own sound events/config when that mod is present. */
    public static void satisfyingHoverChanged(boolean hovered, boolean wasHovered) {
        if (hovered == wasHovered) return;
        try {
            Class.forName("toni.satisfyingbuttons.SatisfyingButtonsClient");
            SatisfyingButtonsBridge.playHover(hovered);
        } catch (Throwable ignored) {
        }
    }

    /**
     * Vanilla ui.button.click by registry id.
     *
     * Do not depend on a mapped SoundEvents field here: this addon is compiled
     * against intermediary names, and the earlier field-based attempt resolved to
     * the wrong event in this 1.20.1 environment. Creating the SoundEvent from the
     * stable registry id keeps the Inventory <-> LevelZ and back-arrow click audible.
     */
    public static void playMenuClick() {
        try {
            class_3414 event = class_3414.method_47908(new class_2960("minecraft:ui.button.click"));
            class_310.method_1551().method_1483().method_4873(
                class_1109.method_4757(event, 1.0F, 0.25F)
            );
        } catch (Throwable ignored) {
        }
    }

    /** Reads AbstractWidget's hovered state from a LevelZ custom button. */
    public static boolean isHovered(Object widget) {
        try {
            Method method = widget.getClass().getMethod("method_49606");
            return Boolean.TRUE.equals(method.invoke(widget));
        } catch (Throwable ignored) {
            // Fallback to the inherited hover flag if mappings/method lookup changes.
            try {
                Field field = findField(widget.getClass(), "field_22766");
                if (field != null) {
                    field.setAccessible(true);
                    return field.getBoolean(widget);
                }
            } catch (Throwable ignoredToo) {
            }
            return false;
        }
    }

    /** Reads the inherited active flag; defaults true if mappings change. */
    public static boolean isActive(Object widget) {
        try {
            Field field = findField(widget.getClass(), "field_22763");
            if (field == null) return true;
            field.setAccessible(true);
            return field.getBoolean(widget);
        } catch (Throwable ignored) {
            return true;
        }
    }

    private static Field findField(Class<?> type, String name) {
        Class<?> current = type;
        while (current != null) {
            try {
                return current.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        return null;
    }
}
