package dev.viaduct.factories.actions.impl;

import dev.viaduct.factories.actions.Action;
import dev.viaduct.factories.domain.players.FactoryPlayer;
import dev.viaduct.factories.upgrades.UpgradeManager;

public class UpgradeLevelAction implements Action {

    private final UpgradeManager.UpgradeName upgradeName;
    private final int newLevel;

    public UpgradeLevelAction(UpgradeManager.UpgradeName upgradeName, int newLevel) {
        this.upgradeName = upgradeName;
        this.newLevel = newLevel;
    }

    @Override
    public void execute(FactoryPlayer factoryPlayer) {
        factoryPlayer.getLevelledUpgradeHolder()
                .setUpgradeLevel(upgradeName, newLevel);
    }

}
