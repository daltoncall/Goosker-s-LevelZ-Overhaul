package com.goosker.levelzoverhaul;

import net.levelz.stats.Skill;
import net.minecraft.class_2960;

/** Texture routing for Goosker's ten replacement skill icons. */
public final class SkillIconTextures {
    public static final class_2960 MAIN_ICON_BG =
        new class_2960("goosker_levelz_overhaul:textures/gui/skills/main_icon_bg.png");

    public static final class_2960 VIGOR = icon("vigor");
    public static final class_2960 VITALITY = icon("vitality");
    public static final class_2960 AGILITY = icon("agility");
    public static final class_2960 DEXTERITY = icon("dexterity");
    public static final class_2960 MAGICKA = icon("magicka");
    public static final class_2960 ARCHERY = icon("archery");
    public static final class_2960 BARTERING = icon("bartering");
    public static final class_2960 SMITHING = icon("smithing");
    public static final class_2960 MINING = icon("mining");
    public static final class_2960 HUSBANDRY = icon("husbandry");

    private static final class_2960[] DISPLAY_ORDER = {
        VIGOR, VITALITY, AGILITY, DEXTERITY, MAGICKA,
        ARCHERY, BARTERING, SMITHING, MINING, HUSBANDRY
    };

    private SkillIconTextures() {}

    private static class_2960 icon(String name) {
        return new class_2960("goosker_levelz_overhaul:textures/gui/skills/" + name + ".png");
    }

    public static class_2960 forDisplayIndex(int index) {
        return index >= 0 && index < DISPLAY_ORDER.length ? DISPLAY_ORDER[index] : VIGOR;
    }

    public static class_2960 forSkill(Skill skill) {
        return switch (skill) {
            case HEALTH -> VIGOR;
            case DEFENSE -> VITALITY;
            case AGILITY -> AGILITY;
            case STRENGTH -> DEXTERITY;
            case ALCHEMY -> MAGICKA;
            case ARCHERY -> ARCHERY;
            case TRADE -> BARTERING;
            case SMITHING -> SMITHING;
            case MINING -> MINING;
            case FARMING -> HUSBANDRY;
            default -> VIGOR;
        };
    }
}
