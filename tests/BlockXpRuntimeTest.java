package com.goosker.levelzoverhaul;

import java.nio.file.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.levelz.entity.LevelExperienceOrbEntity;
import net.minecraft.*;

/** Disposable Fabric server test, never packaged in the release. */
public final class BlockXpRuntimeTest implements ModInitializer {
    static int checks;
    static void check(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
        checks++;
        System.out.println("BLOCK_XP PASS " + message);
    }
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            try {
                var world = server.method_30002();
                var drop = class_2248.class.getDeclaredMethod("method_9583", class_3218.class, class_2338.class, int.class);
                drop.setAccessible(true);
                net.levelz.init.ConfigInit.CONFIG.oreXPMultiplier = 1;
                net.levelz.init.ConfigInit.CONFIG.dropXPbasedOnLvl = false;
                var pos = new class_2338(0, 100, 0);
                check(world.method_8320(pos).method_26215(), "test position is air after block removal");
                int x = 0;
                for (String id : new String[]{"minecraft:sculk", "minecraft:sculk_sensor", "minecraft:sculk_catalyst",
                        "minecraft:sculk_shrieker", "minecraft:calibrated_sculk_sensor", "minecraft:diamond_ore"}) {
                    var block = class_7923.field_41175.method_10223(new class_2960(id));
                    int vanillaBefore = vanilla(world), blueBefore = blue(world);
                    drop.invoke(block, world, pos.method_10069(x += 4, 0, 0), 7);
                    check(vanilla(world) - vanillaBefore == 7, id + " keeps seven normal XP");
                    check(blue(world) - blueBefore == (id.equals("minecraft:diamond_ore") ? 7 : 0), id + " blue XP policy");
                }
                var config = Path.of("config/goosker_levelz_overhaul.json");
                Files.writeString(config, "{\"levelzXpBlockBlacklist\":[\"minecraft:diamond_ore\"]}");
                BlockXpConfig.load(config);
                int before = blue(world), normal = vanilla(world);
                drop.invoke(class_7923.field_41175.method_10223(new class_2960("minecraft:diamond_ore")), world, pos.method_10069(28,0,0), 7);
                System.out.println("MANUAL_DELTA blue="+(blue(world)-before)+" vanilla="+(vanilla(world)-normal));
                check(blue(world) == before && vanilla(world) - normal == 7, "manual exact ID blacklists diamonds and preserves normal XP");
                drop.invoke(class_7923.field_41175.method_10223(new class_2960("minecraft:sculk")), world, pos.method_10069(32,0,0), 7);
                check(blue(world) - before == 7, "removing sculk from config restores blue XP");
                var rules = world.method_8450();
                rules.method_20746(class_1928.field_19392).method_20758(false, server);
                before = blue(world); normal = vanilla(world);
                drop.invoke(class_7923.field_41175.method_10223(new class_2960("minecraft:diamond_ore")), world, pos.method_10069(36,0,0), 7);
                check(blue(world) == before && vanilla(world) == normal, "doTileDrops false suppresses both XP types");
                Files.writeString(Path.of("TEST_PASS"), checks + " block XP runtime checks passed");
            } catch (Throwable t) {
                t.printStackTrace();
                try { Files.writeString(Path.of("TEST_FAIL"), t.toString()); } catch (Exception ignored) {}
            } finally { server.method_3747(false); }
        });
    }
    static int vanilla(class_3218 world) {
        int total = 0;
        for (var e : world.method_27909()) if (e instanceof class_1303 orb) {
            var nbt = new class_2487(); orb.method_5652(nbt);
            total += orb.method_5919() * nbt.method_10550("Count");
        }
        return total;
    }
    static int blue(class_3218 world) throws Exception {
        int total = 0;
        var map = LevelExperienceOrbEntity.class.getDeclaredMethod("getClumpedMap"); map.setAccessible(true);
        for (var e : world.method_27909()) if (e instanceof LevelExperienceOrbEntity orb) {
            @SuppressWarnings("unchecked") var values = (java.util.Map<Integer,Integer>) map.invoke(orb);
            for (var value : values.entrySet()) total += value.getKey() * value.getValue();
        }
        return total;
    }
}
