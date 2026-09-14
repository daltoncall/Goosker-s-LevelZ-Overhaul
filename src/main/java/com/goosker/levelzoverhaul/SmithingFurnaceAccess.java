package com.goosker.levelzoverhaul;

import net.minecraft.class_3222;

/** Bridge between furnace block/entity mixins for per-player Smithing behavior. */
public interface SmithingFurnaceAccess {
    /** Retained for binary/source compatibility with earlier alpha builds; no shared level is cached. */
    void goosker$setSmithingLevel(int level);

    /** Records the server player currently operating this furnace. */
    void goosker$setSmithingPlayer(class_3222 player);
}
