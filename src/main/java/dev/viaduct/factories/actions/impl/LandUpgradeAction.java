package dev.viaduct.factories.actions.impl;

import dev.viaduct.factories.domain.players.FactoryPlayer;
import dev.viaduct.factories.settings.SettingType;
import dev.viaduct.factories.upgrades.UpgradeManager;

public class LandUpgradeAction extends UpgradeLevelAction {

    public LandUpgradeAction(int newLevel) {
        super(UpgradeManager.UpgradeName.LAND_SIZE_UPGRADE, newLevel);
    }

    @Override
    public void execute(FactoryPlayer factoryPlayer) {
        super.execute(factoryPlayer);
        factoryPlayer.getSettingHolder()
                .getSetting(SettingType.PLAYER_LAND)
                .setAccessibleLand();
    }

}
