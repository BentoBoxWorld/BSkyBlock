package dev.viaduct.factories.generators;

import dev.viaduct.factories.Factories;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.Vector;
import org.bukkit.util.noise.PerlinOctaveGenerator;

import java.util.*;

/**
 * @author tastybento
 * Creates the world
 */
public class ChunkGeneratorWorld extends ChunkGenerator {

    private final Factories addon;
    private final Random rand = new Random();
    private final Map<Vector, Material> roofChunk = new HashMap<>();

    /**
     * @param addon - addon
     */
    public ChunkGeneratorWorld(Factories addon) {
        super();
        this.addon = addon;
        makeNetherRoof();
    }

    public ChunkData generateChunks(World world) {
        ChunkData result = createChunkData(world);
        if (world.getEnvironment().equals(Environment.NORMAL) && addon.getSettings().getSeaHeight() > 0) {
            result.setRegion(0, world.getMinHeight(), 0, 16, addon.getSettings().getSeaHeight() + 1, 16, Material.WATER);
        }
        if (world.getEnvironment().equals(Environment.NETHER) && addon.getSettings().isNetherRoof()) {
            roofChunk.forEach((k, v) -> result.setBlock(k.getBlockX(), world.getMaxHeight() + k.getBlockY(), k.getBlockZ(), v));
        }
        return result;
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
        setBiome(world, biomeGrid);
        return generateChunks(world);
    }

    private void setBiome(World world, BiomeGrid biomeGrid) {
        Biome biome = world.getEnvironment() == Environment.NORMAL ? addon.getSettings().getDefaultBiome() :
                world.getEnvironment() == Environment.NETHER ? addon.getSettings().getDefaultNetherBiome() : addon.getSettings().getDefaultEndBiome();
        for (int x = 0; x < 16; x += 4) {
            for (int z = 0; z < 16; z += 4) {
                for (int y = world.getMinHeight(); y < world.getMaxHeight(); y += 4) {
                    biomeGrid.setBiome(x, y, z, biome);
                }
            }
        }

    }

    // This needs to be set to return true to override minecraft's default
    // behavior
    @Override
    public boolean canSpawn(World world, int x, int z) {
        return true;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(final World world) {
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
                // Do the ceiling
                setBlock(x, -1, z, Material.BEDROCK);
                // Next three layers are a mix of bedrock and netherrack
                for (int y = 2; y < 5; y++) {
                    double r = gen.noise(x, -y, z, 0.5, 0.5);
                    if (r > 0D) {
                        setBlock(x, -y, z, Material.BEDROCK);
                    }
                }
                // Next three layers are a mix of netherrack and air
                for (int y = 5; y < 8; y++) {
                    double r = gen.noise(x, -y, z, 0.5, 0.5);
                    if (r > 0D) {
                        setBlock(x, -y, z, Material.NETHERRACK);
                    } else {
                        setBlock(x, -y, z, Material.AIR);
                    }
                }
                // Layer 8 may be glowstone
                double r = gen.noise(x, -8, z, rand.nextFloat(), rand.nextFloat());
                if (r > 0.5D) {
                    // Have blobs of glowstone
                    switch (rand.nextInt(4)) {
                        case 1:
                            // Single block
                            setBlock(x, -8, z, Material.GLOWSTONE);
                            if (x < 14 && z < 14) {
                                setBlock(x + 1, -8, z + 1, Material.GLOWSTONE);
                                setBlock(x + 2, -8, z + 2, Material.GLOWSTONE);
                                setBlock(x + 1, -8, z + 2, Material.GLOWSTONE);
                                setBlock(x + 1, -8, z + 2, Material.GLOWSTONE);
                            }
                            break;
                        case 2:
                            // Stalactite
                            for (int i = 0; i < rand.nextInt(10); i++) {
                                setBlock(x, -8 - i, z, Material.GLOWSTONE);
                            }
                            break;
                        case 3:
                            setBlock(x, -8, z, Material.GLOWSTONE);
                            if (x > 3 && z > 3) {
                                for (int xx = 0; xx < 3; xx++) {
                                    for (int zz = 0; zz < 3; zz++) {
                                        setBlock(x - xx, -8 - rand.nextInt(2), z - xx, Material.GLOWSTONE);
                                    }
                                }
                            }
                            break;
                        default:
                            setBlock(x, -8, z, Material.GLOWSTONE);
                    }
                    setBlock(x, -8, z, Material.GLOWSTONE);
                } else {
                    setBlock(x, -8, z, Material.AIR);
                }
            }

        }
    }

    private void setBlock(int x, int y, int z, Material m) {
        roofChunk.put(new Vector(x, y, z), m);
    }
}