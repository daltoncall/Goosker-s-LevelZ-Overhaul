package com.goosker.levelzoverhaul;

public final class BalanceValues {
    private BalanceValues() {}
    public static int level(int level) { return Math.max(0, Math.min(50, level)); }
    public static double toughness(int level) { return level(level) * 0.2D; }
    public static float exhaustionFactor(int level) { return 1.0F - level(level) / 50.0F; }
    public static double tradeDiscount(int level) { return level(level) * 1.9D; }
    public static float xpBonus(int level) { return level(level) / 50.0F; }
    public static float produceExtras(int level) { return level(level) * 0.12F; }
    public static int oreMultiplier(int level, float roll) {
        int units = level(level) * 2;
        return 1 + units / 50 + (roll < (units % 50) / 50.0F ? 1 : 0);
    }
    public static String oreDisplay(int level) {
        int units = level(level) * 2;
        int guaranteed = 1 + units / 50, percent = (units % 50) * 2;
        return (percent == 0 ? (units == 0 ? "0% x1" : "100% x" + guaranteed)
            : percent + "% x" + (guaranteed + 1)) + " Ore Drops";
    }
}
