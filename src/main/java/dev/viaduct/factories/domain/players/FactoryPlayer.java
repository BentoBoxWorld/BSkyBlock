package dev.viaduct.factories.domain.players;

import dev.viaduct.factories.FactoriesPlugin;
import dev.viaduct.factories.domain.banks.Bank;
import dev.viaduct.factories.guis.scoreboards.FactoryScoreboard;
import dev.viaduct.factories.registries.FactoryPlayerRegistry;
import dev.viaduct.factories.settings.SettingHolder;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class FactoryPlayer {

    private final Player player;
    private final Bank bank;
    private final FactoryScoreboard scoreboard;
    private final SettingHolder settingHolder;

    public FactoryPlayer(Player player) {
        this.player = player;
        this.bank = new Bank();
        this.scoreboard = new FactoryScoreboard(this);
        this.settingHolder = new SettingHolder();
    }

    public void register() {
        FactoriesPlugin.getRegistryManager()
                .getRegistry(FactoryPlayerRegistry.class)
                .register(player.getUniqueId(), this);
        settingHolder.initializeDefaultPlayerSettings(this);
    }

}
