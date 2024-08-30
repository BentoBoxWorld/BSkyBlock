package dev.viaduct.factories;

import dev.viaduct.factories.listeners.PlayerGetResourceListener;
import dev.viaduct.factories.listeners.PlayerJoinListener;
import dev.viaduct.factories.registries.FactoryPlayerRegistry;
import dev.viaduct.factories.registries.RegistryManager;
import dev.viaduct.factories.resources.ResourceManager;
import lombok.Getter;
import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.addons.GameModeAddon;
import world.bentobox.bentobox.api.addons.Pladdon;

@Getter
public class FactoriesPlugin extends Pladdon {

    private GameModeAddon addon;

    @Getter
    public static FactoriesPlugin instance;
    @Getter
    public static RegistryManager registryManager;

    public ResourceManager resourceManager;

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
        initRegistries();
        getServer().getPluginManager()
                .registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(
                new PlayerGetResourceListener(registryManager.getRegistry(FactoryPlayerRegistry.class)), this);
    }

    private void initRegistries() {
        registryManager = new RegistryManager();

        registryManager.registerRegistry(FactoryPlayerRegistry.class, new FactoryPlayerRegistry());

        resourceManager = new ResourceManager();
        resourceManager.registerResources();
    }

}
