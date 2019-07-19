/**
 *
 */
package world.bentobox.bskyblock.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.localization.TextVariables;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.blueprints.dataobjects.BlueprintBundle;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.managers.BlueprintsManager;
import world.bentobox.bentobox.managers.CommandsManager;
import world.bentobox.bentobox.managers.IslandWorldManager;
import world.bentobox.bentobox.managers.IslandsManager;
import world.bentobox.bskyblock.BSkyBlock;
import world.bentobox.bskyblock.Settings;

/**
 * @author tastybento
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class, BentoBox.class, User.class })
public class IslandCommandTest {

    private static final int NUMBER_OF_COMMANDS = 18;
    private User user;
    private IslandsManager im;
    private Island island;
    private BSkyBlock addon;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        // Set up plugin
        BentoBox plugin = mock(BentoBox.class);
        Whitebox.setInternalState(BentoBox.class, "instance", plugin);

        // Command manager
        CommandsManager cm = mock(CommandsManager.class);
        when(plugin.getCommandsManager()).thenReturn(cm);

        // Player
        Player p = mock(Player.class);
        // Sometimes use Mockito.withSettings().verboseLogging()
        user = mock(User.class);
        when(user.isOp()).thenReturn(false);
        UUID uuid = UUID.randomUUID();
        when(user.getUniqueId()).thenReturn(uuid);
        when(user.getPlayer()).thenReturn(p);
        when(user.getName()).thenReturn("tastybento");
        when(user.isPlayer()).thenReturn(true);
        User.setPlugin(plugin);

        // Island World Manager
        IslandWorldManager iwm = mock(IslandWorldManager.class);
        when(plugin.getIWM()).thenReturn(iwm);


        // Player has island to begin with
        im = mock(IslandsManager.class);
        island = mock(Island.class);
        when(im.getIsland(Mockito.any(), Mockito.any(UUID.class))).thenReturn(island);
        when(plugin.getIslands()).thenReturn(im);

        // Locales
        // Return the reference (USE THIS IN THE FUTURE)
        when(user.getTranslation(Mockito.anyString())).thenAnswer((Answer<String>) invocation -> invocation.getArgumentAt(0, String.class));

        addon = mock(BSkyBlock.class);
        Settings settings = mock(Settings.class);
        when(settings.getIslandCommand()).thenReturn("island");
        when(addon.getSettings()).thenReturn(settings);
        when(plugin.getSettings()).thenReturn(mock(world.bentobox.bentobox.Settings.class));

        // Blueprints
        BlueprintsManager bpm = mock(BlueprintsManager.class);
        Map<String, BlueprintBundle> map = new HashMap<>();
        BlueprintBundle bun = mock(BlueprintBundle.class);
        when(bun.getDisplayName()).thenReturn("aaa", "bbb");
        map.put("aaa", bun);
        map.put("bbb", bun);
        when(bun.getUniqueId()).thenReturn("unique1", "unique2");
        when(bun.isRequirePermission()).thenReturn(true);
        when(bpm.getBlueprintBundles(Mockito.any())).thenReturn(map);
        when(plugin.getBlueprintsManager()).thenReturn(bpm);
        PowerMockito.mockStatic(Bukkit.class);
        PluginManager pim = mock(PluginManager.class);
        when(Bukkit.getPluginManager()).thenReturn(pim);
        BukkitScheduler sch = mock(BukkitScheduler.class);
        when(Bukkit.getScheduler()).thenReturn(sch);
    }


    /**
     * Test method for {@link world.bentobox.bskyblock.commands.IslandCommand#IslandCommand(world.bentobox.bentobox.api.addons.Addon, java.lang.String)}.
     */
    @Test
    public void testIslandCommand() {
        IslandCommand cmd = new IslandCommand(addon);
        assertEquals("island", cmd.getLabel());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.commands.IslandCommand#setup()}.
     */
    @Test
    public void testSetup() {
        when(addon.getPermissionPrefix()).thenReturn("bskyblock.");
        IslandCommand cmd = new IslandCommand(addon);
        assertEquals("bskyblock.island", cmd.getPermission());
        assertTrue(cmd.isOnlyPlayer());
        assertEquals("commands.island.parameters", cmd.getParameters());
        assertEquals("commands.island.help.description", cmd.getDescription());
        // Number of commands = sub commands + help
        assertEquals("Number of sub commands registered", NUMBER_OF_COMMANDS, cmd.getSubCommands().values().size());

    }

    /**
     * Test method for {@link world.bentobox.bskyblock.commands.IslandCommand#execute(world.bentobox.bentobox.api.user.User, java.lang.String, java.util.List)}.
     */
    @Test
    public void testExecuteUserStringListOfStringNullUsers() {
        IslandCommand cmd = new IslandCommand(addon);
        assertFalse(cmd.execute(null, "island", Collections.emptyList()));

    }

    /**
     * Test method for {@link world.bentobox.bskyblock.commands.IslandCommand#execute(world.bentobox.bentobox.api.user.User, java.lang.String, java.util.List)}.
     */
    @Test
    public void testExecuteUserStringListOfStringUnknownCommand() {
        IslandCommand cmd = new IslandCommand(addon);
        assertFalse(cmd.execute(user, "island", Collections.singletonList("unknown")));
        verify(user).sendMessage("general.errors.unknown-command", TextVariables.LABEL, "island");
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.commands.IslandCommand#execute(world.bentobox.bentobox.api.user.User, java.lang.String, java.util.List)}.
     */
    @Test
    public void testExecuteUserStringListOfStringNoArgsNoPermission() {
        IslandCommand cmd = new IslandCommand(addon);
        assertFalse(cmd.execute(user, "island", Collections.emptyList()));
        verify(user).sendMessage("general.errors.no-permission", "[permission]", "island.home");
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.commands.IslandCommand#execute(world.bentobox.bentobox.api.user.User, java.lang.String, java.util.List)}.
     */
    @Test
    public void testExecuteUserStringListOfStringNoArgsSuccess() {
        when(user.hasPermission(anyString())).thenReturn(true);
        IslandCommand cmd = new IslandCommand(addon);
        assertTrue(cmd.execute(user, "island", Collections.emptyList()));
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.commands.IslandCommand#execute(world.bentobox.bentobox.api.user.User, java.lang.String, java.util.List)}.
     */
    @Test
    public void testExecuteUserStringListOfStringNoArgsConsole() {
        when(user.isPlayer()).thenReturn(false);
        IslandCommand cmd = new IslandCommand(addon);
        assertFalse(cmd.execute(user, "island", Collections.emptyList()));
        verify(user).sendMessage("general.errors.use-in-game");
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.commands.IslandCommand#execute(world.bentobox.bentobox.api.user.User, java.lang.String, java.util.List)}.
     */
    @Test
    public void testExecuteUserStringListOfStringNoArgsNoIslandConsole() {
        when(user.isPlayer()).thenReturn(false);
        when(im.getIsland(any(), any(UUID.class))).thenReturn(null);
        IslandCommand cmd = new IslandCommand(addon);
        assertFalse(cmd.execute(user, "island", Collections.emptyList()));
        verify(user).sendMessage("general.errors.use-in-game");
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.commands.IslandCommand#execute(world.bentobox.bentobox.api.user.User, java.lang.String, java.util.List)}.
     */
    @Test
    public void testExecuteUserStringListOfStringNoArgsNoIslandNoPermission() {
        when(im.getIsland(any(), any(UUID.class))).thenReturn(null);
        IslandCommand cmd = new IslandCommand(addon);
        assertFalse(cmd.execute(user, "island", Collections.emptyList()));
        verify(user).sendMessage("general.errors.no-permission", "[permission]", "island.create");
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.commands.IslandCommand#execute(world.bentobox.bentobox.api.user.User, java.lang.String, java.util.List)}.
     */
    @Test
    public void testExecuteUserStringListOfStringNoArgsNoIslandCreateSuccess() {
        when(im.getIsland(any(), any(UUID.class))).thenReturn(null);
        when(user.hasPermission(Mockito.eq("island.create"))).thenReturn(true);
        IslandCommand cmd = new IslandCommand(addon);
        assertTrue(cmd.execute(user, "island", Collections.emptyList()));
        verify(user).getTranslation("commands.island.create.pick");
    }

}
