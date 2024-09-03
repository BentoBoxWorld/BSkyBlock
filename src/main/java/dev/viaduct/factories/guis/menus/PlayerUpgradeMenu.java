package dev.viaduct.factories.guis.menus;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import dev.viaduct.factories.FactoriesPlugin;
import dev.viaduct.factories.domain.players.FactoryPlayer;
import dev.viaduct.factories.guis.menus.gui_items.UpgradeGuiItem;
import dev.viaduct.factories.guis.menus.panes.TopAndBottomSixPane;
import dev.viaduct.factories.upgrades.Upgrade;
import dev.viaduct.factories.upgrades.UpgradeManager;
import dev.viaduct.factories.utils.Chat;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerUpgradeMenu {

    public void showToPlayer(FactoryPlayer factoryPlayer) {
        ChestGui gui = new ChestGui(6, Chat.colorize("&b&lYour Upgrade Menu"));
        gui.setOnTopClick(event -> event.setCancelled(true));

        TopAndBottomSixPane topAndBottomSixPane = new TopAndBottomSixPane(Material.BLUE_STAINED_GLASS_PANE);

        PaginatedPane paginatedPane = new PaginatedPane(0, 1, 9, 4);
        paginatedPane.setOnClick(event -> factoryPlayer.getPlayer().closeInventory());

        List<GuiItem> upgradeGuiItems = getGuiItems(factoryPlayer);

        paginatedPane.populateWithGuiItems(upgradeGuiItems);

        gui.addPane(topAndBottomSixPane);
        gui.addPane(paginatedPane);
        gui.show(factoryPlayer.getPlayer());
    }

    private @NotNull List<GuiItem> getGuiItems(FactoryPlayer factoryPlayer) {
        Map<UpgradeManager.UpgradeName, Integer> upgradeLevelMap = factoryPlayer.getLevelledUpgradeHolder()
                .getUpgradeLevelMap();
        UpgradeManager upgradeManager = FactoriesPlugin.getInstance().getUpgradeManager();

        List<GuiItem> upgradeGuiItems = new ArrayList<>();
        upgradeLevelMap.forEach((upgradeName, level) -> {
            Upgrade upgrade = upgradeManager.getUpgrade(upgradeName);
            upgradeGuiItems.add(new UpgradeGuiItem(factoryPlayer, upgrade, level));
        });
        return upgradeGuiItems;
    }

}