package com.goosker.levelzoverhaul;

import net.minecraft.class_1109;
import net.minecraft.class_310;
import toni.satisfyingbuttons.SatisfyingButtonsClient;
import toni.satisfyingbuttons.foundation.config.AllConfigs;

/** Direct bridge for the exact Satisfying Buttons 1.1.2 API used by the pack. */
final class SatisfyingButtonsBridge {
    private SatisfyingButtonsBridge() {}

    static void playHover(boolean hovered) {
        var config = AllConfigs.client();
        if (!((Boolean) config.SoundEnabled.get())) return;
        if (!hovered && !((Boolean) config.ButtonUnhoverSoundEnabled.get())) return;

        var event = hovered ? SatisfyingButtonsClient.BUTTON_HOVER : SatisfyingButtonsClient.BUTTON_HOVER_REVERSE;
        class_310.method_1551().method_1483().method_4873(
            class_1109.method_4757(event, config.ButtonSoundPitch.getF(), config.ButtonSoundVolume.getF())
        );
    }
}
