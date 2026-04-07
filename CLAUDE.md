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
