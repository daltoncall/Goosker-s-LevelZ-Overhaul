# Goosker LevelZ Overhaul 0.3.30-alpha

Target: Fabric 1.20.1 + LevelZ 1.4.13 + Mebahel Creatures: Draugr 1.8.3.

Implemented in this alpha:
- 50 max per active skill / 500 overall cap.
- Luck + Stamina cannot receive levels, are cleared to 0 when player data loads, and are removed from the LevelZ skill screen.
- HEALTH/Vigor: +25 hearts at 50.
- STRENGTH/Dexterity: +5 melee damage at 50.
- DEFENSE/Vitality: +5 armor +4 armor toughness at 50.
- AGILITY: ~20% movement boost, 40% exhaustion reduction, ~25 total safe-fall blocks at 50.
- ARCHERY: +5 bow/crossbow damage at 50 via LevelZ.
- TRADE/Bartering: 36% trade discount +200% LevelZ trade XP at 50.
- SMITHING: 100% durability conservation +50% anvil XP discount at 50.
- MINING: +50% block-breaking speed and 50% LevelZ bonus ore-drop chance at 50.
- FARMING/Husbandry: custom crop yield scaling to ~6-7 wheat per mature wheat at 50, guaranteed twins at 50, and 2x LevelZ breeding XP.
- ALCHEMY/Magicka: native potion amplifier chance plus +5 damage at 50 for Draugr frostbite/frost-spike projectiles and summoned Flame Atronach firebolts.

Not implemented yet:
- The requested +20% enchanting/enchantability behavior for Magicka.
- The fourth non-Draugr magic staff. The supplied BetterEnd 4.0.11 jar does not contain an obvious staff/wand/scepter item, so its exact item/mod still needs identifying.
- Full modpack-wide tagging of every modded bow, crop, ore, etc.; this alpha establishes the core mechanics first.
- Resource-pack renames/icons/UI art.

Testing:
1. Back up the world/playerdata first; this is an alpha.
2. Put `goosker-levelz-overhaul-0.3.12-alpha.jar` in BOTH client and server `mods` folders.
3. Keep LevelZ 1.4.13, Fabric API, Draugr 1.8.3, and Mebahel's API installed.
4. Launch a copied/test world and verify the LevelZ screen shows ten entries and a per-skill cap of 50.
5. Check levels 0/25/50 for the intended mechanics. If launch fails or a mechanic misbehaves, save `latest.log` / the crash report for diagnosis.

Important: this is a test build assembled directly in intermediary mappings against the exact supplied 1.20.1 jars. It has not been launched inside the full Minecraft instance in this environment.


0.2.5-alpha fix: LevelZ 1.4.13 hard-codes a 12-row render loop in SkillScreen.method_25394. The addon now patches that render loop to 10 rows as well as resizing the button arrays, fixing the ArrayIndexOutOfBoundsException when opening the LevelZ screen.


0.2.5-alpha fix: SkillScreen button click lambdas were still resolving the unfiltered 12-skill enum, so visible buttons after the removed Stamina/Luck slots upgraded the wrong underlying skills. The click handlers now use the same 10-skill roster as rendering. This build also includes temporary testing labels and remapped stock icons so Vigor/Dexterity/Agility/Vitality/Archery/Bartering/Smithing/Mining/Husbandry/Magicka are identifiable before the custom resource pack is made. Magicka is internally LevelZ ALCHEMY; LUCK remains retired.


0.2.5-alpha UI testing fix: skill icons now follow the filtered 10-skill roster instead of the original first ten icon slots. Magicka intentionally uses LevelZ's original Alchemy icon. Skill-name translations are redirected to the addon's own resource namespace so LevelZ's higher-priority language file cannot overwrite the temporary Vigor/Dexterity/Vitality/Bartering/Husbandry/Magicka testing labels.


0.2.9-alpha Husbandry pass: LevelZ native plant duplication is disabled and replaced by a smooth custom crop-yield curve (up to +5 or +6 crop items at level 50). `farmingTwinChance` is corrected to 1.0 for guaranteed twins at max Husbandry. `breedingXPMultiplier` is set to 2.0. Active skill writes are hard-clamped to 50 to stop the simultaneous-click level-51 exploit.


0.2.9-alpha compatibility fix: Husbandry now hooks RightClickHarvest 4.6.1's `completeHarvest` path directly, while leaving the known-good normal-break crop hook from 0.2.6 unchanged. Skill-level writes now clamp the incoming value argument to 50 before LevelZ stores it, preventing +5/+10 packets from overshooting the cap.

0.2.9-alpha UI pass:
- Main skill screen reflowed to a clean 5-left / 5-right layout.
- The six legacy LevelZ summary readouts at the top are visually removed; Level/Points/XP remain.
- SkillInfoScreen is replaced at render-time with a simple Goosker detail layout: stock/customizable skill icon at upper-left, Goosker skill name, current Level X/50, a concise description of every implemented mechanic, and dynamic Current Bonuses based on the player's actual skill level.
- The detail screen intentionally omits LevelZ's Unlockable Skills and max-level preview sections.
- Existing LevelZ background textures and tabs are retained so the future resource pack can reskin them without changing mechanics.

0.3.0-alpha UI detail pass:
- Skill detail descriptions are pre-wrapped to stay inside LevelZ's 200px panel.
- Adds a built-in dark skill-detail backdrop based on the approved UI mockup, with subtle dividers and a top-right back-arrow button.
- Clicking the back arrow returns directly to the main LevelZ skill screen, so the LevelZ menu does not need to be keybound for navigation.
- Keeps all 0.2.8/0.2.9 mechanics unchanged.


0.3.1-alpha packaging fix:
- Removes accidentally bundled Minecraft/Fabric/Mixin compile-time stub classes from 0.3.0. Those stubs shadowed the real Minecraft 1.20.1 classes and caused a startup NoSuchMethodError in class_5819 / MathHelper before the game reached the menu.
- UI/features are otherwise unchanged from 0.3.0.


0.3.2-alpha packaging/inventory-tab fix:
- 0.3.1 still accidentally bundled the compile-time LibZ DrawTabHelper stub even after the Minecraft/Fabric stubs were removed.
- That shadowed LibZ's real DrawTabHelper at runtime and could remove/break the inventory-to-LevelZ tab buttons.
- The installable jar now contains only Goosker classes/resources; no net.minecraft, Fabric, Mixin, or LibZ compile-time stubs are packaged.
- Skill-detail UI, wrapped text, custom backdrop, back-arrow behavior, and all 0.2.8/0.2.9 gameplay mechanics are retained.

0.3.4-alpha UI/compatibility pass:
- Removes the outdated LevelZ hover-description tooltips from the ten skill icons; the full click-through skill description pages are now the source of truth.
- Adds optional Flow 2.2.0 compatibility: opening inventories normally still uses Flow, but switching specifically from LevelZ/skill-info back to the vanilla inventory suppresses the redundant slide-up transition.
- Adds optional Satisfying Buttons 1.1.2 compatibility for LevelZ's custom skill/+ widgets, which bypass vanilla AbstractButtonWidget.renderWidget; hover/unhover sounds use Satisfying Buttons' own configured sound events, pitch, volume, and enable settings.
- The skill-info back arrow also uses the Satisfying Buttons hover sound when that mod is installed.
- Skill-info divider rules are now positioned dynamically around the description/current-bonus sections instead of being baked at fixed Y coordinates.
- Main top summary is repainted after LevelZ's legacy UI so the old six-stat readout/title cannot bleed through; Level/Points and a custom XP bar remain.

0.3.5-alpha UI/compatibility fix:
- Fixes LibZ inventory tab becoming unclickable after leveling a skill. LevelZ leaves the + widget focused, and LibZ intentionally refuses tab clicks while any child is focused; this build forces the raw inventory tab to remain usable.
- Restores a menu click sound on the raw inventory tab and the custom skill-info back arrow.
- Replaces the previous reflective Satisfying Buttons playback with a direct bridge to Satisfying Buttons 1.1.2's own BUTTON_HOVER/BUTTON_HOVER_REVERSE events and client pitch/volume settings.
- Adds Satisfying Buttons hover audio to the raw inventory tab while on LevelZ screens.
- Flow suppression now applies in both directions between vanilla Inventory and LevelZ, while normal inventory opening/closing keeps Flow enabled.
- Cleans the main 5x2 card area: hides LevelZ's unused sixth-row remnants, shifts text away from row borders, and moves the right-column + buttons outward so long names (especially Husbandry) do not collide with them.
- Restores the centered-diamond divider style on skill-info pages.
- The back arrow is now a separate exact-pixel 22x22 asset instead of being baked into a scaled 256px background, fixing its skew/alignment.

0.3.6-alpha UI polish pass:
- Main 5x2 skill cards now use 24px row spacing, giving the skill name and level their own clear lines instead of sitting on the card borders.
- Skill icon and + button rows follow the same 24px cadence, preserving click targets while cleaning the layout.
- Removes the obsolete LevelZ '?' help widget from the main screen.
- Restores a larger, visibly centered diamond in the skill-info divider rules.
- Inventory-tab click audio is now triggered only after a successful LevelZ -> Inventory screen switch, avoiding hitbox/timing misses.
- Skill-info back-arrow click audio is played after the screen switch and its hover hitbox is slightly more forgiving for Satisfying Buttons compatibility.

0.3.7-alpha UI precision/audio pass:
- Fixes the 5x2 card rectangles by drawing the 200x215 main overlay with its real texture dimensions instead of Minecraft's implicit 256x256 texture size. This was the reason the card geometry was stretched out of alignment even though the text/icon spacing was correct.
- Replaces the skill-detail divider asset with an exact 180x7 reconstruction of the user-provided centered-diamond rule: dark upper line, lighter lower line, and outlined center diamond.
- Adds a LibZ DrawTabHelper mixin that plays one UI click immediately before InventoryTab.onClick, covering both Inventory -> LevelZ and LevelZ -> Inventory tab switches.
- Back-arrow click audio now plays before the screen is replaced, using the stable minecraft:ui.button.click registry id rather than an intermediary SoundEvents field.
- Keeps Satisfying Buttons hover compatibility on the skill widgets, inventory tab, and back arrow; the back-arrow hover/click region is slightly expanded around the 22x22 texture.


0.3.8-alpha alignment/back-button polish:
- Vertically centers all ten skill icons inside their 22px card rows by moving LevelZ's icon widgets up 4px.
- Vertically centers all ten + buttons in the same card rows by moving the 13px button widgets up 4px.
- Keeps the existing two-line skill name/level text positions, which were already centered; icons, labels, and buttons now share one common row center.
- Replaces the bordered back-arrow texture with a flat gray 22x22 button and a thinner white pixel arrow. Hover/click behavior and Satisfying Buttons compatibility are unchanged.


0.3.9-alpha horizontal alignment pass:
- Shifts all ten skill icons 3px left and skill name/level text 5px left within their 90px cards.
- Shifts left-column + buttons 3px left.
- Removes the old +10px right-column button offset, returning those buttons to LevelZ's original x position so they stay fully inside the card rectangles.
- Vertical spacing, click/audio compatibility, skill detail pages, and gameplay mechanics are unchanged from 0.3.8.


0.3.12-alpha skill-info polish:
- Removes the dark inset look behind the Level / Points / XP summary by matching its cover patch to the surrounding LevelZ panel while retaining the separator line.
- Skill detail pages highlight the current skill level in gold and current bonus values (including percentages) in green.
- Adds small attribute icons beside every current-bonus row; Husbandry uses wheat, heart, and XP-orb symbols for produce, twins, and breeding XP.
- Current Bonuses heading is gold to match the approved UI reference.
- Back button is reduced from 22x22 to 12x12 while keeping a larger invisible click/hover target, and restores the compact beveled gray/white-arrow style from the supplied reference.


0.3.12-alpha crash/UI cleanup:
- Fixes the SkillInfoScreen click crash from 0.3.10. The BonusLine helper was compiled as an inner class inside the declared Mixin package; Sponge Mixin correctly forbids directly loading non-mixin helper classes from that package. The helper now lives in the normal com.goosker.levelzoverhaul package.
- Removes the leftover centered separator/floating line from the main Level / Points / XP cover patch.
- Keeps the 0.3.10 gold current-level text, green bonus values, attribute icons, compact back button, and all mechanics/audio/Flow compatibility unchanged.

0.3.12-alpha UI trim/alignment pass:
- Removes the baked legacy large back-button art from the skill-info background; only the compact 12x12 back arrow remains.
- Trims the main skill-grid overlay so its dark backdrop begins exactly at the first row and ends exactly at the fifth row, removing the stray dark strip above Vigor/Archery and the empty dark band below Magicka/Husbandry.
- Moves only the five right-column + buttons 3px to the right to give Husbandry and the other right-column labels more breathing room.

0.3.13-alpha Excalibur icon pass:
- Replaces the ten player-facing LevelZ skill icons with the approved medieval/Excalibur-style set.
- Vigor, Dexterity, and Husbandry use Goosker-supplied custom PNGs.
- Vitality uses Excalibur's chainmail chestplate texture.
- Agility uses Excalibur leather boots with a small motion-streak treatment so the icon reads as movement/running.
- Magicka uses a custom open enchanted-book icon derived from the Excalibur enchanted-book palette.
- Archery combines the Excalibur Epic Knights longbow with an Excalibur arrow in a loaded-bow composition.
- Bartering uses Excalibur's emerald; Mining uses Excalibur's diamond pickaxe.
- Smithing uses a compact original 2D anvil matching the approved flat icon language rather than the 3D block/item model.
- The same icon slots are used automatically on the skill detail page headers.
- Selected Excalibur / Excalibur Epic Knights Compat textures are used and adapted with explicit permission from Maffhew granted August 19, 2026.


## 0.3.14-alpha
- Fixes the 0.3.13 icon pass: 0.3.13 packaged the ten replacement PNGs but did not route LevelZ's live icon widgets to them, so the stock icons still rendered.
- Main SkillScreen now paints the replacement icons directly over LevelZ's clickable 16x16 icon widgets at the final render stage, preserving all existing click behavior.
- SkillInfoScreen header now draws the same replacement icon texture directly.
- Removes the ineffective `assets/levelz/textures/gui/icons.png` override to avoid resource-pack priority ambiguity.


## 0.3.15-alpha
- Rebuilds the ten replacement skill icons at 18x18 instead of 16x16 for slightly cleaner, more readable silhouettes while retaining the Excalibur pixel-art language.
- Adds a subtle dark edge/outline to all ten icons for separation from the dark skill-card background.
- Agility is rebuilt as one forward-leaning Excalibur leather boot with motion streaks instead of the cramped two-boot composition.
- Dexterity is rebuilt from the original Goosker sword PNG at higher source quality so the blade stays straight instead of appearing bent after downscaling.
- Archery now visibly combines the Epic Knights Excalibur longbow at full draw with an Excalibur arrow in the bow.
- Smithing is enlarged/brightened and outlined so the flat 2D anvil reads clearly against the dark panel. Husbandry is enlarged slightly as well.
- Main-grid icon canvases are centered over the existing clickable LevelZ icon widgets, while skill text is nudged 2px right to give every icon a little more breathing room.
- Skill-detail header icons also render at 18x18 for consistency.

## 0.3.16-alpha icon sharpness/layout pass
- Rebuilds all ten main skill icons as crisp nearest-neighbor/native pixel art with no soft drop-shadow pass.
- Magicka is now an original 18x18 open enchanted tome based on the supplied real-art reference, using the approved Excalibur red/cream palette plus a cyan magical plume.
- Agility returns to the original single-running-boot + wind-streak silhouette, using Excalibur's leather-boot pixels.
- Archery uses the Epic Knights Excalibur longbow at full draw with a clearly nocked arrow aimed toward the upper-right.
- Dexterity is redrawn as a straight diagonal blade instead of the bent downsampled sword.
- Vitality, Bartering, and Mining preserve their native Excalibur item pixels on an 18x18 canvas; Smithing is a cleaner high-contrast 2D anvil; Husbandry is re-sharpened from Goosker's custom wheat/hoe icon.
- Rebuilds both 5x2 card columns from the exact same card-border tile so the right-column lower-left highlight/shading matches the left column.
- Main-menu skill names render at 90% scale while retaining the same top-left alignment; skill-level text remains unchanged.


0.3.23-alpha UI micro-fix:
- Changes only the opaque legacy-icon cover patch from 20x20 at (iconX-1, iconY-1) to exactly 18x18 at (iconX, iconY), preventing the gray patch from overlapping the skill-card bevel/shadow.
- Skill icon PNGs, card layout, text, buttons, mechanics, sounds, and compatibility behavior are otherwise unchanged from 0.3.22-alpha.


0.3.24-alpha requested visual-only pass:
- Shifts the LibZ inventory/LevelZ tab strip 5px to the right, including the matching click hitboxes.
- Replaces the Inventory tab icon with the Excalibur bundle and the LevelZ tab icon with the Excalibur/Epic Knights diamond shortsword.
- Replaces only the Current Bonuses icons with the requested Goosker/Excalibur mappings while leaving bonus text, values, layout, mechanics, skill cards, and main skill icons unchanged.


0.3.26-alpha Excalibur compatibility pass:
- Uses the native closed Excalibur Friends & Foes bundle texture for the Inventory tab.
- Uses Excalibur raw iron for Mining bonus-ore chance.
- Uses Minecraft's native Anvil item render for the Smithing anvil-XP bonus, so Excalibur supplies its exact item appearance when that pack is active; the hand-drawn Smithing skill logo is untouched.
- Detects enabled resource-pack ids containing "excalibur". Only while the vanilla Inventory screen is open with Excalibur active, LibZ tabs use the approved +3px alignment and a brown Excalibur-matching tab frame.
- LevelZ/skill-info screens and inventories without Excalibur keep LibZ's default tab position and existing gray/dark resource-pack styling.
- XP bonus art is intentionally unchanged pending the supplied three-orb textures.


0.3.30-alpha Excalibur activation/icon correction:
- Replaces the previous resource-pack-list name check with a live-resource check for Excalibur's unique inventory_effects_chivalry.png, so brown/+3px tabs only apply while Excalibur is actually enabled.
- Inventory screen uses the native open Excalibur/Friends & Foes bundle tab icon.
- LevelZ/skill-info screens use the native closed/filled Excalibur/Friends & Foes bundle tab icon.
- No gameplay, bonus values, card layout, skill logos, bonus icons, or other UI geometry changed.
