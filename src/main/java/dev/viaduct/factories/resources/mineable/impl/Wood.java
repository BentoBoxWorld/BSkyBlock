package dev.viaduct.factories.resources.mineable.impl;

import dev.viaduct.factories.resources.Resource;
import dev.viaduct.factories.utils.Chat;
import org.bukkit.Material;

public class Wood extends Resource {

    public Wood() {
        super("Wood", 1.0, Material.OAK_WOOD);
    }

    public Wood(String name, double incrementAmount, Material... materials) {
        super(name, incrementAmount, materials);
    }

    @Override
    public String getFormattedName() {
        return Chat.colorizeHex("#a8996f" + getName() + ": ");
    }

}