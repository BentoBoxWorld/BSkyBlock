package dev.viaduct.factories.upgrades;

import dev.viaduct.factories.domain.players.FactoryPlayer;
import org.bukkit.Material;

import java.util.List;

public interface Upgrade {

    void tryUpgrade(FactoryPlayer factoryPlayer);

    Material getIcon();

    String getDisplayName();

    List<String> getDescription();

}
