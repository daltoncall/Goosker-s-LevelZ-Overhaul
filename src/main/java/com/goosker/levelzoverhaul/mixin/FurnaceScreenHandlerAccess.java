package com.goosker.levelzoverhaul.mixin;

import net.minecraft.class_1263;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Lets the furnace bonus verify that the remembered player still has this exact furnace open. */
@Mixin(targets = "net.minecraft.class_1720", remap = false)
public interface FurnaceScreenHandlerAccess {
    @Accessor(value = "field_7824", remap = false)
    class_1263 goosker$getInventory();
}
