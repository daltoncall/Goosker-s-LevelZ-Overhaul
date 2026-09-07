package com.goosker.levelzoverhaul.mixin;

import com.goosker.levelzoverhaul.CompatUtil;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Compatibility/cleanup for LevelZ's custom button class, which bypasses AbstractButtonWidget.renderWidget. */
@Mixin(targets = "net.levelz.screen.SkillScreen$WidgetButtonPage", remap = false)
public abstract class SkillButtonCompatMixin {
    @Shadow @Final private boolean hoverOutline;
    @Shadow @Final private int textureY;
    @Shadow private List<class_2561> tooltip;

    private boolean goosker$wasHovered;

    @Inject(method = "method_48579", at = @At("HEAD"), remap = false)
    private void goosker$removeLegacySkillTooltip(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        // Skill icons are textureY=16 with hoverOutline=false. Their old LevelZ tooltip text
        // is redundant now that clicking the icon opens our full skill-description page.
        if (!hoverOutline && textureY == 16 && tooltip != null) {
            tooltip.clear();
        }
    }

    @Inject(method = "method_48579", at = @At("RETURN"), remap = false)
    private void goosker$satisfyingButtonsCompat(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        boolean hovered = CompatUtil.isActive(this) && CompatUtil.isHovered(this);
        CompatUtil.satisfyingHoverChanged(hovered, goosker$wasHovered);
        goosker$wasHovered = hovered;
    }
}
