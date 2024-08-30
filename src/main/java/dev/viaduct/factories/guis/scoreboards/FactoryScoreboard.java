package dev.viaduct.factories.guis.scoreboards;

import dev.viaduct.factories.banks.Bank;
import dev.viaduct.factories.players.FactoryPlayer;
import dev.viaduct.factories.resources.Resource;
import dev.viaduct.factories.resources.ResourceManager;
import dev.viaduct.factories.resources.impl.Wood;
import dev.viaduct.factories.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

import java.util.Map;
import java.util.Set;

public class FactoryScoreboard {

    private final Scoreboard scoreboard;
    private final Bank bank;

    public FactoryScoreboard(FactoryPlayer factoryPlayer) {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.bank = factoryPlayer.getBank();

        addScoreboardLine();
        factoryPlayer.getPlayer().setScoreboard(scoreboard);
    }

    public void addScoreboardLine() {
        Objective objective = scoreboard.registerNewObjective("Resources", Criteria.DUMMY, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(Chat.colorizeHex("#FFD700&lFactories"));

        // up to 15 lines!

        Score divider = objective.getScore("               ");
        divider.setScore(15); // index 15

        Score resourceTitle = objective.getScore(Chat.colorizeHex("&f&lResources    "));
        resourceTitle.setScore(14); // index 14

        // get all resources
        Set<Resource> resources = bank.getResourceMap().keySet();

        int index = 13;

        // iterate over resources
        for (Resource resource : resources) {
            Score score = objective.getScore(Chat.colorize("  &f• ") + resource.getFormattedName() + bank.getResourceAmt(resource));
            score.setScore(index);
            index--;
        }
    }

}