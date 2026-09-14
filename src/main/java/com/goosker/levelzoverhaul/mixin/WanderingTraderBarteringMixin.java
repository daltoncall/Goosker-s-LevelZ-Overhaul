package com.goosker.levelzoverhaul.mixin;

import com.goosker.levelzoverhaul.SkillUtil;
import net.levelz.stats.Skill;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1657;
import net.minecraft.class_1914;
import net.minecraft.class_3989;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Applies the same Bartering price reduction to wandering traders as villagers. */
@Mixin(targets = "net.minecraft.class_3989", remap = false)
public abstract class WanderingTraderBarteringMixin {
    @Inject(method = "method_5992", at = @At("HEAD"), require = 1, remap = false)
    private void goosker$applyWanderingTraderDiscount(class_1657 player, class_1268 hand,
                                                       CallbackInfoReturnable<class_1269> cir) {
        class_3989 trader = (class_3989) (Object) this;
        if (trader.method_37908().field_9236) return;

        int bartering = SkillUtil.level(player, Skill.TRADE);
        for (class_1914 offer : trader.method_8264()) {
            int baseCost = offer.method_8246().method_7947();
            int adjustment = -(int) (bartering * 0.0072D * baseCost);
            offer.method_19273(adjustment);
        }
    }
}
