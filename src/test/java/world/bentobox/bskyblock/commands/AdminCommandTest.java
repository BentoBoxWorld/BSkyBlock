/**
 *
 */
package world.bentobox.bskyblock.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.localization.TextVariables;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.managers.CommandsManager;
import world.bentobox.bentobox.managers.FlagsManager;
import world.bentobox.bskyblock.BSkyBlock;
import world.bentobox.bskyblock.Settings;

/**
 * @author tastybento
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class, BentoBox.class, User.class })
public class AdminCommandTest {

    private static final int NUM_COMMANDS = 25;
    @Mock
    private User user;
    @Mock
    private BSkyBlock addon;
    @Mock
    private FlagsManager fm;

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
        when(user.isOp()).thenReturn(false);
        UUID uuid = UUID.randomUUID();
        when(user.getUniqueId()).thenReturn(uuid);
        when(user.getPlayer()).thenReturn(p);
        when(user.getName()).thenReturn("tastybento");
        User.setPlugin(plugin);

        // Locales
        // Return the reference (USE THIS IN THE FUTURE)
        when(user.getTranslation(Mockito.anyString())).thenAnswer((Answer<String>) invocation -> invocation.getArgument(0, String.class));

        Settings settings = mock(Settings.class);
        when(settings.getAdminCommand()).thenReturn("bsbadmin");
        when(addon.getSettings()).thenReturn(settings);
        
        // Flags manager
        when(plugin.getFlagsManager()).thenReturn(fm);
        when(fm.getFlags()).thenReturn(Collections.emptyList());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.commands.AdminCommand#AdminCommand(world.bentobox.bentobox.api.addons.Addon, java.lang.String)}.
     */
    @Test
    public void testAdminCommand() {
        AdminCommand cmd = new AdminCommand(addon);
        assertEquals("bsbadmin", cmd.getLabel());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.commands.AdminCommand#setup()}.
     */
    @Test
    public void testSetup() {
        when(addon.getPermissionPrefix()).thenReturn("bskyblock.");
        AdminCommand cmd = new AdminCommand(addon);
        assertEquals("bskyblock.admin.*", cmd.getPermission());
        assertFalse(cmd.isOnlyPlayer());
        assertEquals("commands.admin.help.parameters", cmd.getParameters());
        assertEquals("commands.admin.help.description", cmd.getDescription());
        // Number of commands = sub commands + help
        assertEquals("Number of sub commands registered", NUM_COMMANDS, cmd.getSubCommands().values().size());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.commands.AdminCommand#execute(world.bentobox.bentobox.api.user.User, java.lang.String, java.util.List)}.
     */
    @Test
    public void testExecuteUserStringListOfStringUnknownCommand() {
        AdminCommand cmd = new AdminCommand(addon);
        assertFalse(cmd.execute(user, "bsbadmin", Collections.singletonList("unknown")));
        Mockito.verify(user).sendMessage("general.errors.unknown-command", TextVariables.LABEL, "bsbadmin");
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.commands.AdminCommand#execute(world.bentobox.bentobox.api.user.User, java.lang.String, java.util.List)}.
     */
    @Test
    public void testExecuteUserStringListOfStringNoCommand() {
        AdminCommand cmd = new AdminCommand(addon);
        assertTrue(cmd.execute(user, "bsbadmin", Collections.emptyList()));
        // Show help
        Mockito.verify(user).sendMessage("commands.help.header", TextVariables.LABEL, "commands.help.console");
    }

}
