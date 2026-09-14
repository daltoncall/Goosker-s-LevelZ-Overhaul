# Goosker's LevelZ Overhaul

Fabric 1.20.1 addon/overhaul for LevelZ used by Goosker's Medieval Modpack.

## Current source release

`0.3.36-alpha`

This source tree corresponds to the 0.3.36-alpha jar and includes the changes carried forward from 0.3.35-alpha:

- multiplayer-safe Smithing furnace ownership
- Smithing bonus XP on all cooking recipes that provide vanilla cooking XP
- direct Smithing bonus XP credit to the player collecting furnace output
- wandering-trader Bartering discounts
- Draugr compatibility relaxed from an exact 1.8.3 pin to `>=1.8.3`
- Dwarven 1.1.1 Sphere and Spider summoning staffs included in the Magicka weapon tag

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
