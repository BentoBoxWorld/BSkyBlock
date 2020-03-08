package world.bentobox.bskyblock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.Settings;
import world.bentobox.bentobox.api.addons.AddonDescription;
import world.bentobox.bentobox.api.configuration.Config;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.managers.AddonsManager;
import world.bentobox.bentobox.managers.CommandsManager;
import world.bentobox.bentobox.managers.FlagsManager;
import world.bentobox.bentobox.managers.IslandWorldManager;
import world.bentobox.bentobox.managers.IslandsManager;
import world.bentobox.bskyblock.generators.ChunkGeneratorWorld;

/**
 * @author tastybento
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class, BentoBox.class, User.class, Config.class })
public class BSkyBlockTest {

    @Mock
    private User user;
    @Mock
    private IslandsManager im;
    @Mock
    private Island island;

    private BSkyBlock addon;
    @Mock
    private BentoBox plugin;
    @Mock
    private FlagsManager fm;
    @Mock
    private Settings settings;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        // Set up plugin
        Whitebox.setInternalState(BentoBox.class, "instance", plugin);
        when(plugin.getLogger()).thenReturn(Logger.getAnonymousLogger());
        // Command manager
        CommandsManager cm = mock(CommandsManager.class);
        when(plugin.getCommandsManager()).thenReturn(cm);

        // Player
        Player p = mock(Player.class);
        // Sometimes use Mockito.withSettings().verboseLogging()
        when(user.isOp()).thenReturn(false);
        UUID uuid = UUID.randomUUID();
        when(user.getUniqueId()).thenReturn(uuid);
        when(user.getPlayer()).thenReturn(p);
        when(user.getName()).thenReturn("tastybento");
        User.setPlugin(plugin);

        // Island World Manager
        IslandWorldManager iwm = mock(IslandWorldManager.class);
        when(plugin.getIWM()).thenReturn(iwm);


        // Player has island to begin with
        island = mock(Island.class);
        when(im.getIsland(Mockito.any(), Mockito.any(UUID.class))).thenReturn(island);
        when(plugin.getIslands()).thenReturn(im);

        // Locales
        // Return the reference (USE THIS IN THE FUTURE)
        when(user.getTranslation(Mockito.anyString())).thenAnswer((Answer<String>) invocation -> invocation.getArgument(0, String.class));

        // Server
        PowerMockito.mockStatic(Bukkit.class);
        Server server = mock(Server.class);
        when(Bukkit.getServer()).thenReturn(server);
        when(Bukkit.getLogger()).thenReturn(Logger.getAnonymousLogger());
        when(Bukkit.getPluginManager()).thenReturn(mock(PluginManager.class));

        // Addon
        addon = new BSkyBlock();
        File jFile = new File("addon.jar");
        List<String> lines = Arrays.asList("# BSkyBlock Configuration", "uniqueId: config");
        Path path = Paths.get("config.yml");
        Files.write(path, lines, Charset.forName("UTF-8"));
        try (JarOutputStream tempJarOutputStream = new JarOutputStream(new FileOutputStream(jFile))) {
            //Added the new files to the jar.
            try (FileInputStream fis = new FileInputStream(path.toFile())) {

                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                JarEntry entry = new JarEntry(path.toString());
                tempJarOutputStream.putNextEntry(entry);
                while((bytesRead = fis.read(buffer)) != -1) {
                    tempJarOutputStream.write(buffer, 0, bytesRead);
                }
            }
        }
        File dataFolder = new File("addons/BSkyBlock");
        addon.setDataFolder(dataFolder);
        addon.setFile(jFile);
        AddonDescription desc = new AddonDescription.Builder("bentobox", "bskyblock", "1.3").description("test").authors("tasty").build();
        addon.setDescription(desc);
        // Addons manager
        AddonsManager am = mock(AddonsManager.class);
        when(plugin.getAddonsManager()).thenReturn(am);

        // Flags manager
        when(plugin.getFlagsManager()).thenReturn(fm);
        when(fm.getFlags()).thenReturn(Collections.emptyList());

        // Settings
        when(plugin.getSettings()).thenReturn(settings);

    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        new File("addon.jar").delete();
        new File("config.yml").delete();
        new File("addons/BSkyBlock","config.yml").delete();
        new File("addons/BSkyBlock").delete();
        new File("addons").delete();
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.BSkyBlock#onLoad()}.
     */
    @Test
    public void testOnLoad() {
        addon.onLoad();
        // Check that config.yml file has been saved
        File check = new File("addons/BSkyBlock","config.yml");
        assertTrue(check.exists());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.BSkyBlock#onEnable()}.
     */
    @Test
    public void testOnEnable() {
        testOnLoad();
        addon.onEnable();
        assertTrue(addon.getPlayerCommand().isPresent());
        assertTrue(addon.getAdminCommand().isPresent());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.BSkyBlock#onReload()}.
     */
    @Test
    public void testOnReload() {
        addon.onReload();
        // Check that config.yml file has been saved
        File check = new File("addons/BSkyBlock","config.yml");
        assertTrue(check.exists());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.BSkyBlock#createWorlds()}.
     */
    @Test
    public void testCreateWorlds() {
        addon.onLoad();
        addon.createWorlds();
        Mockito.verify(plugin).log("[bskyblock] Creating BSkyBlock world ...");
        Mockito.verify(plugin).log("[bskyblock] Creating BSkyBlock's Nether...");
        Mockito.verify(plugin).log("[bskyblock] Creating BSkyBlock's End World...");
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.BSkyBlock#getSettings()}.
     */
    @Test
    public void testGetSettings() {
        addon.onLoad();
        assertNotNull(addon.getSettings());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.BSkyBlock#getWorldSettings()}.
     */
    @Test
    public void testGetWorldSettings() {
        addon.onLoad();
        assertEquals(addon.getSettings(), addon.getWorldSettings());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.BSkyBlock#getDefaultWorldGenerator(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetDefaultWorldGeneratorStringString() {
        assertNull(addon.getDefaultWorldGenerator("", ""));
        addon.onLoad();
        addon.createWorlds();
        assertNotNull(addon.getDefaultWorldGenerator("", ""));
        assertTrue(addon.getDefaultWorldGenerator("", "") instanceof ChunkGeneratorWorld);
    }

}
