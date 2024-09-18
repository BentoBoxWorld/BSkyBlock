package dev.viaduct.factories.resources.currency;

import dev.viaduct.factories.resources.Resource;
import org.bukkit.Material;

public abstract class Currency extends Resource {
    protected Currency(String name, Material... materials) {
        super(name, 1.0, materials);
    }

    protected Currency(String name) {
        super(name, 1.0);
    }

    @Override
    public abstract String getFormattedName();
}
