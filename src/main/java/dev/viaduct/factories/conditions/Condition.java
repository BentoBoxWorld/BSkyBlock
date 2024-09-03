package dev.viaduct.factories.conditions;

import dev.viaduct.factories.domain.players.FactoryPlayer;

public interface Condition {

    boolean isMet(FactoryPlayer factoryPlayer);

    void executeActions(FactoryPlayer factoryPlayer);

}
