package dev.viaduct.factories.conditions;

import dev.viaduct.factories.actions.Action;
import dev.viaduct.factories.domain.players.FactoryPlayer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public abstract class AbstractCondition implements Condition {

    private final List<Action> actions;

    public AbstractCondition(Action... actions) {
        this.actions = new ArrayList<>(Arrays.asList(actions));
    }

    @Override
    public void executeActions(FactoryPlayer factoryPlayer) {
        actions.forEach(action -> action.execute(factoryPlayer));
    }

}
