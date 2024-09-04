package dev.viaduct.factories.generators;

import dev.viaduct.factories.Factories;
import dev.viaduct.factories.Settings;
import dev.viaduct.factories.world.generators.ChunkGeneratorWorld;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
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

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author tastybento
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class})
public class ChunkGeneratorWorldTest {

    @Mock
    private Factories addon;
    private ChunkGeneratorWorld cg;
    @Mock
    private World world;
    private final Random random = new Random();
    @Mock
    private BiomeGrid biomeGrid;
    @Mock
    private Settings settings;
    @Mock
    private ChunkData data;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        // Bukkit
        PowerMockito.mockStatic(Bukkit.class);
        Server server = mock(Server.class);
        when(server.createChunkData(any())).thenReturn(data);
        when(Bukkit.getServer()).thenReturn(server);

        // Instance
        cg = new ChunkGeneratorWorld(addon);
        // World
        when(world.getEnvironment()).thenReturn(World.Environment.NORMAL);
        when(world.getMaxHeight()).thenReturn(16);
        // Settings
        when(addon.getSettings()).thenReturn(settings);
        when(settings.getSeaHeight()).thenReturn(0);
        when(settings.isNetherRoof()).thenReturn(true);
        when(settings.getDefaultBiome()).thenReturn(Biome.TAIGA);
        when(settings.getDefaultNetherBiome()).thenReturn(Biome.CRIMSON_FOREST);
        when(settings.getDefaultEndBiome()).thenReturn(Biome.END_MIDLANDS);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link ChunkGeneratorWorld#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}.
     */
    @Test
    public void testGenerateChunkDataWorldRandomIntIntBiomeGridOverworldVoid() {
        ChunkData cd = cg.generateChunkData(world, random, 0, 0, biomeGrid);
        assertEquals(data, cd);
        // Verifications
        // Default biome
        verify(settings).getDefaultBiome();
        verify(biomeGrid, times(64)).setBiome(anyInt(), anyInt(), anyInt(), any());
        // Sea height
        verify(settings).getSeaHeight();
        // Void
        verify(cd, never()).setRegion(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), any(Material.class));
    }

    /**
     * Test method for {@link ChunkGeneratorWorld#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}.
     */
    @Test
    public void testGenerateChunkDataWorldRandomIntIntBiomeGridOverworldSea() {
        // Set sea height
        when(settings.getSeaHeight()).thenReturn(10);
        ChunkData cd = cg.generateChunkData(world, random, 0, 0, biomeGrid);
        assertEquals(data, cd);
        // Verifications
        // Default biome
        verify(settings).getDefaultBiome();
        verify(biomeGrid, times(64)).setBiome(anyInt(), anyInt(), anyInt(), eq(Biome.TAIGA));
        // Sea height
        verify(settings, times(2)).getSeaHeight();
        // Water. Blocks = 16 x 16 x 11 because block 0
        verify(cd).setRegion(0, 0, 0, 16, 11, 16, Material.WATER);
    }

    /**
     * Test method for {@link ChunkGeneratorWorld#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}.
     */
    @Test
    public void testGenerateChunkDataWorldRandomIntIntBiomeGridEnd() {
        when(world.getEnvironment()).thenReturn(World.Environment.THE_END);
        ChunkData cd = cg.generateChunkData(world, random, 0, 0, biomeGrid);
        assertEquals(data, cd);
        // Verifications
        // Default biome
        verify(settings).getDefaultEndBiome();
        // Set biome in end
        verify(biomeGrid, times(64)).setBiome(anyInt(), anyInt(), anyInt(), eq(Biome.END_MIDLANDS));
        // Sea height
        verify(settings, never()).getSeaHeight();
        // Void
        verify(cd, never()).setRegion(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), any(Material.class));
    }

    /**
     * Test method for {@link ChunkGeneratorWorld#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}.
     */
    @Test
    public void testGenerateChunkDataWorldRandomIntIntBiomeGridNetherWithRoof() {
        when(world.getEnvironment()).thenReturn(World.Environment.NETHER);
        ChunkData cd = cg.generateChunkData(world, random, 0, 0, biomeGrid);
        assertEquals(data, cd);
        // Verifications
        // Nether roof check
        verify(settings).isNetherRoof();
        // Set biome in nether
        verify(biomeGrid, times(64)).setBiome(anyInt(), anyInt(), anyInt(), eq(Biome.CRIMSON_FOREST));
        // Nether roof - at least bedrock layer
        verify(cd, atLeast(64)).setBlock(anyInt(), anyInt(), anyInt(), eq(Material.BEDROCK));
    }

    /**
     * Test method for {@link ChunkGeneratorWorld#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}.
     */
    @Test
    public void testGenerateChunkDataWorldRandomIntIntBiomeGridNetherNoRoof() {
        when(settings.isNetherRoof()).thenReturn(false);
        when(world.getEnvironment()).thenReturn(World.Environment.NETHER);
        ChunkData cd = cg.generateChunkData(world, random, 0, 0, biomeGrid);
        assertEquals(data, cd);
        // Verifications
        verify(settings).getDefaultNetherBiome();
        // Nether roof check
        verify(settings).isNetherRoof();
        // Set biome in nether
        verify(biomeGrid, times(64)).setBiome(anyInt(), anyInt(), anyInt(), eq(Biome.CRIMSON_FOREST));
        // Nether roof - at least bedrock layer
        verify(cd, never()).setBlock(anyInt(), anyInt(), anyInt(), any(Material.class));
    }

    /**
     * Test method for {@link ChunkGeneratorWorld#canSpawn(org.bukkit.World, int, int)}.
     */
    @Test
    public void testCanSpawnWorldIntInt() {
        assertTrue(cg.canSpawn(mock(World.class), 0, 1));
    }

    /**
     * Test method for {@link ChunkGeneratorWorld#getDefaultPopulators(org.bukkit.World)}.
     */
    @Test
    public void testGetDefaultPopulatorsWorld() {
        assertTrue(cg.getDefaultPopulators(mock(World.class)).isEmpty());
    }

}
