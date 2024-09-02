package dev.viaduct.factories.upgrades;

import dev.viaduct.factories.actions.impl.LandSizeUpgradeAction;
import dev.viaduct.factories.actions.impl.RemoveResourceAction;
import dev.viaduct.factories.conditions.impl.ResourceCondition;
import dev.viaduct.factories.upgrades.impl.LevelledUpgrade;
import lombok.Getter;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class UpgradeManager {

    public enum UpgradeName {
        LAND_SIZE_UPGRADE_1,
        LAND_SIZE_UPGRADE_2,
        LAND_SIZE_UPGRADE_3,
        LAND_SIZE_UPGRADE_4,
        LAND_SIZE_UPGRADE_5
    }

    private final Map<UpgradeName, Upgrade> upgradeMap;

    public UpgradeManager() {
        upgradeMap = new HashMap<>();
    }

    public void init() {
        upgradeMap.put(UpgradeName.LAND_SIZE_UPGRADE_1, new LevelledUpgrade(
                Material.ITEM_FRAME,
                "&a&lLand Size Upgrade: 1",
                List.of("&7Add 10 blocks to your land size!",
                        "",
                        "&7Cost: &f10x Wood"),
                new ResourceCondition("wood",
                        10,
                        new LandSizeUpgradeAction(1),
                        new RemoveResourceAction("wood", 10))));
        upgradeMap.put(UpgradeName.LAND_SIZE_UPGRADE_2, new LevelledUpgrade(
                Material.ITEM_FRAME,
                "&a&lLand Size Upgrade: 2",
                List.of("&7Add 10 blocks to your land size!",
                        "",
                        "&7Cost: &f10x Wood, 10x Stone"),
                new ResourceCondition("wood",
                        10,
                        new LandSizeUpgradeAction(2),
                        new RemoveResourceAction("wood", 10)),
                new ResourceCondition("stone", 10,
                        new RemoveResourceAction("stone", 10))));
    }

}
