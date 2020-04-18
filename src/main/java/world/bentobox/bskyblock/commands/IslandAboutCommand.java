package world.bentobox.bskyblock.commands;

import java.util.List;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.user.User;

public class IslandAboutCommand extends CompositeCommand {

    /**
     * About
     * @param islandCommand - parent command
     */
    public IslandAboutCommand(CompositeCommand islandCommand) {
        super(islandCommand, "about", "ab");
    }

    @Override
    public void setup() {
        setDescription("commands.island.about.description");
    }

    @Override
    public boolean execute(User user, String label, List<String> args) {
        user.sendRawMessage("About " + getAddon().getDescription().getName() + " " + getAddon().getDescription().getVersion() + ":");
        user.sendRawMessage("Copyright (c) 2017 - 2020 tastybento, Poslovitch");
        user.sendRawMessage("See https://www.eclipse.org/legal/epl-2.0/");
        user.sendRawMessage("for license information.");
        return true;
    }

}
