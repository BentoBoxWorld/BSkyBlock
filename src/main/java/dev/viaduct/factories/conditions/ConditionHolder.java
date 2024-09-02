package dev.viaduct.factories.conditions;

import dev.viaduct.factories.actions.Action;
import dev.viaduct.factories.domain.players.FactoryPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConditionHolder {

    private final List<AbstractCondition> conditions;

    public ConditionHolder(AbstractCondition... conditions) {
        this.conditions = new ArrayList<>(Arrays.asList(conditions));
    }

    public void addCondition(AbstractCondition condition) {
        conditions.add(condition);
    }

    public boolean allConditionsMet(FactoryPlayer factoryPlayer) {
        return conditions.stream().allMatch(condition -> condition.isMet(factoryPlayer));
    }

    public void executeActions(FactoryPlayer factoryPlayer) {
        conditions.forEach(condition -> condition.getActions()
                .forEach(action -> action.execute(factoryPlayer)));
    }

    public List<Action> getActions() {
        List<Action> actions = new ArrayList<>();
        conditions.forEach(condition -> actions.addAll(condition.getActions()));
        return actions;
    }

}
