package dev.viaduct.factories;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import dev.viaduct.factories.listeners.PlayerGetResourceListener;
import dev.viaduct.factories.listeners.PlayerJoinListener;
import dev.viaduct.factories.listeners.PlayerMoveListener;
import dev.viaduct.factories.packets.listeners.ScoreboardPacketListener;
import dev.viaduct.factories.registries.FactoryPlayerRegistry;
import dev.viaduct.factories.registries.RegistryManager;
import dev.viaduct.factories.resources.ResourceManager;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
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
    public void onLoad() {
        super.onLoad();

        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        instance = this;
        initRegistries();
        getServer().getPluginManager()
                .registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(
                new PlayerGetResourceListener(registryManager.getRegistry(FactoryPlayerRegistry.class)), this);
        getServer().getPluginManager()
                .registerEvents(new PlayerMoveListener(registryManager.getRegistry(FactoryPlayerRegistry.class)), this);

        PacketEvents.getAPI().getEventManager().registerListener(new ScoreboardPacketListener(),
                PacketListenerPriority.LOW);

        PacketEvents.getAPI().init();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        PacketEvents.getAPI().terminate();
    }

    private void initRegistries() {
        registryManager = new RegistryManager();

        registryManager.registerRegistry(FactoryPlayerRegistry.class, new FactoryPlayerRegistry());

        resourceManager = new ResourceManager();
        resourceManager.registerResources();
    }

}
