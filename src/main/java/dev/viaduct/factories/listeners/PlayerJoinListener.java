package dev.viaduct.factories.listeners;

import dev.viaduct.factories.players.FactoryPlayer;
import dev.viaduct.factories.registries.FactoryPlayerRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener extends AutoListener {

    public PlayerJoinListener() {
        super();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new FactoryPlayer(event.getPlayer()).register();
    }

}
