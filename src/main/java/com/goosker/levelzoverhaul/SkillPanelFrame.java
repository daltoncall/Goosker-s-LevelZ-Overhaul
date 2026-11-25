package com.goosker.levelzoverhaul;

import net.minecraft.class_332;

/** Expands the old inset border by one GUI pixel, keeping all content/tab anchors. */
public final class SkillPanelFrame {
    private SkillPanelFrame() {}
    public static void draw(class_332 c, int x, int y, int w, int h) {
        // Old opaque perimeter was x+1..w-2 and y+1..h-2.
        int edge = 0xFF141414, light = 0xFF555555, top = 0xFF5E5E5E, shade = 0xFF181818;
        c.method_25294(x, y, x+w, y+1, edge);
        c.method_25294(x, y+h-1, x+w, y+h, edge);
        c.method_25294(x, y+1, x+1, y+h-1, edge);
        c.method_25294(x+w-1, y+1, x+w, y+h-1, edge);
        c.method_25294(x+1, y+1, x+w-1, y+3, top);
        c.method_25294(x+1, y+3, x+3, y+h-3, light);
        c.method_25294(x+w-3, y+3, x+w-1, y+h-3, shade);
        c.method_25294(x+1, y+h-3, x+w-1, y+h-1, shade);
    }
}
