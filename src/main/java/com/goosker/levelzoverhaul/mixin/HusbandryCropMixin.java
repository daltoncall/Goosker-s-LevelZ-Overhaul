package com.goosker.levelzoverhaul.mixin;

import com.goosker.levelzoverhaul.SkillUtil;
import java.util.List;
import net.levelz.init.TagInit;
import net.levelz.stats.Skill;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2586;
import net.minecraft.class_2680;
import net.minecraft.class_3218;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Husbandry crop yield overhaul.
 * LevelZ 1.4.13 can only duplicate one crop stack once, so its native crop bonus
 * is disabled and replaced here with a smooth 0 -> 5.5 extra produce curve.
 * At Husbandry 50, a mature wheat harvest therefore drops 6 or 7 wheat total.
 */
@Mixin(targets = "net.minecraft.class_2248", remap = false)
public abstract class HusbandryCropMixin {
    @Inject(
        method = "method_9576(Lnet/minecraft/class_1937;Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;Lnet/minecraft/class_1657;)V",
        at = @At("TAIL"),
        remap = false
    )
    private void goosker$husbandryCropYield(class_1937 world, class_2338 pos, class_2680 state,
                                             class_1657 player, CallbackInfo ci) {
        if (world.field_9236 || player == null || player.method_7337()) return;

        int husbandry = SkillUtil.level(player, Skill.FARMING);
        if (husbandry <= 0) return;

        List<class_1799> drops = class_2248.method_9562(state, (class_3218) world, pos, (class_2586) null);
        for (class_1799 stack : drops) {
            if (!stack.method_31573(TagInit.FARM_ITEMS)) continue;

            // 5.5 extra produce at 50: wheat's normal 1 + 5/6 bonus = 6-7 total.
            float exactExtra = husbandry * (5.5F / 50.0F);
            int extra = (int) Math.floor(exactExtra);
            float fraction = exactExtra - extra;
            if (world.field_9229.method_43057() < fraction) extra++;

            if (extra > 0) {
                class_1799 bonus = stack.method_46651(extra);
                class_2248.method_9577(world, pos, bonus);
            }
            break;
        }
    }
}
