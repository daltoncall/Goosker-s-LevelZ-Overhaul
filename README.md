# Goosker's LevelZ Overhaul — 0.3.39-alpha

Fabric Minecraft 1.20.1 addon for LevelZ 1.4.13. Carries forward all 0.3.37 fixes.

## Block XP blacklist (0.3.39)

Replace the previous overhaul JAR on both client and server. Keep the normal LevelZ dependencies.
On first launch, the mod creates `config/goosker_levelz_overhaul.json`:

```json
{
  "levelzXpBlockBlacklist": [
    "minecraft:*sculk*",
    "deeperdarker:*sculk*"
  ]
}
```

Entries match the block's registry ID, not the held tool. Exact IDs and `*` wildcards are supported.
For example, add `"minecraft:diamond_ore"` to block that block, or `"examplemod:*_ore"` for all ore-suffixed blocks in that mod. An empty list disables the blacklist.
The defaults cover all vanilla sculk blocks, including calibrated sculk sensors, and all Deeper and Darker sculk variants, including sculk stone ores.
Only blue LevelZ XP is blocked. Normal Minecraft XP, item drops, Silk Touch, and the `doTileDrops` rule keep their normal behavior. This list does not affect XP from mobs, trading, cooking, or breeding, and does not remove existing XP orbs.
Edit the server's config for multiplayer or your instance's config for single-player, then restart the server/game. `/reload` does not reload this config.
Invalid JSON is logged without overwriting your file; the current policy is retained (the defaults on startup).

## Level-50 balance

| Stat | Maximum |
|---|---|
| Vitality Toughness | +10.0 |
| Agility Exhaustion Used | -100% for activity; damage exhaustion stays at its normal value |
| Bartering Trade Discount | 95% (Minecraft still enforces whole-item prices and minimum cost) |
| Smithing Smelting XP | 200% total vanilla cooking XP |
| Mining Ore Drops | 100% x3 Ore Drops |
| Husbandry Avg. Bonus Produce | 700% total normal unenchanted produce yield on average |
| Husbandry Breeding XP | 200% total LevelZ breeding XP |

All bonus values and their hover maximums use the updated balance. XP/produce percentages show total yields: 100% means normal, 200% means double, 700% means seven times normal.

## Ore drops

Mining scales from 1x to 3x. Level 10: 40% chance of double; level 25: guaranteed double; level 40: double with 60% chance of triple; level 50: guaranteed triple. The UI shows the chance and the next target multiplier. Fortune is removed only from the temporary loot tool copy when Mining is above zero; the real tool and its enchantments remain intact. Silk Touch bypasses the multiplier. This applies to ExperienceDroppingBlock ores, blocks whose registry path ends in `_ore`, and ancient debris. Non-player loot calculations and other blocks retain their existing behavior. LevelZ's native ore duplication is disabled to avoid double rewards.

## Husbandry

Extra offspring still scale to five guaranteed extra babies at level 50, in addition to the original baby. Every ten levels guarantees another extra; intervening levels add a 10%-per-level chance of the next. Breeding skill XP now scales from 100% at level 0 to 200% at 50, awarded once per original breeding event. Extra babies do not recursively generate breeding rewards. The vanilla breeding XP/cooldown path is retained.

Produce scales from 100% to 700% on average. Mature wheat gives one normal wheat plus six extras at level 50. The same bonus calculation is used for normal crop breaking and RightClickHarvest. Only the first item in LevelZ's FARM_ITEMS tag is supplemented, preserving the prior crop eligibility and seed behavior.

## Satisfying Buttons 1.1.2

Active clickable LevelZ widgets (skill icons, + buttons, list/back controls) use the mod's hover/unhover sound events. LibZ tabs are covered centrally, including Skills on the vanilla inventory and Inventory on LevelZ screens. The custom description-page back arrow is covered too. Selected, disabled, and non-clickable controls do not trigger hover sounds. Sound enable, reverse sound, volume, and pitch follow Satisfying Buttons settings. Normal click sounds remain; Satisfying Buttons itself supplies hover/reverse events rather than a replacement click event. Integration is optional.

## Requirements

- Java 17
- Minecraft 1.20.1
- Fabric Loader
- Fabric API
- LevelZ 1.4.13
- LibZ 1.0.3

Runtime integrations are declared in `fabric.mod.json`. Draugr is required by the current projectile Magicka mixins; Dwarven is suggested/optional because its current compatibility is item-tag based.

## Build

Windows:

```bat
gradlew.bat build
```

Linux/macOS:

```sh
./gradlew build
```

The release jar is produced under `build/libs/`.

### Mapping note

The Java sources intentionally use Fabric/Minecraft intermediary names (`class_####`, `method_####`, `field_####`). That matches the existing production code and the `remap = false` mixins. Do not mechanically convert this source to Yarn names without also reviewing every mixin target and accessor.

## Project layout

- `src/main/java/` — mod and mixin source
- `src/main/resources/` — Fabric metadata, mixin config, language files, textures and item tags
- `gradle/`, `gradlew`, `gradlew.bat` — Gradle wrapper
- `CREDITS.md` — asset/project credits

## 0.3.36 compatibility notes

Draugr compatibility accepts version 1.8.3 or newer. The supplied/tested update was 1.8.7 for Minecraft 1.20.1; the relevant Frostbite, Frost Spike and Firebolt targets remained present.

The following Dwarven 1.1.1 item IDs are tagged as Magicka weapons:

- `mebahelcreaturesdwarven:weapon/dwemer/dwarven_sphere_summoning_staff`
- `mebahelcreaturesdwarven:weapon/dwemer/dwarven_spider_summoning_staff`

These two summoning staffs are classified as Magicka weapons, but this release does not add Magicka damage scaling to the summoned Sphere/Spider entities themselves.
