package com.goosker.levelzoverhaul.mixin;

import com.goosker.levelzoverhaul.BlockXpConfig;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = class_2248.class, remap = false)
public abstract class BlockLevelzXpBlacklistMixin {
    @Inject(method = "method_9583", at = @At("HEAD"), cancellable = true, remap = false)
    private void goosker$blacklistedXp(class_3218 world, class_2338 pos, int amount, CallbackInfo ci) {
        String id = class_7923.field_41175.method_10221((class_2248) (Object) this).toString();
        if (!BlockXpConfig.blocksLevelzXp(id)) return;

        // Preserve the exact vanilla Block.dropExperience body, including doTileDrops.
        // Returning here bypasses LevelZ's injection at the vanilla orb spawn call.
        // The block at pos may already be air; use this block's registry ID instead.
        if (world.method_8450().method_8355(class_1928.field_19392)) {
            class_1303.method_31493(world, class_243.method_24953(pos), amount);
        }
        ci.cancel();
    }
}
