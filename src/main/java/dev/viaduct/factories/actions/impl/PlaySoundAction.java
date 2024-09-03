package dev.viaduct.factories.actions.impl;

import dev.viaduct.factories.actions.Action;
import dev.viaduct.factories.domain.players.FactoryPlayer;
import org.bukkit.Sound;

public class PlaySoundAction implements Action {

    private final Sound sound;

    public PlaySoundAction(Sound sound) {
        this.sound = sound;
    }

    @Override
    public void execute(FactoryPlayer factoryPlayer) {
        factoryPlayer.getPlayer().playSound(factoryPlayer.getPlayer(),
                sound, 1, 1);
    }

}
