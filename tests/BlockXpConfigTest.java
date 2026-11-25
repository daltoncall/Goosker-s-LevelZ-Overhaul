package com.goosker.levelzoverhaul;

import java.nio.file.*;

public final class BlockXpConfigTest {
    private static void check(boolean ok) { if (!ok) throw new AssertionError(); }
    public static void main(String[] args) throws Exception {
        Path config = Files.createTempDirectory("levelz-xp-test").resolve("config.json");
        BlockXpConfig.load(config);
        check(Files.exists(config));
        for (String id : new String[]{"minecraft:sculk", "minecraft:sculk_sensor",
            "minecraft:calibrated_sculk_sensor", "deeperdarker:gloomy_sculk",
            "deeperdarker:sculk_stone", "deeperdarker:blooming_sculk_stone"}) {
            check(BlockXpConfig.blocksLevelzXp(id));
        }
        check(!BlockXpConfig.blocksLevelzXp("minecraft:diamond_ore"));
        check(!BlockXpConfig.blocksLevelzXp("anothermod:sculk_stone"));
        Files.writeString(config, "{\"levelzXpBlockBlacklist\":[\"minecraft:diamond_ore\",\"example:*_ore\"]}");
        BlockXpConfig.load(config);
        check(BlockXpConfig.blocksLevelzXp("minecraft:diamond_ore"));
        check(BlockXpConfig.blocksLevelzXp("example:copper_ore"));
        check(!BlockXpConfig.blocksLevelzXp("example:copper_ore_block"));
        check(!BlockXpConfig.blocksLevelzXp("minecraft:sculk"));
        Files.writeString(config,"{\"levelzXpBlockBlacklist\":[]}");
        BlockXpConfig.load(config);
        check(!BlockXpConfig.blocksLevelzXp("minecraft:diamond_ore"));
        Files.writeString(config,"{\"levelzXpBlockBlacklist\":[\"minecraft:sculk*\"]}");
        BlockXpConfig.load(config);
        Files.writeString(config,"{broken json");
        BlockXpConfig.load(config);
        check(BlockXpConfig.blocksLevelzXp("minecraft:sculk"));
        check(Files.readString(config).equals("{broken json"));
        System.out.println("Block XP config checks passed");
    }
}
