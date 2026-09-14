package com.goosker.levelzoverhaul.mixin;

import com.goosker.levelzoverhaul.SkillUtil;
import net.levelz.stats.Skill;
import net.minecraft.class_1937;
import net.minecraft.class_1309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.mebahelcreaturesdraugr.entity.projectile.FrostSpikeEntity", remap = false)
public abstract class FrostSpikeProjectileMixin {
    @Shadow(remap = false) float damage;
    @Inject(method = "<init>(Lnet/minecraft/class_1937;Lnet/minecraft/class_1309;F)V", at = @At("TAIL"), remap = false, require = 0)
    private void goosker$magickaDamage(class_1937 world, class_1309 owner, float baseDamage, CallbackInfo ci) {
        damage += SkillUtil.level(owner, Skill.ALCHEMY) * 0.1F;
    }
}
