package world.bentobox.bskyblock.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.addons.AddonDescription;
import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.user.User;

/**
 * @author tastybento
 *
 */
class IslandAboutCommandTest {

    /**
     * Test method for {@link world.bentobox.bskyblock.commands.IslandAboutCommand#execute(world.bentobox.bentobox.api.user.User, java.lang.String, java.util.List)}.
     */
    @Test
    void testExecuteUserStringListOfString() {
        CompositeCommand ic= mock(CompositeCommand.class);
        Addon addon = mock(Addon.class);
        AddonDescription desc = new AddonDescription.Builder("","BSkyBlock","1.2.3").build();
        when(addon.getDescription()).thenReturn(desc);
        when(ic.getAddon()).thenReturn(addon);
        IslandAboutCommand c = new IslandAboutCommand(ic);
        User user = mock(User.class);
        boolean result = c.execute(user, "", Collections.emptyList());
        assertTrue(result);
        // Verify all four lines
        Mockito.verify(user).sendRawMessage("About BSkyBlock 1.2.3:");
        Mockito.verify(user).sendRawMessage("Copyright (c) 2017 - 2026 tastybento, others");
        Mockito.verify(user).sendRawMessage("See https://www.eclipse.org/legal/epl-2.0/");
        Mockito.verify(user).sendRawMessage("for license information.");
    }

}
