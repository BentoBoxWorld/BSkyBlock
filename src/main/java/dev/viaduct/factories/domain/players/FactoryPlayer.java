package dev.viaduct.factories.domain.players;

import dev.viaduct.factories.FactoriesPlugin;
import dev.viaduct.factories.domain.banks.Bank;
import dev.viaduct.factories.domain.banks.impl.CreditBank;
import dev.viaduct.factories.domain.banks.impl.ResourceBank;
import dev.viaduct.factories.generators.Generator;
import dev.viaduct.factories.generators.GeneratorHolder;
import dev.viaduct.factories.guis.scoreboards.FactoryScoreboard;
import dev.viaduct.factories.registries.impl.FactoryPlayerRegistry;
import dev.viaduct.factories.registries.impl.GeneratorRegistry;
import dev.viaduct.factories.resources.currency.impl.Credit;
import dev.viaduct.factories.settings.SettingHolder;
import dev.viaduct.factories.upgrades.LevelledUpgradeHolder;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
public class FactoryPlayer {

    private final Player player;
    private final ResourceBank resourceBank;
    private final CreditBank creditBank;
    private final FactoryScoreboard scoreboard;
    private final SettingHolder settingHolder;
    private final LevelledUpgradeHolder levelledUpgradeHolder;
    private final GeneratorHolder generatorHolder;

    public FactoryPlayer(Player player) {
        this.player = player;
        this.resourceBank = new ResourceBank();
        this.creditBank = new CreditBank();
        this.scoreboard = new FactoryScoreboard(this);
        this.settingHolder = new SettingHolder();
        this.levelledUpgradeHolder = new LevelledUpgradeHolder();
        this.generatorHolder = new GeneratorHolder();
        resourceBank.addToResource("wood", scoreboard, 1000);
        resourceBank.addToResource("stone", scoreboard, 1000);
        creditBank.addToResource("credits", scoreboard, 100);
    }

    public void addGenerator(Location location, Generator generator) {
        generatorHolder.addGenerator(location, generator);
    }

    public void register() {
        FactoriesPlugin.getRegistryManager()
                .getRegistry(FactoryPlayerRegistry.class)
                .register(player.getUniqueId(), this);
        levelledUpgradeHolder.initializeDefaultUpgrades();
        settingHolder.initializeDefaultPlayerSettings(this);

        FactoriesPlugin.getRegistryManager()
                .getRegistry(GeneratorRegistry.class)
                .getAllValues()
                .forEach(generator -> player.getInventory().addItem(generator.getGeneratorPlaceItem()));
    }

}
