package com.goosker.levelzoverhaul.mixin;

import com.goosker.levelzoverhaul.SmithingFurnaceAccess;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2586;
import net.minecraft.class_2680;
import net.minecraft.class_3222;
import net.minecraft.class_3965;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Records the specific server player currently operating a furnace-like block. */
@Mixin(targets = "net.minecraft.class_2363", remap = false)
public abstract class AbstractFurnaceBlockUseMixin {
    @Inject(method = "method_9534", at = @At("HEAD"), require = 1, remap = false)
    private void goosker$rememberSmithingPlayer(class_2680 state, class_1937 world, class_2338 pos,
                                                 class_1657 player, class_1268 hand, class_3965 hit,
                                                 CallbackInfoReturnable<class_1269> cir) {
        if (world.field_9236 || !(player instanceof class_3222 serverPlayer)) return;

        class_2586 blockEntity = world.method_8321(pos);
        if (blockEntity instanceof SmithingFurnaceAccess furnace) {
            furnace.goosker$setSmithingPlayer(serverPlayer);
        }
    }
}
