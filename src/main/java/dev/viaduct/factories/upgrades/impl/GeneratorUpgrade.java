package dev.viaduct.factories.upgrades.impl;

import dev.viaduct.factories.domain.players.FactoryPlayer;
import dev.viaduct.factories.upgrades.mappers.FixedDataMapper;
import dev.viaduct.factories.upgrades.mappers.LevelDataMapper;
import dev.viaduct.factories.utils.Chat;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class GeneratorUpgrade extends LevelledUpgrade<Integer> {

    public GeneratorUpgrade() {
        //TODO: Implement LevelData objects for Generator level mapping.
        super(new FixedDataMapper<>(Map.of()));
    }

    @Override
    public List<String> getLevelDataInfo(int level) {
        //TODO: Implement generator information (per-level parameters).
        return List.of();
    }

    @Override
    public void tryUpgrade(FactoryPlayer factoryPlayer) {
        //  For testing
        Chat.tell(factoryPlayer.getPlayer(),"Upgrade successfully attempted.");
    }

    @Override
    public int getBaseValue() {
        return 0;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public Material getIcon() {
        return Material.REDSTONE_LAMP;
    }

    @Override
    public String getDisplayName() {
        return "&6Generator Upgrade";
    }

    @Override
    public List<String> getDescription() {
        return List.of();
    }
}
