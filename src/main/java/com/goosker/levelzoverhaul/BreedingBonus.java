package com.goosker.levelzoverhaul;

/** Each ten Husbandry levels guarantees one extra baby, with a fractional next baby. */
public final class BreedingBonus {
    private BreedingBonus() {}
    public static int clamp(int level) { return Math.max(0, Math.min(50, level)); }
    public static int guaranteed(int level) { return clamp(level) / 10; }
    public static int chancePercent(int level) { return (clamp(level) % 10) * 10; }
    public static int extraCount(int level, float roll) {
        return guaranteed(level) + (roll < chancePercent(level) / 100.0F ? 1 : 0);
    }
    public static String display(int level) {
        int chance = chancePercent(level);
        int count = guaranteed(level);
        if (chance == 0) return (count == 0 ? "0% x0" : "100% x" + count) + " Spawn";
        return chance + "% x" + (count + 1) + " Spawn";
    }
}
