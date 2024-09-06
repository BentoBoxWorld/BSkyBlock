package dev.viaduct.factories.upgrades.impl;

import dev.viaduct.factories.actions.impl.ChatMessageAction;
import dev.viaduct.factories.actions.impl.LandUpgradeAction;
import dev.viaduct.factories.actions.impl.PlaySoundAction;
import dev.viaduct.factories.actions.impl.RemoveResourceAction;
import dev.viaduct.factories.conditions.ConditionHolder;
import dev.viaduct.factories.conditions.impl.ResourceCondition;
import dev.viaduct.factories.domain.players.FactoryPlayer;
import dev.viaduct.factories.exceptions.MaxLevelReachedException;
import dev.viaduct.factories.upgrades.UpgradeManager;
import dev.viaduct.factories.upgrades.mappers.FixedDataMapper;
import dev.viaduct.factories.upgrades.mappers.LevelData;
import dev.viaduct.factories.utils.Chat;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LandLevelledUpgrade extends LevelledUpgrade<Integer> {
    // Actions won't be executed if unless all conditions are not met
    public LandLevelledUpgrade() {
        super(new FixedDataMapper<>(Map.of(
                // Level 1
                1, new LevelData<>(
                        14, true, true,
                        new ResourceCondition("wood", 100,
                            new RemoveResourceAction("wood", 100),
                            new LandUpgradeAction(1))),
                // Level 2
                2, new LevelData<>(
                        18, true, true,
                        new ResourceCondition("wood", 200, // requires 200 wood
                            new RemoveResourceAction("wood", 200)), // remove 200 wood
                        new ResourceCondition("stone", 100, // requires 100 stone
                                new RemoveResourceAction("stone", 100), // remove 100 stone
                                new LandUpgradeAction(2))),
                //  Level 3
                3, new LevelData<>(
                        22, true, true,
                        new ResourceCondition("wood", 400,  // requires 400 wood
                            new RemoveResourceAction("wood", 200)), // remove 400 wood
                            new ResourceCondition("stone", 200, // requires 200 stone
                                    new RemoveResourceAction("stone", 100), // remove 200 stone
                                    new LandUpgradeAction(3))))));

    }

    @Override
    public void tryUpgrade(FactoryPlayer factoryPlayer) {
        int currentLevel = factoryPlayer.getLevelledUpgradeHolder()
                .getUpgradeLevel(UpgradeManager.UpgradeName.LAND_SIZE_UPGRADE);

        if (currentLevel >= getMaxLevel()) {
            Chat.tell(factoryPlayer.getPlayer(), "&cYou have reached the maximum level for this upgrade.");
            return;
        }

        int nextLevel = currentLevel + 1;

        if (nextLevel <= 0) {
            throw new IllegalArgumentException("Invalid level: " + nextLevel);
        }

        LevelData<Integer> levelData;
        try {
            levelData = levelDataMapper.getDataForLevel(nextLevel);
        } catch (MaxLevelReachedException exception) {
            Chat.tell(factoryPlayer.getPlayer(), "&cYou have reached the maximum level for this upgrade.");
            return;
        }

        if (levelData == null) {
            throw new IllegalArgumentException("No data found for level: " + nextLevel);
        }

        ConditionHolder conditionHolder = levelData.getConditionHolder();
        if (!conditionHolder.allConditionsMet(factoryPlayer)) return;

        conditionHolder.executeActions(factoryPlayer); // Perform the actions for this level
    }

    @Override
    public int getBaseValue() {
        return 10;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public Material getIcon() {
        return Material.ITEM_FRAME;
    }

    @Override
    public String getDisplayName() {
        return "&6Land Level Upgrade";
    }

    @Override
    public List<String> getDescription() {
        return List.of("&7Upgrade your land level to", "&7increase the amount of", "&7land you can access.");
    }

    // TODO: Refactor out the level data info into a separate method
    @Override
    public List<String> getLevelDataInfo(int nextLevel) {
        final LevelData<Integer> levelData;

        try {
            levelData = levelDataMapper.getDataForLevel(nextLevel);
        } catch (MaxLevelReachedException exception) {
            return List.of("", "&cMaximum level reached!");
        }

        String levelBoost = "&7Land size → &e" + levelData.getValue() + " &7blocks";
        List<String> levelConditions = levelData.getLevelConditions();

        List<String> descriptionOfNextLevel = new ArrayList<>();
        descriptionOfNextLevel.add("");
        descriptionOfNextLevel.add(Chat.colorize("&f&lNext → &eLevel " + nextLevel));
        descriptionOfNextLevel.add("");
        descriptionOfNextLevel.add("&7Gains");
        descriptionOfNextLevel.add(" &7• " + levelBoost);
        descriptionOfNextLevel.add("");
        descriptionOfNextLevel.add("&7Requirements");

        for (String condition : levelConditions) {
            descriptionOfNextLevel.add(" &7• " + condition);
        }

        descriptionOfNextLevel.add("");
        descriptionOfNextLevel.add("&6Click to upgrade to next level!");

        return descriptionOfNextLevel;
    }

}