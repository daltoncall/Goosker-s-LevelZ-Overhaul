package com.goosker.levelzoverhaul.mixin;

import com.goosker.levelzoverhaul.BreedingBonus;
import net.levelz.access.PlayerStatsManagerAccess;
import net.levelz.stats.Skill;
import net.minecraft.class_1296;
import net.minecraft.class_1429;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = class_1429.class, remap = false)
public abstract class HusbandryBreedingMixin {
    @Unique private int goosker$breedingLevel;

    // Vanilla clears both love-cause players before spawning the original baby.
    @Inject(method = "method_24650", at = @At("HEAD"), remap = false)
    private void goosker$captureBreeder(class_3218 world, class_1429 other, CallbackInfo ci) {
        class_1429 self = (class_1429) (Object) this;
        class_3222 player = self.method_6478() != null ? self.method_6478() : other.method_6478();
        goosker$breedingLevel = player instanceof PlayerStatsManagerAccess access
            ? BreedingBonus.clamp(access.getPlayerStatsManager().getSkillLevel(Skill.FARMING)) : 0;
    }

    // Reward the original breeding event once, while both love-cause players remain available.
    @Inject(method = "method_49794", at = @At("HEAD"), remap = false)
    private void goosker$breedingXp(class_3218 world, class_1429 other, class_1296 baby, CallbackInfo ci) {
        class_1429 self = (class_1429) (Object) this;
        class_3222 player = self.method_6478() != null ? self.method_6478() : other.method_6478();
        if (player == null) return;
        int level = player instanceof PlayerStatsManagerAccess access
            ? access.getPlayerStatsManager().getSkillLevel(Skill.FARMING) : 0;
        float exact = (self.method_6051().method_43048(7) + 1)
            * (1.0F + com.goosker.levelzoverhaul.BalanceValues.xpBonus(level));
        int xp = (int) exact;
        if (self.method_6051().method_43057() < exact - xp) xp++;
        net.levelz.entity.LevelExperienceOrbEntity.spawn(world,
            self.method_19538().method_1031(0.0D, 0.1D, 0.0D), xp);
    }

    // This point is reached only when vanilla created and spawned a real offspring.
    @Inject(method = "method_24650", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/class_3218;method_30771(Lnet/minecraft/class_1297;)V",
        shift = At.Shift.AFTER), remap = false)
    private void goosker$extraOffspring(class_3218 world, class_1429 other, CallbackInfo ci) {
        class_1429 self = (class_1429) (Object) this;
        int count = BreedingBonus.extraCount(goosker$breedingLevel, self.method_6051().method_43057());
        for (int i = 0; i < count; i++) {
            class_1296 baby = self.method_5613(world, other);
            if (baby == null) continue;
            baby.method_7217(true);
            baby.method_5808(self.method_23317(), self.method_23318(), self.method_23321(), 0.0F, 0.0F);
            world.method_30771(baby);
        }
    }
}
