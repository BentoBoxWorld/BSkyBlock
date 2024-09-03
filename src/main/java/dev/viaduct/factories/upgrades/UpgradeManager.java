package dev.viaduct.factories.upgrades;

import dev.viaduct.factories.upgrades.impl.LandLevelledUpgrade;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class UpgradeManager {

    public enum UpgradeName {
        LAND_SIZE_UPGRADE
    }

    private final Map<UpgradeName, Upgrade> upgradeMap;

    public UpgradeManager() {
        upgradeMap = new HashMap<>();
    }

    public Upgrade getUpgrade(UpgradeName upgradeName) {
        return upgradeMap.get(upgradeName);
    }

    public void init() {
        upgradeMap.put(UpgradeName.LAND_SIZE_UPGRADE, new LandLevelledUpgrade());
    }

}
