# Validation

`BreedingBonusTest.java` is a standalone test requiring only `BreedingBonus.java` and Java 17. Compile both into a temporary directory and run `BreedingBonusTest`.

`BalanceValuesTest.java` checks ore probability and new maximums using only BalanceValues.java and Java 17.

`RuntimeTest.java` is an isolated Fabric server test entrypoint. Package it as a temporary test mod with `entrypoints.main = ["levelztest.RuntimeTest"]`, and use the release JAR plus its normal dependencies. It creates test cows and a test player, checks ore/Fortune/Silk Touch, crop produce, exhaustion, toughness, actual cooking/breeding XP, offspring counts at progression thresholds, and unattributed breeding, and exits the server. Use a disposable world.

`ClientTransformTest.java` is an isolated client `preLaunch` entrypoint, `levelztest.ClientTransformTest`. Include Satisfying Buttons 1.1.2 in the client test mods. It transforms the patched screen/tab/animal/player/block/button classes without initializing a graphics window, writes CLIENT_PASS on success, and exits. This checks mixin targets, not visual rendering.

No test mod or test world is included in the release JAR.

`BlockXpConfigTest.java` checks default policies, exact IDs, wildcards, empty lists, and malformed JSON preservation. Run as `com.goosker.levelzoverhaul.BlockXpConfigTest` with Gson, Fabric Loader, and SLF4J on the classpath.

`BlockXpRuntimeTest.java` is a disposable Fabric server entrypoint (`com.goosker.levelzoverhaul.BlockXpRuntimeTest`) checking the actual transformed block XP method, both orb types, runtime config changes, and doTileDrops. It writes TEST_PASS or TEST_FAIL and exits. Never run this test in a production world; it creates orbs and edits config.
