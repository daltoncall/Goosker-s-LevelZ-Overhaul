package com.goosker.levelzoverhaul.mixin;

import com.goosker.levelzoverhaul.SkillUtil;
import net.levelz.stats.Skill;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.class_1657", remap = false)
public abstract class PlayerEntityAgilityMiningMixin {
    @ModifyArg(
        method = "method_7322(F)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1702;method_7583(F)V", remap = false),
        index = 0, remap = false
    )
    private float goosker$agilityExhaustion(float amount) {
        int agility = SkillUtil.level(this, Skill.AGILITY);
        return amount * (1.0F - Math.min(0.40F, agility * 0.008F));
    }

    @ModifyVariable(method = "method_5747(FFLnet/minecraft/class_1282;)Z", at = @At("HEAD"), argsOnly = true, ordinal = 0, remap = false)
    private float goosker$agilitySafeFall(float fallDistance) {
        int agility = SkillUtil.level(this, Skill.AGILITY);
        // Vanilla starts taking damage after ~3 blocks. Add up to 22 safe blocks => ~25 total at 50.
        return Math.max(0.0F, fallDistance - agility * (22.0F / 50.0F));
    }

    @Inject(method = "method_7351(Lnet/minecraft/class_2680;)F", at = @At("RETURN"), cancellable = true, remap = false)
    private void goosker$miningEfficiency(class_2680 state, CallbackInfoReturnable<Float> cir) {
        int mining = SkillUtil.level(this, Skill.MINING);
        cir.setReturnValue(cir.getReturnValue() * (1.0F + mining * 0.01F));
    }
}
