package dev.viaduct.factories.resources.impl;

import dev.viaduct.factories.resources.Resource;
import dev.viaduct.factories.utils.Chat;
import org.bukkit.Material;

public class Wood extends Resource {

    public Wood() {
        super("Wood", Material.OAK_WOOD);
    }

    public Wood(String name, Material... materials) {
        super(name, materials);
    }

    @Override
    public String getFormattedName() {
        return Chat.colorizeHex("#a8996f" + getName() + ": ");
    }

}