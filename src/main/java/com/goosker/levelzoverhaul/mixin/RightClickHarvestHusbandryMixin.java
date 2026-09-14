package com.goosker.levelzoverhaul.mixin;

import com.goosker.levelzoverhaul.SkillUtil;
import java.util.List;
import net.levelz.init.TagInit;
import net.levelz.stats.Skill;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Adds the same Husbandry crop-yield bonus to RightClickHarvest 4.6.1's custom harvest path. */
@Mixin(targets = "io.github.jamalam360.rightclickharvest.RightClickHarvest", remap = false)
public abstract class RightClickHarvestHusbandryMixin {
    @Inject(
        method = "completeHarvest(Lnet/minecraft/class_1937;Lnet/minecraft/class_2680;Lnet/minecraft/class_2338;Lnet/minecraft/class_1657;Lnet/minecraft/class_1268;Lnet/minecraft/class_1799;ZZLjava/lang/Runnable;)Lnet/minecraft/class_1269;",
        at = @At("TAIL"),
        remap = false
    )
    private static void goosker$rightClickHusbandryYield(
        class_1937 world, class_2680 state, class_2338 pos, class_1657 player,
        class_1268 hand, class_1799 tool, boolean damageTool, boolean dropItems, Runnable replant,
        CallbackInfoReturnable<class_1269> cir
    ) {
        if (world.field_9236 || player == null || player.method_7337() || !dropItems) return;

        int husbandry = SkillUtil.level(player, Skill.FARMING);
        if (husbandry <= 0) return;

        List<class_1799> drops = class_2248.method_9562(state, (class_3218) world, pos, (class_2586) null);
        for (class_1799 stack : drops) {
            if (!stack.method_31573(TagInit.FARM_ITEMS)) continue;

            float exactExtra = husbandry * (5.5F / 50.0F);
            int extra = (int) Math.floor(exactExtra);
            float fraction = exactExtra - extra;
            if (world.field_9229.method_43057() < fraction) extra++;

            if (extra > 0) {
                class_2248.method_9577(world, pos, stack.method_46651(extra));
            }
            break;
        }
    }
}
