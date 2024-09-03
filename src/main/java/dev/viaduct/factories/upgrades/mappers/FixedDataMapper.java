package dev.viaduct.factories.upgrades.mappers;

import dev.viaduct.factories.exceptions.MaxLevelReachedException;

import java.util.HashMap;
import java.util.Map;

public class FixedDataMapper<T> implements LevelDataMapper<T> {

    private final Map<Integer, LevelData<T>> levelDataMap;

    // Using a list of values in parameter instead of a varargs array
    // To avoid heap pollution or confusion when calling the constructor
    public FixedDataMapper(Map<Integer, LevelData<T>> levelDataMap) {
        if (levelDataMap.isEmpty()) {
            throw new IllegalArgumentException("Values list cannot be empty");
        }
        this.levelDataMap = new HashMap<>(levelDataMap);
    }

    @Override
    public LevelData<T> getDataForLevel(int level) throws MaxLevelReachedException {
        if (level <= 0) {
            throw new IllegalArgumentException("Invalid level: " + level);
        }

        if (level > levelDataMap.size()) {
            throw new MaxLevelReachedException();
        }

        return levelDataMap.get(level);
    }

}
