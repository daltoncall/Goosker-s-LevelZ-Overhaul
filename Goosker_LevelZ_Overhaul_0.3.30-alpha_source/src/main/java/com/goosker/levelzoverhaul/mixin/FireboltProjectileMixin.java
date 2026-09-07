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

@Mixin(targets = "net.mebahelcreaturesdraugr.entity.projectile.FireboltEntity", remap = false)
public abstract class FireboltProjectileMixin {
    @Shadow(remap = false) float damage;

    @Inject(method = "<init>(Lnet/minecraft/class_1937;Lnet/minecraft/class_1309;F)V", at = @At("TAIL"), remap = false)
    private void goosker$magickaSummonDamage(class_1937 world, class_1309 shooter, float baseDamage, CallbackInfo ci) {
        // Direct player-fired firebolt: scale from that player.
        int magicka = SkillUtil.level(shooter, Skill.ALCHEMY);
        if (magicka == 0) {
            // Summoned Flame Atronach: trace its owner and scale from the summoner's Magicka.
            try {
                java.lang.reflect.Method isSummoned = shooter.getClass().getMethod("isSummoned");
                if (Boolean.TRUE.equals(isSummoned.invoke(shooter))) {
                    Object owner = shooter.getClass().getMethod("getOwnerEntity").invoke(shooter);
                    magicka = SkillUtil.level(owner, Skill.ALCHEMY);
                }
            } catch (ReflectiveOperationException ignored) {
            }
        }
        damage += magicka * 0.1F;
    }
}
