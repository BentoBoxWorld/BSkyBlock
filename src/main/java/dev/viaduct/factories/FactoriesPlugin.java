package dev.viaduct.factories;

import dev.viaduct.factories.registries.FactoryPlayerRegistry;
import dev.viaduct.factories.registries.RegistryManager;
import lombok.Getter;
import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.addons.GameModeAddon;
import world.bentobox.bentobox.api.addons.Pladdon;

public class FactoriesPlugin extends Pladdon {

    private GameModeAddon addon;

    @Getter
    public static FactoriesPlugin instance;
    @Getter
    public static RegistryManager registryManager;

    @Override
    public Addon getAddon() {
        if (addon == null) {
            addon = new Factories();
        }
        return addon;
    }

    @Override
    public void onEnable() {
        instance = this;
        registryManager = new RegistryManager();
    }

    private void initRegistries() {
        registryManager = new RegistryManager();

        registryManager.registerRegistry(FactoryPlayerRegistry.class, new FactoryPlayerRegistry());
    }

}
