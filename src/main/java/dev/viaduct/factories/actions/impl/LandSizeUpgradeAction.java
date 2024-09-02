package dev.viaduct.factories.actions.impl;

import dev.viaduct.factories.actions.Action;
import dev.viaduct.factories.domain.players.FactoryPlayer;
import dev.viaduct.factories.settings.SettingType;
import lombok.Getter;

@Getter
public class LandSizeUpgradeAction implements Action {

    private final int level;

    public LandSizeUpgradeAction(int level) {
        this.level = level;
    }

    @Override
    public void execute(FactoryPlayer factoryPlayer) {
        factoryPlayer.getSettingHolder()
                .getSetting(SettingType.PLAYER_LAND)
                .setLevel(level);
    }

}
