package world.bentobox.bskyblock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
import java.util.concurrent.CompletableFuture;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import world.bentobox.bentobox.api.addons.AddonDescription;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.AbstractDatabaseHandler;
import world.bentobox.bentobox.database.DatabaseSetup;
import world.bentobox.bentobox.managers.AddonsManager;
import world.bentobox.bentobox.managers.CommandsManager;
import world.bentobox.bskyblock.generators.ChunkGeneratorWorld;

/**
 * @author tastybento
 *
 */
class BSkyBlockTest extends CommonTestSetup {

    @Mock
    private User user;

    private BSkyBlock addon;

    private MockedStatic<DatabaseSetup> mockDb;

    @SuppressWarnings("unchecked")
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        // Database
        AbstractDatabaseHandler<Object> h = mock(AbstractDatabaseHandler.class);
        mockDb = Mockito.mockStatic(DatabaseSetup.class);
        DatabaseSetup dbSetup = mock(DatabaseSetup.class);
        mockDb.when(DatabaseSetup::getDatabase).thenReturn(dbSetup);
        when(dbSetup.getHandler(any())).thenReturn(h);
        when(h.saveObject(any())).thenReturn(CompletableFuture.completedFuture(true));

        // Command manager
        CommandsManager cm = mock(CommandsManager.class);
        when(plugin.getCommandsManager()).thenReturn(cm);

        // User
        when(user.isOp()).thenReturn(false);
        UUID userUuid = UUID.randomUUID();
        when(user.getUniqueId()).thenReturn(userUuid);
        when(user.getPlayer()).thenReturn(mockPlayer);
        when(user.getName()).thenReturn("tastybento");
        User.setPlugin(plugin);

        // Player has island to begin with
        when(im.getIsland(any(), any(UUID.class))).thenReturn(island);

        // Locales
        when(user.getTranslation(Mockito.anyString()))
                .thenAnswer((Answer<String>) invocation -> invocation.getArgument(0, String.class));

        // Addon
        addon = new BSkyBlock();
        File jFile = new File("addon.jar");
        List<String> lines = Arrays.asList("# BSkyBlock Configuration", "uniqueId: config");
        Path path = Paths.get("config.yml");
        Files.write(path, lines, Charset.forName("UTF-8"));
        try (JarOutputStream tempJarOutputStream = new JarOutputStream(new FileOutputStream(jFile))) {
            try (FileInputStream fis = new FileInputStream(path.toFile())) {
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                JarEntry entry = new JarEntry(path.toString());
                tempJarOutputStream.putNextEntry(entry);
                while ((bytesRead = fis.read(buffer)) != -1) {
                    tempJarOutputStream.write(buffer, 0, bytesRead);
                }
            }
        }
        File dataFolder = new File("addons/BSkyBlock");
        addon.setDataFolder(dataFolder);
        addon.setFile(jFile);
        AddonDescription desc = new AddonDescription.Builder("bentobox", "bskyblock", "1.3").description("test")
                .authors("tasty").build();
        addon.setDescription(desc);

        // Addons manager
        AddonsManager am = mock(AddonsManager.class);
        when(plugin.getAddonsManager()).thenReturn(am);

        // Flags manager
        when(plugin.getFlagsManager()).thenReturn(fm);
        when(fm.getFlags()).thenReturn(Collections.emptyList());
    }

    @Override
    @AfterEach
    public void tearDown() throws Exception {
        if (mockDb != null) {
            mockDb.closeOnDemand();
        }
        super.tearDown();
        new File("addon.jar").delete();
        new File("config.yml").delete();
        deleteAll(new File("addons"));
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.BSkyBlock#onLoad()}.
     */
    @Test
    void testOnLoad() {
        addon.onLoad();
        // Check that config.yml file has been saved
        File check = new File("addons/BSkyBlock", "config.yml");
        assertTrue(check.exists());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.BSkyBlock#onEnable()}.
     */
    @Test
    void testOnEnable() {
        testOnLoad();
        addon.onEnable();
        assertTrue(addon.getPlayerCommand().isPresent());
        assertTrue(addon.getAdminCommand().isPresent());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.BSkyBlock#onReload()}.
     */
    @Test
    void testOnReload() {
        addon.onReload();
        // Check that config.yml file has been saved
        File check = new File("addons/BSkyBlock", "config.yml");
        assertTrue(check.exists());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.BSkyBlock#createWorlds()}.
     */
    @Test
    void testCreateWorlds() {
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
    void testGetSettings() {
        addon.onLoad();
        assertNotNull(addon.getSettings());
    }

    /**
     * Test method for
     * {@link world.bentobox.bskyblock.BSkyBlock#getWorldSettings()}.
     */
    @Test
    void testGetWorldSettings() {
        addon.onLoad();
        assertEquals(addon.getSettings(), addon.getWorldSettings());
    }

    /**
     * Test method for
     * {@link world.bentobox.bskyblock.BSkyBlock#getDefaultWorldGenerator(java.lang.String, java.lang.String)}.
     */
    @Test
    void testGetDefaultWorldGeneratorStringString() {
        assertNull(addon.getDefaultWorldGenerator("", ""));
        addon.onLoad();
        addon.createWorlds();
        assertNotNull(addon.getDefaultWorldGenerator("", ""));
        assertTrue(addon.getDefaultWorldGenerator("", "") instanceof ChunkGeneratorWorld);
    }
}
