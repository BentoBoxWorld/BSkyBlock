package dev.viaduct.factories.upgrades.mappers;

import dev.viaduct.factories.conditions.AbstractCondition;
import dev.viaduct.factories.conditions.ConditionHolder;
import lombok.Getter;

import java.util.List;

@Getter
public class LevelData<T> {

    private final T value;
    private final ConditionHolder conditionHolder;

    public LevelData(T value, AbstractCondition... conditions) {
        this.value = value;
        this.conditionHolder = new ConditionHolder(conditions);
    }

    public List<String> getLevelConditions() {
        return conditionHolder.getConditionStrings();
    }

}
