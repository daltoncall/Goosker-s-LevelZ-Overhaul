package com.goosker.levelzoverhaul;

import net.levelz.stats.Skill;
import net.minecraft.class_2960;

/** Texture routing for the skill-detail current-bonus icons. */
public final class BonusIconTextures {
    public static final class_2960 VIGOR_HEALTH = icon("vigor_health");
    public static final class_2960 VITALITY_ARMOR = icon("vitality_armor");
    public static final class_2960 VITALITY_TOUGHNESS = icon("vitality_toughness");
    public static final class_2960 AGILITY_SPEED = icon("agility_speed");
    public static final class_2960 AGILITY_EXHAUSTION = icon("agility_exhaustion");
    public static final class_2960 AGILITY_SAFE_FALL = icon("agility_safe_fall");
    public static final class_2960 DEXTERITY_MELEE = icon("dexterity_melee");
    public static final class_2960 ARCHERY_BOW = icon("archery_bow");
    public static final class_2960 ARCHERY_CROSSBOW = icon("archery_crossbow");
    public static final class_2960 BARTERING_DISCOUNT = icon("bartering_discount");
    public static final class_2960 XP = icon("xp");
    public static final class_2960 SMITHING_DURABILITY = icon("smithing_durability");
    public static final class_2960 SMITHING_ANVIL = icon("smithing_anvil");
    public static final class_2960 MINING_SPEED = icon("mining_speed");
    public static final class_2960 MINING_BONUS_ORE = icon("mining_bonus_ore");
    public static final class_2960 HUSBANDRY_PRODUCE = icon("husbandry_produce");
    public static final class_2960 HUSBANDRY_TWINS = icon("husbandry_twins");
    public static final class_2960 MAGICKA_DAMAGE = icon("magicka_damage");
    public static final class_2960 MAGICKA_POTION = icon("magicka_potion");

    private BonusIconTextures() {}

    private static class_2960 icon(String name) {
        return new class_2960("goosker_levelz_overhaul:textures/gui/bonuses/" + name + ".png");
    }

    public static class_2960 forBonus(Skill skill, int index) {
        return switch (skill) {
            case HEALTH -> VIGOR_HEALTH;
            case DEFENSE -> index == 0 ? VITALITY_ARMOR : VITALITY_TOUGHNESS;
            case AGILITY -> switch (index) {
                case 0 -> AGILITY_SPEED;
                case 1 -> AGILITY_EXHAUSTION;
                default -> AGILITY_SAFE_FALL;
            };
            case STRENGTH -> DEXTERITY_MELEE;
            case ARCHERY -> index == 0 ? ARCHERY_BOW : ARCHERY_CROSSBOW;
            case TRADE -> index == 0 ? BARTERING_DISCOUNT : XP;
            case SMITHING -> index == 0 ? SMITHING_DURABILITY : SMITHING_ANVIL;
            case MINING -> index == 0 ? MINING_SPEED : MINING_BONUS_ORE;
            case FARMING -> switch (index) {
                case 0 -> HUSBANDRY_PRODUCE;
                case 1 -> HUSBANDRY_TWINS;
                default -> XP;
            };
            case ALCHEMY -> index == 0 ? MAGICKA_DAMAGE : MAGICKA_POTION;
            default -> VIGOR_HEALTH;
        };
    }
}
