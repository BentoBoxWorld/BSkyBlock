package dev.viaduct.factories.upgrades;

import dev.viaduct.factories.domain.players.FactoryPlayer;
import org.bukkit.Material;

import java.util.List;

public interface Upgrade {

    void tryUpgrade(FactoryPlayer factoryPlayer);

    int getBaseValue();

    int getMaxLevel();

    Material getIcon();

    String getDisplayName();

    List<String> getDescription();

}