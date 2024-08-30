package dev.viaduct.factories.guis.scoreboards;

import dev.viaduct.factories.banks.Bank;
import dev.viaduct.factories.players.FactoryPlayer;
import dev.viaduct.factories.resources.ResourceManager;
import dev.viaduct.factories.resources.impl.Wood;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

public class FactoryScoreboard {
    private final Scoreboard scoreboard;
    private final FactoryPlayer factoryPlayer;
    private final Bank bank;

    public FactoryScoreboard(FactoryPlayer factoryPlayer) {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.factoryPlayer = factoryPlayer;
        this.bank = factoryPlayer.getBank();
        addScoreboardLine();
        factoryPlayer.getPlayer().setScoreboard(scoreboard);
    }

    public void addScoreboardLine() {
        Objective objective = scoreboard.registerNewObjective("Resources", Criteria.DUMMY, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("Factories");
        Score score10 = objective.getScore("Wood: " + bank.getResourceAmt("Wood"));
        score10.setScore(10);
    }
}
