package dev.viaduct.factories.upgrades;

import dev.viaduct.factories.upgrades.impl.LevelledUpgrade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LevelledUpgradeHolder {

    private final List<LevelledUpgrade> upgradeList;
    private int level = 0;

    public LevelledUpgradeHolder(LevelledUpgrade... upgrades) {
        upgradeList = new ArrayList<>(Arrays.asList(upgrades));
    }

    public LevelledUpgrade getUpgrade() {
        return upgradeList.get(level);
    }


}
