package dev.viaduct.factories.settings;

import dev.viaduct.factories.domain.lands.Land;

public class SettingType<T> {

    public static final SettingType<Land> PLAYER_LAND = new SettingType<>();

    private SettingType() {
    }

}
