package dev.viaduct.factories.listeners;

import dev.viaduct.factories.FactoriesPlugin;
import org.bukkit.event.Listener;

public class AutoListener implements Listener {

    public AutoListener() {
        FactoriesPlugin.getInstance()
                .getServer()
                .getPluginManager()
                .registerEvents(this, FactoriesPlugin.getInstance());
    }

}
