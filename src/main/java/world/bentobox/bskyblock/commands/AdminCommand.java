package world.bentobox.bskyblock.commands;

import java.util.List;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.commands.admin.AdminDeleteCommand;
import world.bentobox.bentobox.api.commands.admin.AdminEmptyTrashCommand;
import world.bentobox.bentobox.api.commands.admin.AdminGetrankCommand;
import world.bentobox.bentobox.api.commands.admin.AdminInfoCommand;
import world.bentobox.bentobox.api.commands.admin.AdminRegisterCommand;
import world.bentobox.bentobox.api.commands.admin.AdminReloadCommand;
import world.bentobox.bentobox.api.commands.admin.AdminResetFlagsCommand;
import world.bentobox.bentobox.api.commands.admin.AdminSetrankCommand;
import world.bentobox.bentobox.api.commands.admin.AdminSetspawnCommand;
import world.bentobox.bentobox.api.commands.admin.AdminSwitchtoCommand;
import world.bentobox.bentobox.api.commands.admin.AdminTeleportCommand;
import world.bentobox.bentobox.api.commands.admin.AdminTrashCommand;
import world.bentobox.bentobox.api.commands.admin.AdminUnregisterCommand;
import world.bentobox.bentobox.api.commands.admin.AdminVersionCommand;
import world.bentobox.bentobox.api.commands.admin.AdminWhyCommand;
import world.bentobox.bentobox.api.commands.admin.deaths.AdminDeathsCommand;
import world.bentobox.bentobox.api.commands.admin.range.AdminRangeCommand;
import world.bentobox.bentobox.api.commands.admin.resets.AdminResetsResetCommand;
import world.bentobox.bentobox.api.commands.admin.schem.AdminSchemCommand;
import world.bentobox.bentobox.api.commands.admin.team.AdminTeamAddCommand;
import world.bentobox.bentobox.api.commands.admin.team.AdminTeamDisbandCommand;
import world.bentobox.bentobox.api.commands.admin.team.AdminTeamKickCommand;
import world.bentobox.bentobox.api.commands.admin.team.AdminTeamSetownerCommand;
import world.bentobox.bentobox.api.localization.TextVariables;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bskyblock.BSkyBlock;

public class AdminCommand extends CompositeCommand {

    public AdminCommand(BSkyBlock addon) {
        super(addon,
                addon.getSettings().getAdminCommand().split(" ")[0],
                addon.getSettings().getAdminCommand().split(" "));
    }

    @Override
    public void setup() {
        setPermission("admin.*");
        setOnlyPlayer(false);
        setParametersHelp("commands.admin.help.parameters");
        setDescription("commands.admin.help.description");
        new AdminVersionCommand(this);
        new AdminTeleportCommand(this, "tp");
        new AdminTeleportCommand(this, "tpnether");
        new AdminTeleportCommand(this, "tpend");
        new AdminGetrankCommand(this);
        new AdminSetrankCommand(this);
        new AdminInfoCommand(this);
        // Team commands
        new AdminTeamAddCommand(this);
        new AdminTeamKickCommand(this);
        new AdminTeamDisbandCommand(this);
        new AdminTeamSetownerCommand(this);
        // Schems
        new AdminSchemCommand(this);
        // Register/unregister islands
        new AdminRegisterCommand(this);
        new AdminUnregisterCommand(this);
        // Range
        new AdminRangeCommand(this);
        // Resets
        new AdminResetsResetCommand(this);
        // TODO new AdminClearresetsallCommand(this);
        // Delete
        new AdminDeleteCommand(this);
        // Why
        new AdminWhyCommand(this);
        // Deaths
        new AdminDeathsCommand(this);
        // Reload
        new AdminReloadCommand(this);
        // Spawn
        new AdminSetspawnCommand(this);
        // Reset flags
        new AdminResetFlagsCommand(this);
        // Trash
        new AdminTrashCommand(this);
        new AdminEmptyTrashCommand(this);
        new AdminSwitchtoCommand(this);
    }

    @Override
    public boolean execute(User user, String label, List<String> args) {
        if (!args.isEmpty()) {
            user.sendMessage("general.errors.unknown-command", TextVariables.LABEL, getTopLabel());
            return false;
        }
        // By default run the attached help command, if it exists (it should)
        return showHelp(this, user);
    }

}
