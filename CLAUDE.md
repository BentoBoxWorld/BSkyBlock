# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

BSkyBlock is a SkyBlock game mode addon for BentoBox, a Minecraft Bukkit/Spigot island management framework. It extends `GameModeAddon` to provide sky-based island worlds with custom chunk generation.

## Build Commands

```bash
mvn clean package          # Build the JAR
mvn test                   # Run all tests
mvn clean verify           # Run tests with JaCoCo coverage reports
```

Maven profiles control versioning: CI sets `-b${BUILD_NUMBER}`, master branch removes `-SNAPSHOT`. Java 17 required.

## Architecture

**Entry points:**
- `BSkyBlockPladdon` — Spigot plugin entry point (Pladdon lazy-loading pattern)
- `BSkyBlock` — Core addon extending `GameModeAddon`, manages lifecycle (`onLoad`/`onEnable`/`onDisable`/`onReload`), registers commands, creates Overworld/Nether/End worlds

**Key classes:**
- `Settings` — Large configuration class (~100+ properties) with BentoBox `@ConfigEntry`/`@ConfigComment` annotations, extends `WorldSettings`
- `ChunkGeneratorWorld` — Custom `ChunkGenerator` for sky worlds, handles Nether roof generation, sea level, biome placement
- `IslandAboutCommand` — Simple composite command ("about"/"ab")

**Package:** `world.bentobox.bskyblock`

## Testing

Uses JUnit 4 + Mockito + PowerMock (for static mocking). Test classes mirror main structure under `src/test/java/`. `ServerMocks` utility provides Bukkit mock setup.

Surefire is configured with `--add-opens` JVM args for Java 17 module access needed by PowerMock reflection.

## Key Dependencies

- **Spigot API 1.21.3** — Minecraft server API (`provided` scope)
- **BentoBox 2.7.1** — Core framework (`provided` scope, addon API version in `addon.yml`)

## Resources

- `addon.yml` — BentoBox addon descriptor (permissions, API version, icon)
- `plugin.yml` — Spigot plugin descriptor
- `config.yml` — Default configuration (island settings, world properties, biomes, game rules)
- `locales/` — 22 language files
- `blueprints/` — Island blueprint `.blu` and `.json` files
