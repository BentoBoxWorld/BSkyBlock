package dev.viaduct.factories.upgrades.impl;

import dev.viaduct.factories.actions.impl.ChatMessageAction;
import dev.viaduct.factories.actions.impl.PlaySoundAction;
import dev.viaduct.factories.exceptions.MaxLevelReachedException;
import dev.viaduct.factories.upgrades.Upgrade;
import dev.viaduct.factories.upgrades.mappers.LevelData;
import dev.viaduct.factories.upgrades.mappers.LevelDataMapper;
import lombok.Getter;
import org.bukkit.Sound;

import java.util.List;

@Getter
public abstract class LevelledUpgrade<T> implements Upgrade {

    protected final LevelDataMapper<T> levelDataMapper;

    public LevelledUpgrade(LevelDataMapper<T> levelDataMapper) {
        this.levelDataMapper = levelDataMapper;
    }

    public LevelData<T> getDataForLevel(int level) throws MaxLevelReachedException {
        return levelDataMapper.getDataForLevel(level);
    }

    public abstract List<String> getLevelDataInfo(int level);

}