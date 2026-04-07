# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

BSkyBlock is a SkyBlock game mode addon for BentoBox, a Minecraft Bukkit/Paper island management framework. It extends `GameModeAddon` to provide sky-based island worlds with custom chunk generation.

## Build Commands

```bash
mvn clean package          # Build the JAR
mvn test                   # Run all tests
mvn clean verify           # Run tests with JaCoCo coverage reports
```

Maven profiles control versioning: CI sets `-b${BUILD_NUMBER}`, master branch removes `-SNAPSHOT`. Java 21 required.

## Architecture

**Entry points:**
- `BSkyBlockPladdon` — Paper plugin entry point (Pladdon lazy-loading pattern)
- `BSkyBlock` — Core addon extending `GameModeAddon`, manages lifecycle (`onLoad`/`onEnable`/`onDisable`/`onReload`), registers commands, creates Overworld/Nether/End worlds

**Key classes:**
- `Settings` — Large configuration class (~100+ properties) with BentoBox `@ConfigEntry`/`@ConfigComment` annotations, extends `WorldSettings`
- `ChunkGeneratorWorld` — Custom `ChunkGenerator` for sky worlds, handles Nether roof generation, sea level, biome placement
- `IslandAboutCommand` — Simple composite command ("about"/"ab")

**Package:** `world.bentobox.bskyblock`

## Testing

Uses JUnit 5 (Jupiter) + Mockito + MockBukkit. Test classes mirror main structure under `src/test/java/`. The `CommonTestSetup` base class boots MockBukkit, opens Mockito annotations, installs `MockedStatic<Bukkit>` / `MockedStatic<Util>`, and wires up the BentoBox plugin, IWM, IslandsManager, Player, World, etc. `WhiteBox` is a small reflection helper that replaces PowerMock's `Whitebox`.

Surefire is configured with `--add-opens` JVM args for Java 21 module access (needed by Mockito inline mock maker and JaCoCo).

### Test patterns and gotchas

- **Always extend `CommonTestSetup`** for tests that touch Bukkit/BentoBox state. Override `setUp`/`tearDown` with `@BeforeEach`/`@AfterEach` and call `super.setUp()` / `super.tearDown()` first/last.
- **Per-test `MockedStatic`** (e.g. `DatabaseSetup`) should be created in `setUp` *after* `super.setUp()` and closed in `tearDown` *before* `super.tearDown()` via `closeOnDemand()`. See `BSkyBlockTest` for the pattern.
- **`Bukkit::getServer` is already stubbed** by `CommonTestSetup` to return MockBukkit's `ServerMock`. If a test needs a Mockito-controlled server (e.g. to stub `createChunkData()`), re-stub it after `super.setUp()`: `mockedBukkit.when(Bukkit::getServer).thenReturn(myMockServer);` — `ChunkGeneratorWorldTest` does this.
- **Do not use `eq(Biome.X)` (or any registry-backed `Keyed` constant) in `verify`.** Under MockBukkit, `Biome.TAIGA` resolves through the mocked registry and may return distinct `BiomeMock` instances per call site, so identity-based `eq()` matching fails. Use `argThat(b -> "taiga".equals(b.getKey().getKey()))` instead. The same caution applies to other `Keyed` registries (Material/ItemType/BlockType/Tag/etc.).
- **Mind the Settings defaults when verifying chunk-generator output.** `ChunkGeneratorWorld` reads biomes from `Settings.getDefaultBiome()` / `getDefaultNetherBiome()` / `getDefaultEndBiome()` (defaults: PLAINS / NETHER_WASTES / THE_END), not from hard-coded constants. The pre-migration tests asserted TAIGA / END_MIDLANDS / CRIMSON_FOREST and only "passed" because PowerMock null-mocked every enum constant.

### Build gotcha: maven-compiler-plugin on JDK 25

`maven-compiler-plugin` 3.13.0 / 3.14.0 in non-forked mode crashes on JDK 25 with `Fatal error compiling: Cannot load from object array because "this.hashes" is null` while *formatting* a deprecation diagnostic. The actual compile is fine — only the in-process plexus diagnostic formatter throws. The POM works around this by setting `<fork>true</fork>` on the compiler plugin. CI on JDK 21 is not affected, but the fork is harmless there. If you ever see that NPE, do not assume there is a real compile error — try forking first, or run `javac` directly to surface the truth.

## Key Dependencies

- **Paper API 1.21.11** — Minecraft server API (`provided` scope)
- **BentoBox 3.14.0-SNAPSHOT** — Core framework (`provided` scope, addon API version in `addon.yml`)
- **MockBukkit v1.21-SNAPSHOT** — server mock used in tests (via JitPack)

## Resources

- `addon.yml` — BentoBox addon descriptor (permissions, API version, icon)
- `plugin.yml` — Paper plugin descriptor
- `config.yml` — Default configuration (island settings, world properties, biomes, game rules)
- `locales/` — 22 language files
- `blueprints/` — Island blueprint `.blu` and `.json` files
