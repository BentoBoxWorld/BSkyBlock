package dev.viaduct.factories.resources.currency.impl;

import dev.viaduct.factories.resources.currency.Currency;
import dev.viaduct.factories.utils.Chat;
import org.bukkit.Material;

public class Credit extends Currency {

    public Credit() {
        super("Credit", Material.EMERALD);
    }

    @Override
    public String getFormattedName() {
        return Chat.colorizeHex("#FFD700" + getName() + ": ");
    }
}
