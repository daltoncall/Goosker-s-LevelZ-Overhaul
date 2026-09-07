package com.goosker.levelzoverhaul;

import net.levelz.access.PlayerStatsManagerAccess;
import net.levelz.stats.Skill;

public final class SkillUtil {
    private SkillUtil() {}

    public static int level(Object player, Skill skill) {
        if (player instanceof PlayerStatsManagerAccess access) {
            return access.getPlayerStatsManager().getSkillLevel(skill);
        }
        return 0;
    }

    /** Sets an EntityAttributeInstance base value without compiling against Minecraft mappings. */
    public static void setAttributeBase(Object livingEntity, String attributeField, double value) {
        try {
            Class<?> attrs = Class.forName("net.minecraft.class_5134");
            Object attribute = attrs.getField(attributeField).get(null);
            java.lang.reflect.Method getter = null;
            for (java.lang.reflect.Method m : livingEntity.getClass().getMethods()) {
                if (m.getName().equals("method_5996") && m.getParameterCount() == 1) {
                    getter = m;
                    break;
                }
            }
            if (getter == null) return;
            Object instance = getter.invoke(livingEntity, attribute);
            if (instance == null) return;
            java.lang.reflect.Method setter = instance.getClass().getMethod("method_6192", double.class);
            setter.invoke(instance, value);
        } catch (ReflectiveOperationException ignored) {
        }
    }
}
