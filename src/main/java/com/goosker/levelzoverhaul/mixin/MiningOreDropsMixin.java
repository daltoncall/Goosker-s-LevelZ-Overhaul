package com.goosker.levelzoverhaul.mixin;

import com.goosker.levelzoverhaul.BalanceValues;
import com.goosker.levelzoverhaul.SkillUtil;
import java.util.ArrayList;
import java.util.List;
import net.levelz.stats.Skill;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Replaces Fortune only in this loot calculation; the held tool is never changed. */
@Mixin(value = class_2248.class, remap = false)
public abstract class MiningOreDropsMixin {
    @Redirect(method = "method_9609", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/class_2680;method_26189(Lnet/minecraft/class_8567$class_8568;)Ljava/util/List;"), remap = false)
    private static List<class_1799> goosker$oreYield(class_2680 state, class_8567.class_8568 builder,
        class_2680 originalState, class_3218 world, class_2338 pos, class_2586 blockEntity,
        class_1297 entity, class_1799 tool) {
        int level = SkillUtil.level(entity, Skill.MINING);
        String id = class_7923.field_41175.method_10221(state.method_26204()).method_12832();
        boolean ore = state.method_26204() instanceof class_2431 || id.endsWith("_ore") || id.equals("ancient_debris");
        if (!(entity instanceof class_1657) || level <= 0 || !ore
                || class_1890.method_8225(class_1893.field_9099, tool) > 0) {
            return state.method_26189(builder);
        }
        class_1799 unfortuned = tool.method_7972();
        var enchantments = class_1890.method_8222(unfortuned);
        enchantments.remove(class_1893.field_9130);
        class_1890.method_8214(enchantments, unfortuned);
        builder.method_51874(class_181.field_1229, unfortuned);
        List<class_1799> normal = state.method_26189(builder);
        int multiplier = BalanceValues.oreMultiplier(level, world.field_9229.method_43057());
        List<class_1799> drops = new ArrayList<>();
        for (class_1799 stack : normal) {
            int remaining = stack.method_7947() * multiplier;
            while (remaining > 0) {
                int count = Math.min(remaining, stack.method_7914());
                drops.add(stack.method_46651(count));
                remaining -= count;
            }
        }
        return drops;
    }
}
