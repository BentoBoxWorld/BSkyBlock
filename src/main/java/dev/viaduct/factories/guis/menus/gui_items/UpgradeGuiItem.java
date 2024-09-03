package dev.viaduct.factories.guis.menus.gui_items;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import dev.viaduct.factories.domain.players.FactoryPlayer;
import dev.viaduct.factories.guis.menus.display_items.UpgradeDisplayItem;
import dev.viaduct.factories.upgrades.Upgrade;

public class UpgradeGuiItem extends GuiItem {

    public UpgradeGuiItem(FactoryPlayer factoryPlayer, Upgrade upgrade, int level) {
        super(new UpgradeDisplayItem(upgrade, level).build(),
                click -> upgrade.tryUpgrade(factoryPlayer));
    }

}
