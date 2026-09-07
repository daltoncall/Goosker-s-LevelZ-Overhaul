package com.goosker.levelzoverhaul.mixin;

import com.goosker.levelzoverhaul.CompatUtil;
import com.goosker.levelzoverhaul.SkillIconTextures;
import net.levelz.stats.Skill;
import net.minecraft.class_2561;
import net.minecraft.class_5250;
import net.minecraft.class_332;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_4587;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.levelz.screen.SkillScreen", remap = false)
public abstract class SkillScreenMixin {

    @Shadow private int x;
    @Shadow private int y;
    @Shadow private net.levelz.stats.PlayerStatsManager playerStatsManager;

    private boolean goosker$inventoryTabHovered;

    // Reflow the ten active skills from LevelZ's original 6/6 grid into 5/5.
    @ModifyConstant(method = "method_25426", constant = @Constant(intValue = 5, ordinal = 0), remap = false)
    private int goosker$skillColumnBreak0(int original) { return 4; }
    @ModifyConstant(method = "method_25426", constant = @Constant(intValue = 5, ordinal = 1), remap = false)
    private int goosker$skillColumnBreak1(int original) { return 4; }
    @ModifyConstant(method = "method_25426", constant = @Constant(intValue = 5, ordinal = 2), remap = false)
    private int goosker$levelColumnBreak0(int original) { return 4; }
    @ModifyConstant(method = "method_25426", constant = @Constant(intValue = 5, ordinal = 3), remap = false)
    private int goosker$levelColumnBreak1(int original) { return 4; }
    @ModifyConstant(method = "method_25426", constant = @Constant(intValue = 120, ordinal = 0), remap = false)
    private int goosker$skillSecondColumnYOffset(int original) { return 125; }
    @ModifyConstant(method = "method_25426", constant = @Constant(intValue = 120, ordinal = 1), remap = false)
    private int goosker$levelSecondColumnYOffset(int original) { return 125; }

    // Two lines of text do not fit cleanly in LevelZ's original 20px rows.
    // 24px rows give the icon/name/value proper breathing room while still fitting five rows.
    @ModifyConstant(method = "method_25426", constant = @Constant(intValue = 20, ordinal = 0), remap = false)
    private int goosker$skillRowSpacing(int original) { return 25; }
    @ModifyConstant(method = "method_25426", constant = @Constant(intValue = 20, ordinal = 1), remap = false)
    private int goosker$levelRowSpacing(int original) { return 25; }

    @ModifyConstant(method = "method_25394", constant = @Constant(intValue = 5, ordinal = 2), remap = false)
    private int goosker$renderColumnBreak0(int original) { return 4; }
    @ModifyConstant(method = "method_25394", constant = @Constant(intValue = 5, ordinal = 3), remap = false)
    private int goosker$renderColumnBreak1(int original) { return 4; }
    @ModifyConstant(method = "method_25394", constant = @Constant(intValue = 120, ordinal = 1), remap = false)
    private int goosker$renderSecondColumnYOffset(int original) { return 100; }

    // Hide only LevelZ's legacy per-skill current-level strings.
    // Our custom card labels are rendered separately; no widget/button coordinates are changed here.
    @ModifyConstant(method = "method_25394", constant = @Constant(intValue = 57), remap = false)
    private int goosker$hideLegacyCurrentLevelTextX(int original) { return -10000; }

    // Repaint the main content immediately before Screen renders its widgets. This lets
    // LevelZ keep all of its interaction logic while our layout hides the old labels and
    // presents the ten skills as tidy two-column cards. The actual art can be replaced
    // later by the resource pack without changing any screen coordinates.
    private static final net.minecraft.class_2960 GOOSKER_MAIN_OVERLAY =
        new net.minecraft.class_2960("goosker_levelz_overhaul:textures/gui/skill_main_overlay.png");
    private static final net.minecraft.class_2960 GOOSKER_TOP_OVERLAY =
        new net.minecraft.class_2960("goosker_levelz_overhaul:textures/gui/skill_top_overlay.png");
    private static final net.minecraft.class_2960 GOOSKER_XP_EMPTY =
        new net.minecraft.class_2960("goosker_levelz_overhaul:textures/gui/xp_bar_empty.png");
    private static final net.minecraft.class_2960 GOOSKER_XP_FILL =
        new net.minecraft.class_2960("goosker_levelz_overhaul:textures/gui/xp_bar_fill.png");

    private static final String[] GOOSKER_NAMES = {
        "Vigor", "Vitality", "Agility", "Dexterity", "Magicka",
        "Archery", "Bartering", "Smithing", "Mining", "Husbandry"
    };

    @Inject(
        method = "method_25394",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/class_437;method_25394(Lnet/minecraft/class_332;IIF)V",
            shift = At.Shift.BEFORE,
            remap = false
        ),
        remap = false
    )
    private void goosker$renderMainLayout(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        // Opaque card patches cover LevelZ's old skill labels while keeping its actual
        // icon/+ widgets alive. The top summary is repainted at TAIL so nothing old can bleed through.
        context.method_25290(GOOSKER_MAIN_OVERLAY, x, y, 0.0F, 0.0F, 200, 215, 200, 215);

        final int white = 0xF2F2F2;
        final int muted = 0xB8B8B8;
        Skill[] roster = goosker$tenSkillRoster();
        class_327 text = net.minecraft.class_310.method_1551().field_1772;

        // Five rows per column. Existing LevelZ icon widgets and + buttons are rendered
        // immediately after this injection, on top of these cards.
        for (int i = 0; i < roster.length; i++) {
            int col = i < 5 ? 0 : 1;
            int row = i < 5 ? i : i - 5;
            int cardX = x + (col == 0 ? 4 : 102);
            int cardY = y + 84 + row * 25;
            int textX = cardX + 30;
            int level = playerStatsManager.getSkillLevel(roster[i]);

            // Slightly reduce only the skill-name labels. Their top-left anchors stay
            // exactly where 0.3.15 placed them, but the smaller glyphs leave more
            // breathing room beside the larger 18px icons and long names.
            goosker$drawScaledSkillName(context, text, GOOSKER_NAMES[i], textX, cardY + 3, white);
            context.method_51433(text, level + " / 50", textX, cardY + 14, muted, true);
        }
    }


    private static void goosker$drawScaledSkillName(class_332 context, class_327 text, String value, int x, int y, int color) {
        final float scale = 0.84F;
        class_4587 matrices = context.method_51448();
        matrices.method_22903();
        matrices.method_22904((double) x, (double) y, 0.0D);
        matrices.method_22905(scale, scale, 1.0F);
        context.method_51433(text, value, 0, 0, color, true);
        matrices.method_22909();
    }

    @Inject(method = "method_25394", at = @At("TAIL"), remap = false)
    private void goosker$renderTopSummary(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        class_327 text = net.minecraft.class_310.method_1551().field_1772;
        final int white = 0xF2F2F2;
        final int gold = 0xF2D36B;
        final int muted = 0xB8B8B8;

        // Drawn last on purpose: this hides LevelZ's legacy six-stat summary and its old title/help widget.
        context.method_25290(GOOSKER_TOP_OVERLAY, x + 55, y + 5, 0.0F, 0.0F, 141, 78, 141, 78);

        context.method_51433(text, "Level " + playerStatsManager.getOverallLevel(), x + 76, y + 18, white, true);
        int pointsX = x + 76;
        int pointsEnd = context.method_51433(text, "Points ", pointsX, y + 31, white, true);
        pointsEnd = pointsEnd > pointsX ? pointsEnd : pointsX + pointsEnd;
        context.method_51433(text, String.valueOf(playerStatsManager.getSkillPoints()), pointsEnd, y + 31, gold, true);

        int nextXp = Math.max(1, playerStatsManager.getNextLevelExperience());
        float progress;
        try {
            progress = Math.max(0.0F, Math.min(1.0F, playerStatsManager.getLevelProgress()));
        } catch (Throwable ignored) {
            progress = 0.0F;
        }
        long currentXp = Math.round(nextXp * progress);
        int barX = x + 66;
        int barY = y + 51;
        context.method_25290(GOOSKER_XP_EMPTY, barX, barY, 0.0F, 0.0F, 120, 5, 120, 5);
        int fill = Math.round(120.0F * progress);
        if (fill > 0) {
            context.method_25290(GOOSKER_XP_FILL, barX, barY, 0.0F, 0.0F, fill, 5, 120, 5);
        }
        context.method_51433(text, "XP " + currentXp + " / " + nextXp, x + 90, y + 61, muted, true);

        // LevelZ still owns the real clickable icon widgets. 0.3.13 only bundled
        // our textures, so its widget renderer continued drawing ICON_TEXTURES.
        // Paint the approved replacement art last, directly over those 16x16 widgets.
        // The opaque 16x16 card patch prevents any legacy icon pixels from showing
        // through the transparent parts of the new artwork.
        for (int i = 0; i < 10; i++) {
            int col = i < 5 ? 0 : 1;
            int row = i < 5 ? i : i - 5;
            int iconX = x + (col == 0 ? 10 : 108);
            int iconY = y + 87 + row * 25;
            context.method_25290(SkillIconTextures.MAIN_ICON_BG, iconX, iconY, 0.0F, 0.0F, 18, 18, 18, 18);
            context.method_25290(SkillIconTextures.forDisplayIndex(i), iconX, iconY, 0.0F, 0.0F, 18, 18, 18, 18);
        }
    }


    // The old LevelZ '?' help widget duplicates the new click-through skill pages.
    @ModifyArg(
        method = "method_25426",
        at = @At(
            value = "INVOKE",
            target = "Lnet/levelz/screen/SkillScreen$WidgetButtonPage;<init>(IIIIIIZZLnet/minecraft/class_2561;Lnet/minecraft/class_4185$class_4241;)V",
            ordinal = 2,
            remap = false
        ),
        index = 0,
        require = 1,
        remap = false
    )
    private int goosker$hideLegacyHelpButton(int originalX) {
        return -10000;
    }

    // Nudge the skill contents slightly left so the icon, label/value, and + button
    // sit comfortably inside each 90px card.  Keep the right-column + button at
    // LevelZ's original x position; the previous +10px offset pushed it past the card edge.
    @ModifyArg(
        method = "method_25426",
        at = @At(
            value = "INVOKE",
            target = "Lnet/levelz/screen/SkillScreen$WidgetButtonPage;<init>(IIIIIIZZLnet/minecraft/class_2561;Lnet/minecraft/class_4185$class_4241;)V",
            ordinal = 0,
            remap = false
        ),
        index = 0,
        require = 1,
        remap = false
    )
    private int goosker$shiftSkillIconX(int originalX) {
        return originalX >= x + 100 ? x + 108 : x + 10;
    }

    // LevelZ places the 16px skill icons at y+90 and the 13px + buttons at y+92.
    // Our card rows begin at y+84 and are 22px tall, so both widgets sat about four
    // pixels too low. Shift them up so the icon, two-line label, and + button all
    // share the same visual center inside each rectangle.
    @ModifyArg(
        method = "method_25426",
        at = @At(
            value = "INVOKE",
            target = "Lnet/levelz/screen/SkillScreen$WidgetButtonPage;<init>(IIIIIIZZLnet/minecraft/class_2561;Lnet/minecraft/class_4185$class_4241;)V",
            ordinal = 0,
            remap = false
        ),
        index = 1,
        require = 1,
        remap = false
    )
    private int goosker$centerSkillIconY(int originalY) {
        return originalY - 3;
    }

    @ModifyArg(
        method = "method_25426",
        at = @At(
            value = "INVOKE",
            target = "Lnet/levelz/screen/SkillScreen$WidgetButtonPage;<init>(IIIIIIZZLnet/minecraft/class_2561;Lnet/minecraft/class_4185$class_4241;)V",
            ordinal = 1,
            remap = false
        ),
        index = 1,
        require = 1,
        remap = false
    )
    private int goosker$centerLevelButtonY(int originalY) {
        return originalY - 3;
    }

    // Place both columns on the same mathematical grid: identical icon padding,
    // identical text start, and identical right-side button padding.
    @ModifyArg(
        method = "method_25426",
        at = @At(
            value = "INVOKE",
            target = "Lnet/levelz/screen/SkillScreen$WidgetButtonPage;<init>(IIIIIIZZLnet/minecraft/class_2561;Lnet/minecraft/class_4185$class_4241;)V",
            ordinal = 1,
            remap = false
        ),
        index = 0,
        require = 1,
        remap = false
    )
    private int goosker$moveRightColumnPlusButton(int originalX) {
        return originalX >= x + 170 ? x + 180 : x + 82;
    }

    // LibZ refuses tab clicks whenever any child widget remains focused. Clicking a LevelZ + button
    // leaves that widget focused, which made the inventory tab stop working until the screen reopened.
    @ModifyArg(
        method = "method_25402",
        at = @At(
            value = "INVOKE",
            target = "Lnet/libz/util/DrawTabHelper;onTabButtonClick(Lnet/minecraft/class_310;Lnet/minecraft/class_437;IIDDZ)V",
            remap = false
        ),
        index = 6,
        require = 1,
        remap = false
    )
    private boolean goosker$neverBlockInventoryTabBecauseOfFocus(boolean focused) {
        return false;
    }

    @Inject(method = "method_25394", at = @At("TAIL"), remap = false)
    private void goosker$inventoryTabHoverSound(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        boolean hovered = goosker$isInventoryTab(mouseX, mouseY);
        CompatUtil.satisfyingHoverChanged(hovered, goosker$inventoryTabHovered);
        goosker$inventoryTabHovered = hovered;
    }

    private boolean goosker$isInventoryTab(double mouseX, double mouseY) {
        // Matches LibZ DrawTabHelper's first (inventory) tab hitbox on SkillScreen.
        return mouseX >= x && mouseX < x + 24 && mouseY >= y - 23 && mouseY < y + 4;
    }

    private static final int[] GOOSKER_ICON_ORDINALS = {0, 3, 2, 1, 11, 6, 7, 8, 9, 10};

    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 12), remap = false)
    private int goosker$tenButtonSlots(int original) { return 10; }

    @ModifyConstant(method = "method_25394", constant = @Constant(intValue = 12), remap = false)
    private int goosker$tenRenderedSkillRows(int original) { return 10; }

    @Redirect(
        method = {"method_25426", "method_25394", "lambda$init$0", "lambda$init$1"},
        at = @At(value = "INVOKE", target = "Lnet/levelz/stats/Skill;values()[Lnet/levelz/stats/Skill;", remap = false),
        require = 0,
        remap = false
    )
    private Skill[] goosker$tenSkillRoster() {
        // Player-facing order: five character skills on the left, five practical skills on the right.
        return new Skill[] {
            Skill.HEALTH,   // Vigor
            Skill.DEFENSE,  // Vitality
            Skill.AGILITY,
            Skill.STRENGTH, // Dexterity
            Skill.ALCHEMY,  // Magicka
            Skill.ARCHERY,
            Skill.TRADE,
            Skill.SMITHING,
            Skill.MINING,
            Skill.FARMING   // Husbandry
        };
    }

    // SkillScreen chooses each skill icon by display-index * 16. Once Luck and
    // Stamina are removed, that would make Archery inherit Stamina's icon, etc.
    // Remap the ten display slots back to the original LevelZ icon ordinals.
    // Slot 9 therefore intentionally uses ordinal 11: LevelZ's Alchemy icon for Magicka.
    @ModifyArg(
        method = "method_25426",
        at = @At(
            value = "INVOKE",
            target = "Lnet/levelz/screen/SkillScreen$WidgetButtonPage;<init>(IIIIIIZZLnet/minecraft/class_2561;Lnet/minecraft/class_4185$class_4241;)V",
            ordinal = 0,
            remap = false
        ),
        index = 4,
        require = 1,
        remap = false
    )
    private int goosker$remapSkillIconU(int originalU) {
        int displayIndex = originalU / 16;
        if (displayIndex >= 0 && displayIndex < GOOSKER_ICON_ORDINALS.length) {
            return GOOSKER_ICON_ORDINALS[displayIndex] * 16;
        }
        return originalU;
    }

    // LevelZ's own language resource has higher pack priority than resources bundled
    // by this addon when both use assets/levelz. Redirect only the ten skill-name
    // translation lookups to our namespace so the testing names are unambiguous.
    @Redirect(
        method = "method_25426",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/class_2561;method_43471(Ljava/lang/String;)Lnet/minecraft/class_5250;",
            remap = false
        ),
        require = 0,
        remap = false
    )
    private class_5250 goosker$skillTranslations(String key) {
        String mapped = switch (key) {
            case "spritetip.levelz.health_skill" -> "spritetip.goosker_levelz_overhaul.vigor_skill";
            case "spritetip.levelz.strength_skill" -> "spritetip.goosker_levelz_overhaul.dexterity_skill";
            case "spritetip.levelz.agility_skill" -> "spritetip.goosker_levelz_overhaul.agility_skill";
            case "spritetip.levelz.defense_skill" -> "spritetip.goosker_levelz_overhaul.vitality_skill";
            case "spritetip.levelz.archery_skill" -> "spritetip.goosker_levelz_overhaul.archery_skill";
            case "spritetip.levelz.trade_skill" -> "spritetip.goosker_levelz_overhaul.bartering_skill";
            case "spritetip.levelz.smithing_skill" -> "spritetip.goosker_levelz_overhaul.smithing_skill";
            case "spritetip.levelz.mining_skill" -> "spritetip.goosker_levelz_overhaul.mining_skill";
            case "spritetip.levelz.farming_skill" -> "spritetip.goosker_levelz_overhaul.husbandry_skill";
            case "spritetip.levelz.alchemy_skill" -> "spritetip.goosker_levelz_overhaul.magicka_skill";
            default -> key;
        };
        return class_2561.method_43471(mapped);
    }
}
