package world.bentobox.bskyblock.generators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import org.bukkit.UnsafeValues;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import world.bentobox.bskyblock.BSkyBlock;
import world.bentobox.bskyblock.Settings;
import world.bentobox.bskyblock.mocks.ServerMocks;

/**
 * @author tastybento
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class})
public class ChunkGeneratorWorldTest {

    @Mock
    private BSkyBlock addon;
    private ChunkGeneratorWorld cg;
    @Mock
    private World world;
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
    @Before
    public void setUp() throws Exception {
        ServerMocks.newServer();
        // Bukkit

        PowerMockito.mockStatic(Bukkit.class);
        Server server = mock(Server.class);
        when(server.createChunkData(any())).thenReturn(data);
        when(Bukkit.getServer()).thenReturn(server);
        UnsafeValues unsafe = mock(UnsafeValues.class);
        when(Bukkit.getUnsafe()).thenReturn(unsafe);

        // Instance
        cg = new ChunkGeneratorWorld(addon);
        // World
        when(world.getEnvironment()).thenReturn(World.Environment.NORMAL);
        when(world.getMaxHeight()).thenReturn(16);
        // Settings
        settings = new Settings();
        when(addon.getSettings()).thenReturn(settings);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() {
        ServerMocks.unsetBukkitServer();
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.generators.ChunkGeneratorWorld#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}.
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testGenerateChunkDataWorldRandomIntIntBiomeGridOverworldVoid() {
        ChunkData cd = cg.generateChunkData(world, random, 0 , 0 , biomeGrid);
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
    public void testGenerateChunkDataWorldRandomIntIntBiomeGridOverworldSea() {
        // Set sea height
        settings.setSeaHeight(10);
        ChunkData cd = cg.generateChunkData(world, random, 0, 0, biomeGrid);
        assertEquals(data, cd);
        // Verifications
        verify(biomeGrid, times(64)).setBiome(anyInt(), anyInt(), anyInt(), eq(Biome.TAIGA));
        // Water. Blocks = 16 x 16 x 11 because block 0
        verify(cd).setRegion(0, 0, 0, 16, 11, 16, Material.WATER);
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.generators.ChunkGeneratorWorld#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}.
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testGenerateChunkDataWorldRandomIntIntBiomeGridEnd() {
        when(world.getEnvironment()).thenReturn(World.Environment.THE_END);
        ChunkData cd = cg.generateChunkData(world, random, 0 , 0 , biomeGrid);
        assertEquals(data, cd);
        // Verifications
        // Set biome in end
        verify(biomeGrid, times(64)).setBiome(anyInt(), anyInt(), anyInt(), eq(Biome.END_MIDLANDS));
        // Void
        verify(cd, never()).setRegion(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), any(Material.class));
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.generators.ChunkGeneratorWorld#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}.
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testGenerateChunkDataWorldRandomIntIntBiomeGridNetherWithRoof() {
        when(world.getEnvironment()).thenReturn(World.Environment.NETHER);
        ChunkData cd = cg.generateChunkData(world, random, 0 , 0 , biomeGrid);
        assertEquals(data, cd);
        // Verifications
        // Set biome in nether
        verify(biomeGrid, times(64)).setBiome(anyInt(), anyInt(), anyInt(), eq(Biome.CRIMSON_FOREST));
        // Nether roof - at least bedrock layer
        verify(cd, atLeast(64)).setBlock(anyInt(), anyInt(), anyInt(), eq(Material.BEDROCK));
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.generators.ChunkGeneratorWorld#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}.
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testGenerateChunkDataWorldRandomIntIntBiomeGridNetherNoRoof() {
        settings.setNetherRoof(false);
        when(world.getEnvironment()).thenReturn(World.Environment.NETHER);
        ChunkData cd = cg.generateChunkData(world, random, 0 , 0 , biomeGrid);
        assertEquals(data, cd);
        // Verifications
        // Set biome in nether
        verify(biomeGrid, times(64)).setBiome(anyInt(), anyInt(), anyInt(), eq(Biome.CRIMSON_FOREST));
        // Nether roof - at least bedrock layer
        verify(cd, never()).setBlock(anyInt(), anyInt(), anyInt(), any(Material.class));
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.generators.ChunkGeneratorWorld#canSpawn(org.bukkit.World, int, int)}.
     */
    @Test
    public void testCanSpawnWorldIntInt() {
        assertTrue(cg.canSpawn(mock(World.class), 0, 1));
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.generators.ChunkGeneratorWorld#getDefaultPopulators(org.bukkit.World)}.
     */
    @Test
    public void testGetDefaultPopulatorsWorld() {
        assertTrue(cg.getDefaultPopulators(mock(World.class)).isEmpty());
    }

}
