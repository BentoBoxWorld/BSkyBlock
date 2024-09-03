package dev.viaduct.factories.guis.menus.display_items;

import dev.viaduct.factories.upgrades.Upgrade;
import dev.viaduct.factories.upgrades.impl.LevelledUpgrade;
import dev.viaduct.factories.utils.ItemBuilder;

public class UpgradeDisplayItem extends ItemBuilder {

    public UpgradeDisplayItem(Upgrade upgrade, int level) {
        super(upgrade.getIcon());

        setName(upgrade.getDisplayName())
                .setLore(upgrade.getDescription());

        if (level != 0) {
            addLoreLines("",
                    "&f&lInfo",
                    "&f • &6Current level: &e" + level,
                    "&f • &6Max level: &e" + upgrade.getMaxLevel());
        }

        if (!(upgrade instanceof LevelledUpgrade<?> levelledUpgrade)) return;

        int nextLevel = level + 1;

        addLoreLines(levelledUpgrade.getLevelDataInfo(nextLevel));
    }

}
