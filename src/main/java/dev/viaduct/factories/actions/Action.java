package dev.viaduct.factories.actions;

import dev.viaduct.factories.domain.players.FactoryPlayer;

public interface Action {

    void execute(FactoryPlayer factoryPlayer);

}
