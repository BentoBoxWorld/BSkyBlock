package world.bentobox.bskyblock.generators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import world.bentobox.bskyblock.BSkyBlock;
import world.bentobox.bskyblock.Settings;

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
        when(server.createChunkData(Mockito.any())).thenReturn(data);
        when(Bukkit.getServer()).thenReturn(server);

        // Instance
        cg = new ChunkGeneratorWorld(addon);
        // World
        when(world.getEnvironment()).thenReturn(World.Environment.NORMAL);
        // Settings
        when(addon.getSettings()).thenReturn(settings);
        when(settings.getSeaHeight()).thenReturn(0);
        when(settings.isNetherRoof()).thenReturn(true);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.generators.ChunkGeneratorWorld#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}.
     */
    @Test
    public void testGenerateChunkDataWorldRandomIntIntBiomeGridOverworldVoid() {
        ChunkData cd = cg.generateChunkData(world, random, 0 , 0 , biomeGrid);
        assertEquals(data, cd);
        // Verifications
        // Default biome
        Mockito.verify(settings).getDefaultBiome();
        Mockito.verify(biomeGrid, Mockito.times(16 * 16)).setBiome(Mockito.anyInt(), Mockito.anyInt(), Mockito.any());
        // Sea height
        Mockito.verify(settings).getSeaHeight();
        // Void
        Mockito.verify(cd, Mockito.never()).setBlock(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any(Material.class));
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.generators.ChunkGeneratorWorld#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}.
     */
    @Test
    public void testGenerateChunkDataWorldRandomIntIntBiomeGridOverworldSea() {
        // Set sea height
        when(settings.getSeaHeight()).thenReturn(10);
        ChunkData cd = cg.generateChunkData(world, random, 0 , 0 , biomeGrid);
        assertEquals(data, cd);
        // Verifications
        // Default biome
        Mockito.verify(settings).getDefaultBiome();
        Mockito.verify(biomeGrid, Mockito.times(16 * 16)).setBiome(Mockito.anyInt(), Mockito.anyInt(), Mockito.any());
        // Sea height
        Mockito.verify(settings).getSeaHeight();
        // Water. Blocks = 16 x 16 x 11 because block 0
        Mockito.verify(cd, Mockito.times(2816)).setBlock(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(Material.WATER));
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.generators.ChunkGeneratorWorld#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}.
     */
    @Test
    public void testGenerateChunkDataWorldRandomIntIntBiomeGridEnd() {
        when(world.getEnvironment()).thenReturn(World.Environment.THE_END);
        ChunkData cd = cg.generateChunkData(world, random, 0 , 0 , biomeGrid);
        assertEquals(data, cd);
        // Verifications
        // Default biome
        Mockito.verify(settings, Mockito.never()).getDefaultBiome();
        // Never set biome in end
        Mockito.verify(biomeGrid, Mockito.never()).setBiome(Mockito.anyInt(), Mockito.anyInt(), Mockito.any());
        // Sea height
        Mockito.verify(settings, Mockito.never()).getSeaHeight();
        // Void
        Mockito.verify(cd, Mockito.never()).setBlock(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any(Material.class));
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.generators.ChunkGeneratorWorld#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}.
     */
    @Test
    public void testGenerateChunkDataWorldRandomIntIntBiomeGridNetherWithRoof() {
        when(world.getEnvironment()).thenReturn(World.Environment.NETHER);
        ChunkData cd = cg.generateChunkData(world, random, 0 , 0 , biomeGrid);
        assertEquals(data, cd);
        // Verifications
        // Nether roof check
        Mockito.verify(settings).isNetherRoof();
        // Never set biome in nether
        Mockito.verify(biomeGrid, Mockito.never()).setBiome(Mockito.anyInt(), Mockito.anyInt(), Mockito.any());
        // Nether roof - at least bedrock layer
        Mockito.verify(cd, Mockito.atLeast(16 * 16)).setBlock(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(Material.BEDROCK));
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.generators.ChunkGeneratorWorld#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}.
     */
    @Test
    public void testGenerateChunkDataWorldRandomIntIntBiomeGridNetherNoRoof() {
        when(settings.isNetherRoof()).thenReturn(false);
        when(world.getEnvironment()).thenReturn(World.Environment.NETHER);
        ChunkData cd = cg.generateChunkData(world, random, 0 , 0 , biomeGrid);
        assertEquals(data, cd);
        // Verifications
        // Nether roof check
        Mockito.verify(settings).isNetherRoof();
        // Never set biome in nether
        Mockito.verify(biomeGrid, Mockito.never()).setBiome(Mockito.anyInt(), Mockito.anyInt(), Mockito.any());
        // Nether roof - at least bedrock layer
        Mockito.verify(cd, Mockito.never()).setBlock(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any(Material.class));
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
