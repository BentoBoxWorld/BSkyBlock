package dev.viaduct.factories.resources.mineable.impl;

import dev.viaduct.factories.resources.Resource;
import dev.viaduct.factories.utils.Chat;
import org.bukkit.Material;

public class Stone extends Resource {

    public Stone() {
        super("Stone", 1.0, Material.STONE);
    }

    public Stone(String name, double incrementAmount, Material... materials) {
        super(name, incrementAmount, materials);
    }

    @Override
    public String getFormattedName() {
        return Chat.colorizeHex("#bfbfbf" + getName() + ": ");
    }

}
