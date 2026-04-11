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

## Dependency Source Lookup

When you need to inspect source code for a dependency (e.g., BentoBox, addons):

1. **Check local Maven repo first**: `~/.m2/repository/` — sources jars are named `*-sources.jar`
2. **Check the workspace**: Look for sibling directories or Git submodules that may contain the dependency as a local project (e.g., `../bentoBox`, `../addon-*`)
3. **Check Maven local cache for already-extracted sources** before downloading anything
4. Only download a jar or fetch from the internet if the above steps yield nothing useful

Prefer reading `.java` source files directly from a local Git clone over decompiling or extracting a jar.

In general, the latest version of BentoBox should be targeted.

## Project Layout

Related projects are checked out as siblings under `~/git/`:

**Core:**
- `bentobox/` — core BentoBox framework

**Game modes:**
- `addon-acidisland/` — AcidIsland game mode
- `addon-bskyblock/` — BSkyBlock game mode
- `Boxed/` — Boxed game mode (expandable box area)
- `CaveBlock/` — CaveBlock game mode
- `OneBlock/` — AOneBlock game mode
- `SkyGrid/` — SkyGrid game mode
- `RaftMode/` — Raft survival game mode
- `StrangerRealms/` — StrangerRealms game mode
- `Brix/` — plot game mode
- `parkour/` — Parkour game mode
- `poseidon/` — Poseidon game mode
- `gg/` — gg game mode

**Addons:**
- `addon-level/` — island level calculation
- `addon-challenges/` — challenges system
- `addon-welcomewarpsigns/` — warp signs
- `addon-limits/` — block/entity limits
- `addon-invSwitcher/` / `invSwitcher/` — inventory switcher
- `addon-biomes/` / `Biomes/` — biomes management
- `Bank/` — island bank
- `Border/` — world border for islands
- `Chat/` — island chat
- `CheckMeOut/` — island submission/voting
- `ControlPanel/` — game mode control panel
- `Converter/` — ASkyBlock to BSkyBlock converter
- `DimensionalTrees/` — dimension-specific trees
- `discordwebhook/` — Discord integration
- `Downloads/` — BentoBox downloads site
- `DragonFights/` — per-island ender dragon fights
- `ExtraMobs/` — additional mob spawning rules
- `FarmersDance/` — twerking crop growth
- `GravityFlux/` — gravity addon
- `Greenhouses-addon/` — greenhouse biomes
- `IslandFly/` — island flight permission
- `IslandRankup/` — island rankup system
- `Likes/` — island likes/dislikes
- `Limits/` — block/entity limits
- `lost-sheep/` — lost sheep adventure
- `MagicCobblestoneGenerator/` — custom cobblestone generator
- `PortalStart/` — portal-based island start
- `pp/` — pp addon
- `Regionerator/` — region management
- `Residence/` — residence addon
- `TopBlock/` — top ten for OneBlock
- `TwerkingForTrees/` — twerking tree growth
- `Upgrades/` — island upgrades (Vault)
- `Visit/` — island visiting
- `weblink/` — web link addon
- `CrowdBound/` — CrowdBound addon

**Data packs:**
- `BoxedDataPack/` — advancement datapack for Boxed

**Documentation & tools:**
- `docs/` — main documentation site
- `docs-chinese/` — Chinese documentation
- `docs-french/` — French documentation
- `BentoBoxWorld.github.io/` — GitHub Pages site
- `website/` — website
- `translation-tool/` — translation tool

Check these for source before any network fetch.

## Key Dependencies (source locations)

- `world.bentobox:bentobox` → `~/git/bentobox/src/`
