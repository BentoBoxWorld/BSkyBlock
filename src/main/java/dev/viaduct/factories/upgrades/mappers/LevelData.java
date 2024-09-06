package dev.viaduct.factories.upgrades.mappers;

import dev.viaduct.factories.conditions.AbstractCondition;
import dev.viaduct.factories.conditions.ConditionHolder;
import dev.viaduct.factories.domain.players.FactoryPlayer;
import lombok.Getter;

import java.util.List;

import static dev.viaduct.factories.upgrades.UpgradeManager.UPGRADE_MSG;
import static dev.viaduct.factories.upgrades.UpgradeManager.UPGRADE_SOUND;

@Getter
public class LevelData<T> {

    private final T value;
    private final ConditionHolder conditionHolder;

    public LevelData(T value, AbstractCondition... conditions) {
        this.value = value;
        this.conditionHolder = new ConditionHolder(conditions);
    }

    //  Constructor for LevelData that includes an upgrade message and/or upgrade sound.
    public LevelData(T value, boolean hasUpgradeSound, boolean hasUpgradeMessage, AbstractCondition... conditions) {
        this.value = value;
        if (hasUpgradeMessage) conditions[conditions.length - 1].getActions().add(UPGRADE_MSG);
        if (hasUpgradeSound) conditions[conditions.length - 1].getActions().add(UPGRADE_SOUND);
        this.conditionHolder = new ConditionHolder(conditions);
    }

    public List<String> getLevelConditions() {
        return conditionHolder.getConditionStrings();
    }

}
