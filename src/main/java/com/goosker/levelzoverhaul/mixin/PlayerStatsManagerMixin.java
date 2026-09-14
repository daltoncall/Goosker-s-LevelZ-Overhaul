package com.goosker.levelzoverhaul.mixin;

import java.lang.reflect.Field;
import java.util.Map;
import net.minecraft.class_2487;
import net.levelz.stats.Skill;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.levelz.stats.PlayerStatsManager", remap = false)
public abstract class PlayerStatsManagerMixin {
    @Inject(method = "setSkillLevel", at = @At("HEAD"), cancellable = true, remap = false)
    private void goosker$disableRetiredSkills(Skill skill, int level, CallbackInfo ci) {
        if (skill == Skill.LUCK || skill == Skill.STAMINA) {
            ci.cancel();
        }
    }

    /**
     * Clamp the value argument itself before LevelZ writes it. This handles +1/+5/+10
     * upgrade packets cleanly: 48 + 5 becomes 50 instead of 53.
     */
    @ModifyVariable(method = "setSkillLevel", at = @At("HEAD"), ordinal = 0, argsOnly = true, remap = false)
    private int goosker$clampSkillLevel(int requestedLevel) {
        return Math.min(requestedLevel, 50);
    }

    /** Existing worlds may already have points stored in the two retired skills. Zero them after NBT load. */
    @Inject(method = "readNbt", at = @At("TAIL"), remap = false)
    @SuppressWarnings("unchecked")
    private void goosker$clearRetiredSkillsAfterLoad(class_2487 nbt, CallbackInfo ci) {
        try {
            Field field = this.getClass().getDeclaredField("skillLevel");
            field.setAccessible(true);
            Map<Skill, Integer> levels = (Map<Skill, Integer>) field.get(this);
            levels.put(Skill.LUCK, 0);
            levels.put(Skill.STAMINA, 0);
        } catch (ReflectiveOperationException ignored) {
        }
    }
}
