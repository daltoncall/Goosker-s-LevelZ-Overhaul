package com.goosker.levelzoverhaul;

/** Simple data holder for player-facing skill bonus rows. Kept outside the Mixin package so Sponge Mixin does not treat it as a mixin class. */
public final class SkillBonusLine {
    public final String label;
    public final String value;
    public final int iconOrdinal;
    public final int valueColor;

    public SkillBonusLine(String label, String value, int iconOrdinal, int valueColor) {
        this.label = label;
        this.value = value;
        this.iconOrdinal = iconOrdinal;
        this.valueColor = valueColor;
    }
}
