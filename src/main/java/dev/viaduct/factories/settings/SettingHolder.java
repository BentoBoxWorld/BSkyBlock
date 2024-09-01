package dev.viaduct.factories.settings;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public class SettingHolder {

    private final Map<SettingType, Object> settings = new EnumMap<>(SettingType.class);

    public void registerSetting(SettingType type) {
        settings.put(type, type.getDefaultValue());
    }

    @SuppressWarnings("unchecked")
    public <T> T getSetting(SettingType type) {
        return (T) settings.get(type);
    }

    public <T> void addSetting(SettingType type, T value) {
        settings.put(type, value);
    }

    public <T> void modifySetting(SettingType type, Function<T, T> modifier) {
        T currentValue = getSetting(type);
        T newValue = modifier.apply(currentValue);
        addSetting(type, newValue);
    }

    public void initializeDefaultPlayerSettings() {
        Arrays.stream(SettingType.values())
                .forEach(this::registerSetting);
    }

}
