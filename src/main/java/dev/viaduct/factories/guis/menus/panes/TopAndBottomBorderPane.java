package dev.viaduct.factories.guis.menus.panes;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import dev.viaduct.factories.utils.ItemBuilder;
import org.bukkit.Material;

public abstract class TopAndBottomBorderPane extends StaticPane {

    public TopAndBottomBorderPane(int height, Material borderColor) {
        super(0, 0, 9, height);

        for (int i = 0; i < 9; i++) {
            if (height == 5) {
                addItem(new GuiItem(new ItemBuilder(borderColor).setCustomModelData(696916969).setName("&f").build(), click -> click.setCancelled(true)), i, 0);
                addItem(new GuiItem(new ItemBuilder(borderColor).setCustomModelData(696916969).setName("&f").build(), click -> click.setCancelled(true)), i, 4);
            } else {
                addItem(new GuiItem(new ItemBuilder(borderColor).setCustomModelData(696916969).setName("&f").build(), click -> click.setCancelled(true)), i, 0);
                addItem(new GuiItem(new ItemBuilder(borderColor).setCustomModelData(696916969).setName("&f").build(), click -> click.setCancelled(true)), i, 5);
            }
        }
    }

}
