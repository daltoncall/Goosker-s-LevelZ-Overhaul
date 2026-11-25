package com.goosker.levelzoverhaul;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.LoggerFactory;

/** Server-owned block XP policy. Does not change item drops or ordinary XP. */
public final class BlockXpConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static volatile List<Pattern> blacklist = compile(new Settings().levelzXpBlockBlacklist);

    public static final class Settings {
        public List<String> levelzXpBlockBlacklist = new ArrayList<>(List.of(
            "minecraft:*sculk*", "deeperdarker:*sculk*"));
    }

    private BlockXpConfig() {}

    public static void load() {
        load(FabricLoader.getInstance().getConfigDir().resolve("goosker_levelz_overhaul.json"));
    }

    // Path overload also permits isolated config verification outside Minecraft.
    static void load(Path path) {
        try {
            Settings settings;
            if (Files.exists(path)) {
                try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                    settings = GSON.fromJson(reader, Settings.class);
                }
                if (settings == null || settings.levelzXpBlockBlacklist == null) {
                    throw new IllegalArgumentException("levelzXpBlockBlacklist must be a JSON array");
                }
            } else {
                settings = new Settings();
                Files.createDirectories(path.getParent());
                try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                    GSON.toJson(settings, writer);
                }
            }
            blacklist = compile(settings.levelzXpBlockBlacklist);
        } catch (IOException | RuntimeException e) {
            LoggerFactory.getLogger(GooskerLevelZOverhaul.MOD_ID).error(
                "Could not load {}. Keeping the current LevelZ XP blacklist; the file was not overwritten.", path, e);
        }
    }

    private static List<Pattern> compile(List<String> entries) {
        List<Pattern> result = new ArrayList<>();
        for (String entry : entries) {
            if (entry == null || !entry.matches("[a-z0-9_.*-]+:[a-z0-9_./*-]+")) {
                throw new IllegalArgumentException("Invalid block ID/pattern: " + entry);
            }
            String[] pieces = entry.split("\\*", -1);
            StringBuilder regex = new StringBuilder("^");
            for (int i = 0; i < pieces.length; i++) {
                if (i > 0) regex.append(".*");
                regex.append(Pattern.quote(pieces[i]));
            }
            result.add(Pattern.compile(regex.append('$').toString()));
        }
        return List.copyOf(result);
    }

    public static boolean blocksLevelzXp(String blockId) {
        for (Pattern pattern : blacklist) {
            if (pattern.matcher(blockId).matches()) return true;
        }
        return false;
    }
}
