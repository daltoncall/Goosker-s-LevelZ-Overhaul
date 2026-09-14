package com.goosker.levelzoverhaul.mixin;

import com.goosker.levelzoverhaul.SkillUtil;
import net.levelz.stats.Skill;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.class_3222", remap = false)
public abstract class ServerPlayerVitalityMixin {
    @Inject(method = "method_14226()V", at = @At("TAIL"), remap = false)
    private void goosker$vitalityToughness(CallbackInfo ci) {
        int vitality = SkillUtil.level(this, Skill.DEFENSE);
        // EntityAttributes.GENERIC_ARMOR_TOUGHNESS = intermediary field_23725 in 1.20.1.
        SkillUtil.setAttributeBase(this, "field_23725", vitality * 0.08D);
    }
}
