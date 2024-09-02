package dev.viaduct.factories.guis.menus;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import dev.viaduct.factories.FactoriesPlugin;
import dev.viaduct.factories.domain.players.FactoryPlayer;
import dev.viaduct.factories.guis.menus.gui_items.UpgradeGuiItem;
import dev.viaduct.factories.guis.menus.panes.TopAndBottomSixPane;
import dev.viaduct.factories.upgrades.Upgrade;
import dev.viaduct.factories.utils.Chat;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collection;

public class PlayerUpgradeMenu {

    public void showToPlayer(FactoryPlayer factoryPlayer) {
        ChestGui gui = new ChestGui(6, Chat.colorize("&b&lYour Upgrade Menu"));
        TopAndBottomSixPane topAndBottomSixPane = new TopAndBottomSixPane(Material.BLUE_STAINED_GLASS_PANE);

        StaticPane staticPane = new StaticPane(0, 1, 9, 4);

        gui.setOnTopClick(event -> event.setCancelled(true));

        Collection<Upgrade> values = FactoriesPlugin.getInstance().getUpgradeManager()
                .getUpgradeMap()
                .values();
        ArrayList<Upgrade> upgradeList = new ArrayList<>(values);

        int i = 0;
        for (Upgrade upgrade : upgradeList) {
            staticPane.addItem(new UpgradeGuiItem(factoryPlayer, upgrade), i, 0);
            i++;
        }

        gui.addPane(topAndBottomSixPane);
        gui.addPane(staticPane);
        gui.show(factoryPlayer.getPlayer());
    }

}