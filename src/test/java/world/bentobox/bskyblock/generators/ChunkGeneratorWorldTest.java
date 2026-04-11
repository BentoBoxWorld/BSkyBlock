package world.bentobox.bskyblock.generators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import world.bentobox.bskyblock.BSkyBlock;
import world.bentobox.bskyblock.CommonTestSetup;
import world.bentobox.bskyblock.Settings;

/**
 * @author tastybento
 *
 */
public class ChunkGeneratorWorldTest extends CommonTestSetup {

    @Mock
    private BSkyBlock addon;
    private ChunkGeneratorWorld cg;
    @Mock
    private World cgWorld;
    private final Random random = new Random();
    private Settings settings;
    @Mock
    private ChunkData data;

    /**
     * @throws java.lang.Exception - exception
     */
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        // Instance
        cg = new ChunkGeneratorWorld(addon);
        // World
        when(cgWorld.getEnvironment()).thenReturn(World.Environment.NORMAL);
        when(cgWorld.getMaxHeight()).thenReturn(16);
        // Settings
        settings = new Settings();
        when(addon.getSettings()).thenReturn(settings);
    }

    @Override
    @AfterEach
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link ChunkGeneratorWorld#generateNoise} in the overworld with no sea (void).
     */
    @Test
    void testGenerateNoiseOverworldVoid() {
        cg.generateNoise(cgWorld, random, 0, 0, data);
        // Void - no water fill
        verify(data, never()).setRegion(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), any(Material.class));
    }

    /**
     * Test method for {@link ChunkGeneratorWorld#generateNoise} in the overworld with a sea.
     */
    @Test
    void testGenerateNoiseOverworldSea() {
        // Set sea height
        settings.setSeaHeight(10);
        cg.generateNoise(cgWorld, random, 0, 0, data);
        // Water. Blocks = 16 x 16 x 11 because block 0
        verify(data).setRegion(0, 0, 0, 16, 11, 16, Material.WATER);
    }

    /**
     * Test method for {@link ChunkGeneratorWorld#getDefaultBiomeProvider} in the overworld.
     */
    @Test
    void testGetDefaultBiomeProviderOverworld() {
        BiomeProvider provider = cg.getDefaultBiomeProvider(cgWorld);
        Biome biome = provider.getBiome(cgWorld, 0, 0, 0);
        assertEquals("plains", biome.getKey().getKey());
    }

    /**
     * Test method for {@link ChunkGeneratorWorld#getDefaultBiomeProvider} in the End.
     */
    @Test
    void testGetDefaultBiomeProviderEnd() {
        when(cgWorld.getEnvironment()).thenReturn(World.Environment.THE_END);
        BiomeProvider provider = cg.getDefaultBiomeProvider(cgWorld);
        Biome biome = provider.getBiome(cgWorld, 0, 0, 0);
        assertEquals("the_end", biome.getKey().getKey());
    }

    /**
     * Test method for {@link ChunkGeneratorWorld#generateNoise} in the End (void).
     */
    @Test
    void testGenerateNoiseEnd() {
        when(cgWorld.getEnvironment()).thenReturn(World.Environment.THE_END);
        cg.generateNoise(cgWorld, random, 0, 0, data);
        // Void - no region fill
        verify(data, never()).setRegion(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), any(Material.class));
    }

    /**
     * Test method for {@link ChunkGeneratorWorld#getDefaultBiomeProvider} in the Nether.
     */
    @Test
    void testGetDefaultBiomeProviderNether() {
        when(cgWorld.getEnvironment()).thenReturn(World.Environment.NETHER);
        BiomeProvider provider = cg.getDefaultBiomeProvider(cgWorld);
        Biome biome = provider.getBiome(cgWorld, 0, 0, 0);
        assertEquals("nether_wastes", biome.getKey().getKey());
    }

    /**
     * Test method for {@link ChunkGeneratorWorld#generateNoise} in the Nether with a roof.
     */
    @Test
    void testGenerateNoiseNetherWithRoof() {
        when(cgWorld.getEnvironment()).thenReturn(World.Environment.NETHER);
        cg.generateNoise(cgWorld, random, 0, 0, data);
        // Nether roof - at least bedrock layer
        verify(data, atLeast(64)).setBlock(anyInt(), anyInt(), anyInt(), eq(Material.BEDROCK));
    }

    /**
     * Test method for {@link ChunkGeneratorWorld#generateNoise} in the Nether with no roof.
     */
    @Test
    void testGenerateNoiseNetherNoRoof() {
        settings.setNetherRoof(false);
        when(cgWorld.getEnvironment()).thenReturn(World.Environment.NETHER);
        cg.generateNoise(cgWorld, random, 0, 0, data);
        // No nether roof blocks set
        verify(data, never()).setBlock(anyInt(), anyInt(), anyInt(), any(Material.class));
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.generators.ChunkGeneratorWorld#canSpawn(org.bukkit.World, int, int)}.
     */
    @Test
    void testCanSpawnWorldIntInt() {
        assertTrue(cg.canSpawn(mock(World.class), 0, 1));
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.generators.ChunkGeneratorWorld#getDefaultPopulators(org.bukkit.World)}.
     */
    @Test
    void testGetDefaultPopulatorsWorld() {
        assertTrue(cg.getDefaultPopulators(mock(World.class)).isEmpty());
    }

    /**
     * Test method for {@link ChunkGeneratorWorld#shouldGenerateStructures}.
     */
    @Test
    void testShouldGenerateStructures() {
        assertFalse(cg.shouldGenerateStructures(cgWorld, random, 0, 0));
    }

    /**
     * Test method for {@link ChunkGeneratorWorld#shouldGenerateMobs}.
     */
    @Test
    void testShouldGenerateMobs() {
        assertTrue(cg.shouldGenerateMobs(cgWorld, random, 0, 0));
    }

    /**
     * Test method for the BiomeProvider's {@code getBiomes} list in each environment.
     */
    @Test
    void testGetBiomesOverworld() {
        BiomeProvider provider = cg.getDefaultBiomeProvider(cgWorld);
        var biomes = provider.getBiomes(cgWorld);
        assertEquals(1, biomes.size());
        assertEquals("plains", biomes.get(0).getKey().getKey());
    }

    @Test
    void testGetBiomesNether() {
        when(cgWorld.getEnvironment()).thenReturn(World.Environment.NETHER);
        BiomeProvider provider = cg.getDefaultBiomeProvider(cgWorld);
        var biomes = provider.getBiomes(cgWorld);
        assertEquals(1, biomes.size());
        assertEquals("nether_wastes", biomes.get(0).getKey().getKey());
    }

    @Test
    void testGetBiomesEnd() {
        when(cgWorld.getEnvironment()).thenReturn(World.Environment.THE_END);
        BiomeProvider provider = cg.getDefaultBiomeProvider(cgWorld);
        var biomes = provider.getBiomes(cgWorld);
        assertEquals(1, biomes.size());
        assertEquals("the_end", biomes.get(0).getKey().getKey());
    }
}
