package dev.viaduct.factories.settings;

import dev.viaduct.factories.domain.lands.Land;
import dev.viaduct.factories.domain.players.FactoryPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SettingHolder {

    private final Map<SettingType<?>, Object> settings = new HashMap<>();

    public <T> void registerSetting(SettingType<T> type, T defaultValue) {
        settings.put(type, defaultValue);
    }

    @SuppressWarnings("unchecked")
    public <T> T getSetting(SettingType<T> type) {
        return (T) settings.get(type);
    }

    public <T> void addSetting(SettingType<T> type, T value) {
        settings.put(type, value);
    }

    public <T> void modifySetting(SettingType<T> type, Function<T, T> modifier) {
        T currentValue = getSetting(type);
        T newValue = modifier.apply(currentValue);
        addSetting(type, newValue);
    }

    public void initializeDefaultPlayerSettings(FactoryPlayer factoryPlayer) {
        registerSetting(SettingType.PLAYER_LAND, new Land(factoryPlayer));
    }

}
