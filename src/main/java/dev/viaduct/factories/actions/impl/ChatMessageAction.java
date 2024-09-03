package dev.viaduct.factories.actions.impl;

import dev.viaduct.factories.actions.Action;
import dev.viaduct.factories.domain.players.FactoryPlayer;
import dev.viaduct.factories.utils.Chat;

public class ChatMessageAction implements Action {

    private final String message;

    public ChatMessageAction(String message) {
        this.message = message;
    }

    @Override
    public void execute(FactoryPlayer factoryPlayer) {
        Chat.tell(factoryPlayer.getPlayer(), message);
    }

}
