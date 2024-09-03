package dev.viaduct.factories.conditions;

import dev.viaduct.factories.domain.players.FactoryPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConditionHolder {

    private final List<AbstractCondition> conditions;

    public ConditionHolder(AbstractCondition... conditions) {
        this.conditions = new ArrayList<>(Arrays.asList(conditions));
    }

    public boolean allConditionsMet(FactoryPlayer factoryPlayer) {
        return conditions.stream().allMatch(condition -> condition.isMet(factoryPlayer));
    }

    public void executeActions(FactoryPlayer factoryPlayer) {
        conditions.forEach(condition -> condition.executeActions(factoryPlayer));
    }

    public List<String> getConditionStrings() {
        List<String> conditionStrings = new ArrayList<>();
        conditions.forEach(condition -> conditionStrings.add(condition.toString()));
        return conditionStrings;
    }

}
