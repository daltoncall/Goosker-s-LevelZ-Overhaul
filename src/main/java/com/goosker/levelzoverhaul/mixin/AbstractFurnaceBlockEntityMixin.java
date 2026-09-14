package com.goosker.levelzoverhaul.mixin;

import com.goosker.levelzoverhaul.SkillUtil;
import com.goosker.levelzoverhaul.SmithingFurnaceAccess;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Optional;
import net.levelz.stats.Skill;
import net.minecraft.class_1263;
import net.minecraft.class_1720;
import net.minecraft.class_1799;
import net.minecraft.class_1860;
import net.minecraft.class_1863;
import net.minecraft.class_1874;
import net.minecraft.class_243;
import net.minecraft.class_2960;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Smithing furnace bonuses, scoped to the player actually operating/collecting from the furnace.
 * No Smithing level is stored globally on the furnace.
 */
@Mixin(targets = "net.minecraft.class_2609", remap = false)
public abstract class AbstractFurnaceBlockEntityMixin implements SmithingFurnaceAccess {
    @Shadow private Object2IntOpenHashMap<class_2960> field_11986;

    @Unique private class_3222 goosker$smithingPlayer;
    @Unique private class_3222 goosker$outputPlayer;

    @Override
    public void goosker$setSmithingLevel(int level) {
        // Intentionally a no-op. Older alpha builds cached a level on the shared furnace,
        // which could leak one player's bonus to another player in multiplayer.
    }

    @Override
    public void goosker$setSmithingPlayer(class_3222 player) {
        this.goosker$smithingPlayer = player;
    }

    /** Fuel consumption reduction reaches 50% at Smithing 50. */
    @Inject(method = "method_11200", at = @At("RETURN"), cancellable = true, require = 1, remap = false)
    private void goosker$extendFuelTime(class_1799 fuel, CallbackInfoReturnable<Integer> cir) {
        int original = cir.getReturnValue();
        if (original <= 0) return;

        class_3222 player = goosker$smithingPlayer;
        if (player == null) return;

        // Only use the remembered player while they still have this exact furnace inventory open.
        if (!(player.field_7512 instanceof class_1720)) {
            goosker$smithingPlayer = null;
            return;
        }
        class_1263 openInventory = ((FurnaceScreenHandlerAccess) player.field_7512).goosker$getInventory();
        if (openInventory != (Object) this) {
            goosker$smithingPlayer = null;
            return;
        }

        int smithing = SkillUtil.level(player, Skill.SMITHING);
        if (smithing <= 0) return;

        double reduction = Math.min(smithing, 50) / 100.0D;
        int extended = (int) Math.round(original / (1.0D - reduction));
        cir.setReturnValue(Math.max(original, extended));
    }

    @Inject(method = "method_17763", at = @At("HEAD"), require = 1, remap = false)
    private void goosker$captureOutputPlayer(class_3222 player, CallbackInfo ci) {
        goosker$outputPlayer = player;
        goosker$smithingPlayer = player;
    }

    /**
     * Adds +3% of vanilla cooking XP per Smithing level for every cooking recipe that
     * actually has vanilla recipe XP. At Smithing 50 this is +150% bonus XP (2.5x total).
     * The bonus is granted directly to the player collecting output, not as shared XP orbs.
     */
    @Inject(method = "method_27354", at = @At("HEAD"), require = 1, remap = false)
    private void goosker$bonusSmeltingXp(class_3218 world, class_243 pos,
                                          CallbackInfoReturnable<java.util.List<class_1860<?>>> cir) {
        class_3222 player = goosker$outputPlayer;
        if (player != null) {
            int smithing = SkillUtil.level(player, Skill.SMITHING);
            if (smithing > 0) {
                class_1863 recipes = world.method_8433();
                float bonusMultiplier = smithing * 0.03F;
                float bonusXp = 0.0F;

                for (Object2IntMap.Entry<class_2960> entry : field_11986.object2IntEntrySet()) {
                    Optional<class_1860<?>> optional = recipes.method_8130(entry.getKey());
                    if (optional.isEmpty() || !(optional.get() instanceof class_1874 cooking)) continue;

                    float xpPerItem = cooking.method_8171();
                    if (xpPerItem <= 0.0F) continue;
                    bonusXp += entry.getIntValue() * xpPerItem * bonusMultiplier;
                }

                int bonus = (int) Math.floor(bonusXp);
                float fraction = bonusXp - bonus;
                if (fraction > 0.0F && Math.random() < fraction) bonus++;
                if (bonus > 0) player.method_7255(bonus);
            }
        }

        goosker$outputPlayer = null;
    }
}
