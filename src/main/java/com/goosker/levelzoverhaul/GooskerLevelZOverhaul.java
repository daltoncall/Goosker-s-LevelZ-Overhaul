package com.goosker.levelzoverhaul;

import net.fabricmc.api.ModInitializer;
import net.levelz.config.LevelzConfig;
import net.levelz.init.ConfigInit;

/** Mechanical half of Goosker's ten-skill LevelZ overhaul. */
public final class GooskerLevelZOverhaul implements ModInitializer {
    public static final String MOD_ID = "goosker_levelz_overhaul";

    @Override
    public void onInitialize() {
        LevelzConfig c = ConfigInit.CONFIG;
        if (c == null) return;

        // 10 active skills x 50 = 500 total.
        c.maxLevel = 50;
        c.overallMaxLevel = 500;
        c.allowHigherSkillLevel = false;
        c.pointsPerLevel = 1;

        // HEALTH -> Vigor: +1 HP/level = +25 hearts at 50.
        c.healthBase = 20.0D;
        c.healthBonus = 1.0D;
        c.healthAbsorptionBonus = 0.0F;

        // STRENGTH -> Dexterity: +5 melee damage at 50.
        c.attackBase = 1.0D;
        c.attackBonus = 0.1D;
        c.attackDoubleDamageChance = 0.0F;
        c.attackCritDmgBonus = 0.0F;

        // AGILITY -> Agility: about +20% movement speed at 50.
        c.movementBase = 0.1D;
        c.movementBonus = 0.0004D;
        c.movementMissChance = 0.0F;
        c.movementFallBonus = 0.0F;

        // DEFENSE -> Vitality: +5 armor at 50; toughness is addon-handled.
        c.defenseBase = 0.0D;
        c.defenseBonus = 0.1D;
        c.defenseReflectChance = 0.0F;

        // Retired skills.
        c.luckBase = 0.0D;
        c.luckBonus = 0.0D;
        c.luckCritBonus = 0.0F;
        c.luckSurviveChance = 0.0F;
        c.staminaBase = 1.0F;
        c.staminaBonus = 0.0F;
        c.staminaHealthBonus = 0.0F;
        c.staminaFoodBonus = 0.0F;

        // ARCHERY -> Archery: +5 bow/crossbow damage at 50.
        c.archeryInaccuracyBonus = 0.0F;
        c.archeryBowExtraDamage = 0.1F;
        c.archeryCrossbowExtraDamage = 0.1F;
        c.archeryDoubleDamageChance = 0.0F;

        // TRADE -> Bartering: 36% discount at 50 and +200% trade XP (3x total).
        c.tradeBonus = 0.72D;
        c.tradeXPBonus = 0.04F;
        c.tradeReputation = true;

        // SMITHING -> Smithing: 100% durability conservation and 50% anvil discount at 50.
        c.smithingToolChance = 0.02F;
        c.smithingCostBonus = 0.01F;

        // MINING -> Mining: 50% LevelZ bonus ore-drop chance at 50.
        c.miningOreChance = 0.01F;
        c.miningTntBonus = 0.0F;
        c.miningLockedMultiplicator = 0.0F;

        // FARMING -> Husbandry: 100% extra tagged plant drop + 100% twin chance at 50.
        c.farmingBase = 1;
        c.farmingChanceBonus = 0.0F; // custom crop-yield scaling is addon-handled
        c.farmingTwinChance = 1.0F; // LevelZ checks this at max Husbandry: guaranteed twin at 50
        c.breedingXPMultiplier = 2.0F; // modestly reward breeding as a progression activity

        // ALCHEMY -> Magicka: native potion amplifier chance; magic damage is addon-handled.
        c.alchemyEnchantmentChance = 0.0F;
        c.alchemyPotionChance = 0.02F;

        // Keep progression additive / Vanilla+ rather than gating vanilla play.
        c.miningProgression = false;
        c.itemProgression = false;
        c.blockProgression = false;
        c.entityProgression = false;
        c.brewingProgression = false;
        c.smithingProgression = false;
    }
}
