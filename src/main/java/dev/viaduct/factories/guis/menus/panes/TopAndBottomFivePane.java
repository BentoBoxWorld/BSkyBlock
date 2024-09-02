package dev.viaduct.factories.guis.menus.panes;

import org.bukkit.Material;

public class TopAndBottomFivePane extends TopAndBottomBorderPane {

    public TopAndBottomFivePane() {
        super(5, Material.GRAY_STAINED_GLASS_PANE);
    }

    public TopAndBottomFivePane(Material borderColor) {
        super(5, borderColor);
    }

}
