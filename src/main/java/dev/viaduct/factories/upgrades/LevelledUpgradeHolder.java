package dev.viaduct.factories.upgrades;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public class LevelledUpgradeHolder {

    private final Map<UpgradeManager.UpgradeName, Integer> upgradeLevelMap;

    public LevelledUpgradeHolder() {
        this.upgradeLevelMap = new HashMap<>();
    }

    public Integer getUpgradeLevel(UpgradeManager.UpgradeName upgradeName) {
        return upgradeLevelMap.get(upgradeName);
    }

    public void setUpgradeLevel(UpgradeManager.UpgradeName upgradeName, int level) {
        upgradeLevelMap.put(upgradeName, level);
    }

    public void initializeDefaultUpgrades() {
        Arrays.stream(UpgradeManager.UpgradeName.values())
                .forEach(upgradeName -> upgradeLevelMap.put(upgradeName, 0));
    }

}
