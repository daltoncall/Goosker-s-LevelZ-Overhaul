# 0.3.39-alpha

- Add a configurable block blacklist for blue LevelZ XP.
- Default to all vanilla and Deeper and Darker sculk variants; preserve normal XP.
- Support exact registry IDs and `*` wildcards in `config/goosker_levelz_overhaul.json`.
- Retain previous policy on invalid config and never overwrite malformed user config.

# 0.3.38-alpha

- Vitality toughness: +4.00 -> +10.0 maximum.
- Activity exhaustion reduction: 40% -> 100%; damage-related exhaustion is exempt from reduction.
- Villager and wandering trader discounts: 36% -> 95% maximum.
- Smithing total cooking XP: 250% -> 200% maximum.
- Replaces LevelZ native bonus ore duplication with a progressive 1x -> 3x drop multiplier, using a Fortune-free tool copy. Silk Touch and the held item's enchantments are preserved.
- Husbandry produce: 6.5x average -> 7x average total drops; UI shows percentages.
- Breeding skill XP: fixed 2x -> progressive 100%..200%, with native constant XP disabled.
- Central Satisfying Buttons tab coverage and direct active/clickable detection for LevelZ widgets. Existing custom back-arrow coverage retained, duplicate inventory-only hover hooks removed.
- Keeps all 0.3.37 panel, Inventory tooltip, maximum tooltip, breeding, and configuration-order fixes.

Validation: full Java 17 compilation; 5,327 runtime checks in an isolated Fabric 1.20.1 server; 100 Fortune III ore rolls all returned exactly three diamonds; Silk Touch returned one ore block; actual wheat extras, toughness, damage/activity exhaustion, seeded breeding XP (6/9/12 at levels 0/25/50), and actual smelting bonus XP at 0/25/50 passed. Standalone 5,100-outcome ore probability test passed. Ten patched classes transformed successfully on the client with Satisfying Buttons 1.1.2 present. No graphical/audio playtest was performed.
