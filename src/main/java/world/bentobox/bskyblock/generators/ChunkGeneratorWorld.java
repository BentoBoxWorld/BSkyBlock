package world.bentobox.bskyblock.generators;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.Vector;
import org.bukkit.util.noise.PerlinOctaveGenerator;

import org.jspecify.annotations.NonNull;
import world.bentobox.bskyblock.BSkyBlock;

/**
 * @author tastybento
 *         Creates the world
 */
public class ChunkGeneratorWorld extends ChunkGenerator {

    private final BSkyBlock addon;
    private final Random rand = new Random();
    private final Map<Vector, Material> roofChunk = new HashMap<>();

    /**
     * @param addon - addon
     */
    public ChunkGeneratorWorld(BSkyBlock addon) {
        super();
        this.addon = addon;
        makeNetherRoof();
    }

    @Override
    public boolean shouldGenerateStructures(@NonNull WorldInfo worldInfo, @NonNull Random random, int chunkX, int chunkZ) {
        return false;
    }

    @Override
    public void generateNoise(@NonNull WorldInfo worldInfo, @NonNull Random random, int chunkX, int chunkZ, @NonNull ChunkData chunkData) {
        if (worldInfo.getEnvironment() == Environment.NORMAL && addon.getSettings().getSeaHeight() > 0) {
            chunkData.setRegion(0, worldInfo.getMinHeight(), 0, 16, addon.getSettings().getSeaHeight() + 1, 16, Material.WATER);
        }
        if (worldInfo.getEnvironment() == Environment.NETHER && addon.getSettings().isNetherRoof()) {
            roofChunk.forEach((k, v) -> chunkData.setBlock(k.getBlockX(), worldInfo.getMaxHeight() + k.getBlockY(), k.getBlockZ(), v));
        }
    }

    @Override
    public BiomeProvider getDefaultBiomeProvider(@NonNull WorldInfo worldInfo) {
        return new BiomeProvider() {
            @Override
            public @NonNull Biome getBiome(@NonNull WorldInfo worldInfo, int x, int y, int z) {
                return defaultBiomeFor(worldInfo.getEnvironment());
            }

            @Override
            public @NonNull List<Biome> getBiomes(@NonNull WorldInfo worldInfo) {
                return List.of(defaultBiomeFor(worldInfo.getEnvironment()));
            }
        };
    }

    private Biome defaultBiomeFor(Environment env) {
        return switch (env) {
            case NORMAL -> addon.getSettings().getDefaultBiome();
            case NETHER -> addon.getSettings().getDefaultNetherBiome();
            default -> addon.getSettings().getDefaultEndBiome();
        };
    }

    // This needs to be set to return true to override minecraft's default behavior
    @Override
    public boolean canSpawn(@NonNull World world, int x, int z) {
        return true;
    }

    @Override
    public boolean shouldGenerateMobs(@NonNull WorldInfo worldInfo, @NonNull Random random, int chunkX, int chunkZ) {
        return true;
    }

    @Override
    public @NonNull List<BlockPopulator> getDefaultPopulators(final @NonNull World world) {
        return Collections.emptyList();
    }

    /*
     * Nether Section
     */
    private void makeNetherRoof() {
        rand.setSeed(System.currentTimeMillis());
        PerlinOctaveGenerator gen = new PerlinOctaveGenerator((long) (rand.nextLong() * rand.nextGaussian()), 8);

        // Make the roof - common across the world
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                fillRoofColumn(gen, x, z);
            }
        }
    }

    private void fillRoofColumn(PerlinOctaveGenerator gen, int x, int z) {
        // Ceiling
        setBlock(x, -1, z, Material.BEDROCK);
        // Bedrock/netherrack mix
        for (int y = 2; y < 5; y++) {
            if (gen.noise(x, -y, z, 0.5, 0.5) > 0D) {
                setBlock(x, -y, z, Material.BEDROCK);
            }
        }
        // Netherrack/air mix
        for (int y = 5; y < 8; y++) {
            Material m = gen.noise(x, -y, z, 0.5, 0.5) > 0D ? Material.NETHERRACK : Material.AIR;
            setBlock(x, -y, z, m);
        }
        // Layer 8 may be glowstone
        if (gen.noise(x, -8, z, rand.nextFloat(), rand.nextFloat()) > 0.5D) {
            placeGlowstoneBlob(x, z);
        } else {
            setBlock(x, -8, z, Material.AIR);
        }
    }

    private void placeGlowstoneBlob(int x, int z) {
        switch (rand.nextInt(4)) {
        case 1 -> placeGlowstoneCluster(x, z);
        case 2 -> placeGlowstoneStalactite(x, z);
        case 3 -> placeGlowstonePatch(x, z);
        default -> setBlock(x, -8, z, Material.GLOWSTONE);
        }
        setBlock(x, -8, z, Material.GLOWSTONE);
    }

    private void placeGlowstoneCluster(int x, int z) {
        setBlock(x, -8, z, Material.GLOWSTONE);
        if (x < 14 && z < 14) {
            setBlock(x + 1, -8, z + 1, Material.GLOWSTONE);
            setBlock(x + 2, -8, z + 2, Material.GLOWSTONE);
            setBlock(x + 1, -8, z + 2, Material.GLOWSTONE);
        }
    }

    private void placeGlowstoneStalactite(int x, int z) {
        for (int i = 0; i < rand.nextInt(10); i++) {
            setBlock(x, -8 - i, z, Material.GLOWSTONE);
        }
    }

    private void placeGlowstonePatch(int x, int z) {
        setBlock(x, -8, z, Material.GLOWSTONE);
        if (x > 3 && z > 3) {
            for (int xx = 0; xx < 3; xx++) {
                for (int zz = 0; zz < 3; zz++) {
                    setBlock(x - xx, -8 - rand.nextInt(2), z - xx, Material.GLOWSTONE);
                }
            }
        }
    }

    private void setBlock(int x, int y, int z, Material m) {
        roofChunk.put(new Vector(x, y, z), m);
    }
}