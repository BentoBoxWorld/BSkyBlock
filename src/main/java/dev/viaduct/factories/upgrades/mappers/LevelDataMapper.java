package dev.viaduct.factories.upgrades.mappers;

import dev.viaduct.factories.exceptions.MaxLevelReachedException;

public interface LevelDataMapper<T> {

    LevelData<T> getDataForLevel(int level) throws MaxLevelReachedException;

}
