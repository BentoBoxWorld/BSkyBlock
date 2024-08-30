package dev.viaduct.factories.resources.impl;

import dev.viaduct.factories.resources.Resource;
import dev.viaduct.factories.utils.Chat;
import org.bukkit.Material;

public class Stone extends Resource {

    public Stone() { super("Stone", Material.STONE); }

    public Stone(String name, Material... materials) { super(name, materials); }

    @Override
    public String getFormattedName() {
        return Chat.colorizeHex("#bfbfbf" + getName() + ": ");
    }

}
