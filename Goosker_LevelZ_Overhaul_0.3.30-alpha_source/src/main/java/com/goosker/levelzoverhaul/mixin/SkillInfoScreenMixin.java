package com.goosker.levelzoverhaul.mixin;

import java.util.Locale;
import com.goosker.levelzoverhaul.CompatUtil;
import com.goosker.levelzoverhaul.BonusIconTextures;
import com.goosker.levelzoverhaul.SkillBonusLine;
import com.goosker.levelzoverhaul.SkillIconTextures;
import net.levelz.access.PlayerStatsManagerAccess;
import net.levelz.screen.SkillScreen;
import net.levelz.stats.PlayerStatsManager;
import net.levelz.stats.Skill;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.libz.util.DrawTabHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Goosker's compact, player-facing skill sheet. */
@Mixin(targets = "net.levelz.screen.SkillInfoScreen", remap = false)
public abstract class SkillInfoScreenMixin {
    private static final class_2960 GOOSKER_BACKGROUND =
        new class_2960("goosker_levelz_overhaul:textures/gui/skill_info_background.png");
    private static final class_2960 GOOSKER_RULE =
        new class_2960("goosker_levelz_overhaul:textures/gui/section_rule.png");
    private static final class_2960 GOOSKER_BACK_BUTTON =
        new class_2960("goosker_levelz_overhaul:textures/gui/back_button.png");

    private static final int WHITE = 0xF2F2F2;
    private static final int GOLD = 0xF2D36B;
    private static final int GREEN = 0x55FF55;
    private static final int MUTED = 0xB8B8B8;

    private boolean goosker$backHovered;
    private boolean goosker$inventoryTabHovered;

    @Shadow private int x;
    @Shadow private int y;
    @Shadow private int backgroundWidth;
    @Shadow private int backgroundHeight;
    @Shadow @Final private String title;

    @Inject(method = "method_25394", at = @At("HEAD"), cancellable = true, remap = false)
    private void goosker$renderSkillInfo(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        class_310 client = class_310.method_1551();
        class_327 text = client.field_1772;

        context.method_25290(GOOSKER_BACKGROUND,
            x, y, 0.0F, 0.0F, backgroundWidth, backgroundHeight, 256, 256);

        // Half-size version of the original Minecraft-style beveled back button.
        int backX = x + backgroundWidth - 20;
        int backY = y + 8;
        context.method_25290(GOOSKER_BACK_BUTTON, backX, backY, 0.0F, 0.0F, 12, 12, 12, 12);

        Skill skill = goosker$skill();
        int level = 0;
        if (client.field_1724 instanceof PlayerStatsManagerAccess access) {
            PlayerStatsManager manager = access.getPlayerStatsManager();
            level = manager.getSkillLevel(skill);
        }

        // SkillInfoScreen is fully custom-rendered, so use the replacement icon
        // directly rather than LevelZ's legacy icon sheet.
        context.method_25290(SkillIconTextures.forSkill(skill),
            x + 11, y + 11, 0.0F, 0.0F, 18, 18, 18, 18);

        goosker$draw(context, text, goosker$name(skill), x + 36, y + 16, WHITE);

        // Match the approved mockup: only the player's current skill level is gold.
        int levelX = x + 12;
        int end = goosker$drawEnd(context, text, "Level ", levelX, y + 44, WHITE);
        end = goosker$drawEnd(context, text, String.valueOf(level), end, y + 44, GOLD);
        goosker$draw(context, text, " / 50", end, y + 44, WHITE);

        goosker$rule(context, x + 10, y + 64);

        String[] description = goosker$description(skill);
        int lineY = y + 73;
        for (String line : description) {
            goosker$draw(context, text, line, x + 12, lineY, WHITE);
            lineY += 11;
        }

        int secondRuleY = lineY + 5;
        goosker$rule(context, x + 10, secondRuleY);

        lineY = secondRuleY + 13;
        goosker$draw(context, text, "Current Bonuses", x + 12, lineY, GOLD);
        lineY += 17;

        int bonusIndex = 0;
        for (SkillBonusLine bonus : goosker$bonuses(skill, level)) {
            if (skill == Skill.SMITHING && bonusIndex == 1) {
                // Render Minecraft's actual anvil item so Excalibur supplies its native 3D item icon when active.
                context.method_51445(new class_1799(class_1802.field_8782), x + 13, lineY - 3);
            } else {
                context.method_25290(BonusIconTextures.forBonus(skill, bonusIndex),
                    x + 12, lineY - 4, 0.0F, 0.0F, 18, 18, 18, 18);
            }

            int valueX = goosker$drawEnd(context, text, bonus.label, x + 34, lineY, WHITE);
            goosker$draw(context, text, bonus.value, valueX, lineY, bonus.valueColor);
            lineY += 18;
            bonusIndex++;
        }

        if (lineY + 4 < y + backgroundHeight - 8) {
            goosker$rule(context, x + 10, lineY + 3);
        }

        boolean backHovered = goosker$isBackButton(mouseX, mouseY);
        CompatUtil.satisfyingHoverChanged(backHovered, goosker$backHovered);
        goosker$backHovered = backHovered;

        boolean inventoryHovered = goosker$isInventoryTab(mouseX, mouseY);
        CompatUtil.satisfyingHoverChanged(inventoryHovered, goosker$inventoryTabHovered);
        goosker$inventoryTabHovered = inventoryHovered;

        DrawTabHelper.drawTab(client, context, (class_437) (Object) this, x, y, mouseX, mouseY);
        ci.cancel();
    }

    @Inject(method = "method_25402", at = @At("HEAD"), cancellable = true, remap = false)
    private void goosker$backToSkills(double mouseX, double mouseY, int button,
                                      CallbackInfoReturnable<Boolean> cir) {
        if (button == 0 && goosker$isBackButton(mouseX, mouseY)) {
            class_310 client = class_310.method_1551();
            CompatUtil.playMenuClick();
            client.method_1507(new SkillScreen());
            cir.setReturnValue(true);
        }
    }

    private static void goosker$draw(class_332 context, class_327 renderer, String value, int x, int y, int color) {
        context.method_51433(renderer, value, x, y, color, false);
    }

    /** Works whether DrawContext returns text width or final x position. */
    private static int goosker$drawEnd(class_332 context, class_327 renderer, String value, int x, int y, int color) {
        int result = context.method_51433(renderer, value, x, y, color, false);
        return result > x ? result : x + result;
    }

    private static void goosker$rule(class_332 context, int x, int y) {
        context.method_25290(GOOSKER_RULE, x, y, 0.0F, 0.0F, 180, 7, 180, 7);
    }

    private boolean goosker$isBackButton(double mouseX, double mouseY) {
        int backX = x + backgroundWidth - 20;
        int backY = y + 8;
        // Keep a comfortable 16x16 interaction area even though the art is only 12x12.
        return mouseX >= backX - 2 && mouseX < backX + 14 && mouseY >= backY - 2 && mouseY < backY + 14;
    }

    private boolean goosker$isInventoryTab(double mouseX, double mouseY) {
        return mouseX >= x && mouseX < x + 24 && mouseY >= y - 23 && mouseY < y + 4;
    }

    private Skill goosker$skill() {
        return switch (title) {
            case "health" -> Skill.HEALTH;
            case "strength" -> Skill.STRENGTH;
            case "agility" -> Skill.AGILITY;
            case "defense" -> Skill.DEFENSE;
            case "archery" -> Skill.ARCHERY;
            case "trade" -> Skill.TRADE;
            case "smithing" -> Skill.SMITHING;
            case "mining" -> Skill.MINING;
            case "farming" -> Skill.FARMING;
            case "alchemy" -> Skill.ALCHEMY;
            default -> Skill.HEALTH;
        };
    }

    private static int goosker$iconOrdinal(Skill skill) {
        return switch (skill) {
            case HEALTH -> 0;
            case STRENGTH -> 1;
            case AGILITY -> 2;
            case DEFENSE -> 3;
            case ARCHERY -> 6;
            case TRADE -> 7;
            case SMITHING -> 8;
            case MINING -> 9;
            case FARMING -> 10;
            case ALCHEMY -> 11;
            default -> 0;
        };
    }

    private static String goosker$name(Skill skill) {
        return switch (skill) {
            case HEALTH -> "Vigor";
            case STRENGTH -> "Dexterity";
            case AGILITY -> "Agility";
            case DEFENSE -> "Vitality";
            case ARCHERY -> "Archery";
            case TRADE -> "Bartering";
            case SMITHING -> "Smithing";
            case MINING -> "Mining";
            case FARMING -> "Husbandry";
            case ALCHEMY -> "Magicka";
            default -> skill.name();
        };
    }

    private static String[] goosker$description(Skill skill) {
        return switch (skill) {
            case HEALTH -> new String[]{
                "Increases your maximum health."
            };
            case STRENGTH -> new String[]{
                "Increases melee weapon damage,",
                "including compatible staffs."
            };
            case AGILITY -> new String[]{
                "Improves movement speed, reduces",
                "exhaustion, and increases safe",
                "fall distance."
            };
            case DEFENSE -> new String[]{
                "Increases armor and armor",
                "toughness."
            };
            case ARCHERY -> new String[]{
                "Increases bow and crossbow",
                "damage."
            };
            case TRADE -> new String[]{
                "Reduces villager prices and",
                "increases XP earned from trades."
            };
            case SMITHING -> new String[]{
                "Reduces equipment wear and",
                "anvil XP costs."
            };
            case MINING -> new String[]{
                "Improves mining speed and gives",
                "a chance for bonus ore drops."
            };
            case FARMING -> new String[]{
                "Improves crop yields and animal",
                "breeding, including twins and",
                "increased breeding XP."
            };
            case ALCHEMY -> new String[]{
                "Improves magic staff damage and",
                "the chance for potions to gain",
                "a stronger effect."
            };
            default -> new String[]{""};
        };
    }

    private static SkillBonusLine[] goosker$bonuses(Skill skill, int level) {
        return switch (skill) {
            case HEALTH -> new SkillBonusLine[]{
                new SkillBonusLine("Max Health: ", "+" + goosker$one(level * 0.5) + " hearts", 0, GREEN)
            };
            case STRENGTH -> new SkillBonusLine[]{
                new SkillBonusLine("Melee Damage: ", "+" + goosker$one(level * 0.1), 1, GREEN)
            };
            case AGILITY -> new SkillBonusLine[]{
                new SkillBonusLine("Movement Speed: ", "+" + goosker$percent(level * 0.4) + "%", 2, GREEN),
                new SkillBonusLine("Exhaustion Used: ", "-" + goosker$percent(level * 0.8) + "%", 2, GREEN),
                new SkillBonusLine("Safe Fall: ", "+" + goosker$one(level * (22.0 / 50.0)) + " blocks", 2, GREEN)
            };
            case DEFENSE -> new SkillBonusLine[]{
                new SkillBonusLine("Armor: ", "+" + goosker$one(level * 0.1), 3, GREEN),
                new SkillBonusLine("Armor Toughness: ", "+" + goosker$two(level * 0.08), 3, GREEN)
            };
            case ARCHERY -> new SkillBonusLine[]{
                new SkillBonusLine("Bow Damage: ", "+" + goosker$one(level * 0.1), 6, GREEN),
                new SkillBonusLine("Crossbow Damage: ", "+" + goosker$one(level * 0.1), 6, GREEN)
            };
            case TRADE -> new SkillBonusLine[]{
                new SkillBonusLine("Trade Discount: ", goosker$percent(level * 0.72) + "%", 7, GREEN),
                new SkillBonusLine("Trade XP: ", "+" + goosker$percent(level * 4.0) + "%", 7, GREEN)
            };
            case SMITHING -> new SkillBonusLine[]{
                new SkillBonusLine("Durability Saved: ", goosker$percent(level * 2.0) + "%", 8, GREEN),
                new SkillBonusLine("Anvil XP Discount: ", goosker$percent(level * 1.0) + "%", 8, GREEN)
            };
            case MINING -> new SkillBonusLine[]{
                new SkillBonusLine("Mining Speed: ", "+" + goosker$percent(level * 1.0) + "%", 9, GREEN),
                new SkillBonusLine("Bonus Ore Chance: ", goosker$percent(level * 1.0) + "%", 9, GREEN)
            };
            case FARMING -> new SkillBonusLine[]{
                new SkillBonusLine("Avg. Bonus Produce: ", "+" + goosker$two(level * (5.5 / 50.0)), 10, GREEN),
                new SkillBonusLine("Twin Chance: ", level >= 50 ? "100%" : "Locked until Lv. 50", 0, level >= 50 ? GREEN : MUTED),
                new SkillBonusLine("Breeding XP: ", "2x", 11, GREEN)
            };
            case ALCHEMY -> new SkillBonusLine[]{
                new SkillBonusLine("Magic Damage: ", "+" + goosker$one(level * 0.1), 11, GREEN),
                new SkillBonusLine("Potion Boost Chance: ", goosker$percent(level * 2.0) + "%", 11, GREEN)
            };
            default -> new SkillBonusLine[]{};
        };
    }

    private static String goosker$one(double value) {
        return String.format(Locale.ROOT, "%.1f", value);
    }

    private static String goosker$two(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }

    private static String goosker$percent(double value) {
        return String.format(Locale.ROOT, value == Math.rint(value) ? "%.0f" : "%.1f", value);
    }
}
