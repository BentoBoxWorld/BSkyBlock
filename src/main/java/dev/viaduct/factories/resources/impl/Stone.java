package dev.viaduct.factories.resources.impl;

import dev.viaduct.factories.resources.Resource;
import dev.viaduct.factories.utils.Chat;

public class Stone extends Resource {

    public Stone() {
        super("Stone");
    }

    @Override
    public String getFormattedName() {
        return Chat.colorizeHex("#bfbfbfStone: ");
    }

}
