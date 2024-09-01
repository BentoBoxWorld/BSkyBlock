package dev.viaduct.factories.settings;

import dev.viaduct.factories.domain.lands.Land;
import lombok.Getter;
import java.util.function.Supplier;

@Getter
public enum SettingType {

    ACCESSIBLE_LAND_BLOCKS(Double.class, () -> 10.0),
    PLAYER_LAND(Land.class, () -> null);

    private final Class<?> type;
    private final Supplier<?> defaultSupplier;

    SettingType(Class<?> type, Supplier<?> defaultSupplier) {
        this.type = type;
        this.defaultSupplier = defaultSupplier;
    }

    @SuppressWarnings("unchecked")
    public <T> T getDefaultValue() {
        return (T) defaultSupplier.get();
    }

}
