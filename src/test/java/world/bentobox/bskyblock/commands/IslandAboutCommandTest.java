package world.bentobox.bskyblock.commands;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Test;
import org.mockito.Mockito;

import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.addons.AddonDescription;
import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.user.User;

/**
 * @author tastybento
 *
 */
public class IslandAboutCommandTest {

    /**
     * Test method for {@link world.bentobox.bskyblock.commands.IslandAboutCommand#execute(world.bentobox.bentobox.api.user.User, java.lang.String, java.util.List)}.
     */
    @Test
    public void testExecuteUserStringListOfString() {
        CompositeCommand ic= mock(CompositeCommand.class);
        Addon addon = mock(Addon.class);
        AddonDescription desc = new AddonDescription.Builder("","BSkyBlock","1.2.3").build();
        when(addon.getDescription()).thenReturn(desc);
        when(ic.getAddon()).thenReturn(addon);
        IslandAboutCommand c = new IslandAboutCommand(ic);
        User user = mock(User.class);
        c.execute(user, "", Collections.emptyList());
        // Verify
        Mockito.verify(user).sendRawMessage(Mockito.eq("Copyright (c) 2017 - 2020 tastybento, Poslovitch"));
        Mockito.verify(user).sendRawMessage(Mockito.eq("About BSkyBlock 1.2.3:"));
    }

}
