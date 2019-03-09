package world.bentobox.bskyblock.generators;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.PerlinOctaveGenerator;

import world.bentobox.bskyblock.BSkyBlock;

/**
 * @author tastybento
 *         Creates the world
 */
public class ChunkGeneratorWorld extends ChunkGenerator {

    BSkyBlock addon;
    Random rand;
    PerlinOctaveGenerator gen;

    /**
     * @param addon - BSkyBlock object
     */
    public ChunkGeneratorWorld(BSkyBlock addon) {
        super();
        this.addon = addon;
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, ChunkGenerator.BiomeGrid biomeGrid) {
        this.rand = random;
        if (world.getEnvironment().equals(World.Environment.NETHER) && addon.getSettings().isNetherRoof()) {
            return generateNetherRoofChunks(world, random);
        }
        ChunkData result = createChunkData(world);
        if (!world.getEnvironment().equals(Environment.NORMAL)) {
            return result;
        }
        Biome bio = addon.getSettings().getDefaultBiome();
        int seaHeight = addon.getSettings().getSeaHeight();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                biomeGrid.setBiome(x, z, bio);
                if (seaHeight != 0) {
                    for (int y = 0; y <= seaHeight; y++) {
                        result.setBlock(x, y, z, Material.WATER);
                    }
                }
            }
        }
        return result;
    }

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
    private ChunkData generateNetherRoofChunks(World world, Random random) {
        ChunkData result = createChunkData(world);
        rand.setSeed(world.getSeed());
        gen = new PerlinOctaveGenerator((long) (random.nextLong() * random.nextGaussian()), 8);
        // Make the roof - common across the world
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // Do the ceiling
                makeCeiling(result, x, z, world.getMaxHeight());
            }
        }
        return result;

    }

    private void makeCeiling(ChunkData result, int x, int z, int maxHeight) {
        result.setBlock(x, (maxHeight - 1), z, Material.BEDROCK);
        // Next three layers are a mix of bedrock and netherrack
        for (int y = 2; y < 5; y++) {
            double r = gen.noise(x, (maxHeight - y), z, 0.5, 0.5);
            if (r > 0D) {
                result.setBlock(x, (maxHeight - y), z, Material.BEDROCK);
            }
        }
        // Next three layers are a mix of netherrack and air
        for (int y = 5; y < 8; y++) {
            double r = gen.noise(x, (double)maxHeight - y, z, 0.5, 0.5);
            if (r > 0D) {
                result.setBlock(x, (maxHeight - y), z, Material.NETHERRACK);
            } else {
                result.setBlock(x, (maxHeight - y), z, Material.AIR);
            }
        }
        // Layer 8 may be glowstone
        doGlowStone(result, maxHeight, x, z);
    }

    private void doGlowStone(ChunkData result, int maxHeight, int x, int z) {
        double r = gen.noise(x, (double)maxHeight - 8, z, rand.nextFloat(), rand.nextFloat());
        if (r < 0.5D) {
            return;
        }
        // Have blobs of glowstone
        switch (rand.nextInt(4)) {
        case 1:
            // Blob type 1
            setBlob1(result, x, maxHeight - 8, z);
            break;
        case 2:
            // Stalactite
            setStalactite(result, x, maxHeight - 8,  z);
            break;
        case 3:
            setBlob2(result, x, maxHeight - 8, z);
            break;
        default:
            result.setBlock(x, (maxHeight - 8), z, Material.GLOWSTONE);
        }
        result.setBlock(x, (maxHeight - 8), z, Material.GLOWSTONE);
    }

    private void setBlob2(ChunkData result, int x, int y, int z) {
        result.setBlock(x, y, z, Material.GLOWSTONE);
        if (x > 3 && z > 3) {
            for (int xx = 0; xx < 3; xx++) {
                for (int zz = 0; zz < 3; zz++) {
                    result.setBlock(x - xx, y - rand.nextInt(2), z - xx, Material.GLOWSTONE);
                }
            }
        }
    }

    private void setStalactite(ChunkData result, int x, int y, int z) {
        for (int i = 0; i < rand.nextInt(10); i++) {
            result.setBlock(x, y - i, z, Material.GLOWSTONE);
        }
    }

    private void setBlob1(ChunkData result, int x, int y, int z) {
        result.setBlock(x, y, z, Material.GLOWSTONE);
        if (x < 14 && z < 14) {
            result.setBlock(x + 1, y, z + 1, Material.GLOWSTONE);
            result.setBlock(x + 2, y, z + 2, Material.GLOWSTONE);
            result.setBlock(x + 1, y, z + 2, Material.GLOWSTONE);
            result.setBlock(x + 1, y, z + 2, Material.GLOWSTONE);
        }
    }
}