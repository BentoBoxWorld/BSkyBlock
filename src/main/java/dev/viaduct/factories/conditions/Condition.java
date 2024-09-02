package dev.viaduct.factories.conditions;

import dev.viaduct.factories.actions.Action;
import dev.viaduct.factories.domain.players.FactoryPlayer;

import java.util.List;

public interface Condition {

    boolean isMet(FactoryPlayer factoryPlayer);

    List<Action> getActions();

}
