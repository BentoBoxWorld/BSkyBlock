package dev.viaduct.factories.guis.menus.gui_items;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import dev.viaduct.factories.domain.players.FactoryPlayer;
import dev.viaduct.factories.upgrades.Upgrade;
import dev.viaduct.factories.utils.ItemBuilder;

public class UpgradeGuiItem extends GuiItem {

    public UpgradeGuiItem(FactoryPlayer factoryPlayer, Upgrade upgrade) {
        super(new ItemBuilder(upgrade.getIcon())
                .setName(upgrade.getDisplayName())
                .setLore(upgrade.getDescription())
                .addLoreLines("", "&e&oClick to upgrade")
                .build(), click -> upgrade.tryUpgrade(factoryPlayer));
    }

}
