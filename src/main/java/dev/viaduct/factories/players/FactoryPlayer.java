package dev.viaduct.factories.players;

import dev.viaduct.factories.FactoriesPlugin;
import dev.viaduct.factories.banks.Bank;
import dev.viaduct.factories.registries.FactoryPlayerRegistry;
import org.bukkit.entity.Player;

public class FactoryPlayer {

    private final Player player;
    private final Bank bank;

    public FactoryPlayer(Player player) {
        this.player = player;
        this.bank = new Bank();
    }

    public void register() {
        FactoriesPlugin.getRegistryManager()
                .getRegistry(FactoryPlayerRegistry.class)
                .register(player.getUniqueId(), this);
    }

}
