package dev.viaduct.factories;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import dev.viaduct.factories.listeners.GeneratorListener;
import dev.viaduct.factories.listeners.PlayerGetResourceListener;
import dev.viaduct.factories.listeners.PlayerJoinListener;
import dev.viaduct.factories.listeners.AccessibleLandListeners;
import dev.viaduct.factories.packets.listeners.ScoreboardPacketListener;
import dev.viaduct.factories.registries.impl.FactoryPlayerRegistry;
import dev.viaduct.factories.registries.RegistryManager;
import dev.viaduct.factories.registries.impl.GeneratorRegistry;
import dev.viaduct.factories.resources.ResourceManager;
import dev.viaduct.factories.upgrades.UpgradeManager;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
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
    public UpgradeManager upgradeManager;

    @Override
    public Addon getAddon() {
        if (addon == null) {
            addon = new Factories();
        }
        return addon;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        instance = this;
        initRegistries();
        registerListeners();

        PacketEvents.getAPI().getEventManager().registerListener(new ScoreboardPacketListener(),
                PacketListenerPriority.LOW);

        PacketEvents.getAPI().init();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        PacketEvents.getAPI().terminate();
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerGetResourceListener(registryManager
                .getRegistry(FactoryPlayerRegistry.class)), this);
        pluginManager.registerEvents(new AccessibleLandListeners(registryManager
                .getRegistry(FactoryPlayerRegistry.class)), this);
        pluginManager.registerEvents(new GeneratorListener(registryManager
                .getRegistry(FactoryPlayerRegistry.class), registryManager
                .getRegistry(GeneratorRegistry.class)), this);
    }

    private void initRegistries() {
        registryManager = new RegistryManager();

        registryManager.registerRegistry(FactoryPlayerRegistry.class, new FactoryPlayerRegistry());
        GeneratorRegistry registry = new GeneratorRegistry();
        registryManager.registerRegistry(GeneratorRegistry.class, registry);

        Bukkit.getScheduler().runTaskLater(this, registry::initialize, 20L);

        resourceManager = new ResourceManager();
        resourceManager.registerResources();

        upgradeManager = new UpgradeManager();
        upgradeManager.init();
    }

}
