package world.bentobox.bskyblock.generators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
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
    @SuppressWarnings("deprecation")
    @Mock
    private BiomeGrid biomeGrid;
    private Settings settings;
    @Mock
    private ChunkData data;

    /**
     * @throws java.lang.Exception
     */
    @SuppressWarnings("deprecation")
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        // Re-stub Bukkit.getServer() with a Mockito mock so we can control createChunkData()
        Server server = mock(Server.class);
        when(server.createChunkData(any())).thenReturn(data);
        mockedBukkit.when(Bukkit::getServer).thenReturn(server);

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
     * Test method for {@link world.bentobox.bskyblock.generators.ChunkGeneratorWorld#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}.
     */
    @SuppressWarnings("deprecation")
    @Test
    void testGenerateChunkDataWorldRandomIntIntBiomeGridOverworldVoid() {
        ChunkData cd = cg.generateChunkData(cgWorld, random, 0, 0, biomeGrid);
        assertEquals(data, cd);
        // Verifications
        verify(biomeGrid, times(64)).setBiome(anyInt(), anyInt(), anyInt(), any());
        // Void
        verify(cd, never()).setRegion(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), any(Material.class));
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.generators.ChunkGeneratorWorld#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}.
     */
    @SuppressWarnings("deprecation")
    @Test
    void testGenerateChunkDataWorldRandomIntIntBiomeGridOverworldSea() {
        // Set sea height
        settings.setSeaHeight(10);
        ChunkData cd = cg.generateChunkData(cgWorld, random, 0, 0, biomeGrid);
        assertEquals(data, cd);
        // Verifications
        verify(biomeGrid, times(64)).setBiome(anyInt(), anyInt(), anyInt(),
                argThat(b -> b != null && "plains".equals(b.getKey().getKey())));
        // Water. Blocks = 16 x 16 x 11 because block 0
        verify(cd).setRegion(0, 0, 0, 16, 11, 16, Material.WATER);
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.generators.ChunkGeneratorWorld#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}.
     */
    @SuppressWarnings("deprecation")
    @Test
    void testGenerateChunkDataWorldRandomIntIntBiomeGridEnd() {
        when(cgWorld.getEnvironment()).thenReturn(World.Environment.THE_END);
        ChunkData cd = cg.generateChunkData(cgWorld, random, 0, 0, biomeGrid);
        assertEquals(data, cd);
        // Verifications
        // Set biome in end
        verify(biomeGrid, times(64)).setBiome(anyInt(), anyInt(), anyInt(),
                argThat(b -> b != null && "the_end".equals(b.getKey().getKey())));
        // Void
        verify(cd, never()).setRegion(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), any(Material.class));
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.generators.ChunkGeneratorWorld#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}.
     */
    @SuppressWarnings("deprecation")
    @Test
    void testGenerateChunkDataWorldRandomIntIntBiomeGridNetherWithRoof() {
        when(cgWorld.getEnvironment()).thenReturn(World.Environment.NETHER);
        ChunkData cd = cg.generateChunkData(cgWorld, random, 0, 0, biomeGrid);
        assertEquals(data, cd);
        // Verifications
        // Set biome in nether
        verify(biomeGrid, times(64)).setBiome(anyInt(), anyInt(), anyInt(),
                argThat(b -> b != null && "nether_wastes".equals(b.getKey().getKey())));
        // Nether roof - at least bedrock layer
        verify(cd, atLeast(64)).setBlock(anyInt(), anyInt(), anyInt(), eq(Material.BEDROCK));
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.generators.ChunkGeneratorWorld#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}.
     */
    @SuppressWarnings("deprecation")
    @Test
    void testGenerateChunkDataWorldRandomIntIntBiomeGridNetherNoRoof() {
        settings.setNetherRoof(false);
        when(cgWorld.getEnvironment()).thenReturn(World.Environment.NETHER);
        ChunkData cd = cg.generateChunkData(cgWorld, random, 0, 0, biomeGrid);
        assertEquals(data, cd);
        // Verifications
        // Set biome in nether
        verify(biomeGrid, times(64)).setBiome(anyInt(), anyInt(), anyInt(),
                argThat(b -> b != null && "nether_wastes".equals(b.getKey().getKey())));
        // Nether roof - at least bedrock layer
        verify(cd, never()).setBlock(anyInt(), anyInt(), anyInt(), any(Material.class));
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
}
