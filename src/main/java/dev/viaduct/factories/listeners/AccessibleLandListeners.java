package dev.viaduct.factories.listeners;

import dev.viaduct.factories.FactoriesPlugin;
import dev.viaduct.factories.domain.lands.Land;
import dev.viaduct.factories.guis.menus.PlayerUpgradeMenu;
import dev.viaduct.factories.registries.FactoryPlayerRegistry;
import dev.viaduct.factories.settings.SettingHolder;
import dev.viaduct.factories.settings.SettingType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class AccessibleLandListeners implements Listener {

    private final FactoryPlayerRegistry factoryPlayerRegistry;

    public AccessibleLandListeners(FactoryPlayerRegistry factoryPlayerRegistry) {
        this.factoryPlayerRegistry = factoryPlayerRegistry;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        factoryPlayerRegistry.get(event.getPlayer().getUniqueId()).ifPresent(factoryPlayer -> {
            Player player = factoryPlayer.getPlayer();

            String worldPlayerIsInName = player.getWorld().getName();
            String islandWorldName = FactoriesPlugin.getInstance()
                    .getConfig()
                    .getString("world.world-name");

            if (!worldPlayerIsInName.equals(islandWorldName)) return;

            SettingHolder settingHolder = factoryPlayer.getSettingHolder();

            if (settingHolder.getSetting(SettingType.PLAYER_LAND) == null) return;
            Land playerLand = settingHolder.getSetting(SettingType.PLAYER_LAND);
            boolean playerInAccessibleLand = playerLand.isPlayerInAccessibleLand(player, event.getTo());

            if (playerInAccessibleLand) return;
            event.setCancelled(true);
        });
    }

    @EventHandler
    public void onBlockPlace(BlockBreakEvent event) {
        factoryPlayerRegistry.get(event.getPlayer().getUniqueId()).ifPresent(factoryPlayer -> {
            Player player = factoryPlayer.getPlayer();

            String worldPlayerIsInName = player.getWorld().getName();
            String islandWorldName = FactoriesPlugin.getInstance()
                    .getConfig()
                    .getString("world.world-name");

            if (!worldPlayerIsInName.equals(islandWorldName)) return;

            SettingHolder settingHolder = factoryPlayer.getSettingHolder();

            if (settingHolder.getSetting(SettingType.PLAYER_LAND) == null) return;
            Land playerLand = settingHolder.getSetting(SettingType.PLAYER_LAND);
            boolean playerInAccessibleLand = playerLand.isPlayerInAccessibleLand(event.getPlayer(), event.getBlock().getLocation());

            if (playerInAccessibleLand) return;
            event.setCancelled(true);
            new PlayerUpgradeMenu().showToPlayer(factoryPlayer);
        });
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        factoryPlayerRegistry.get(event.getPlayer().getUniqueId()).ifPresent(factoryPlayer -> {
            Player player = factoryPlayer.getPlayer();

            String worldPlayerIsInName = player.getWorld().getName();
            String islandWorldName = FactoriesPlugin.getInstance()
                    .getConfig()
                    .getString("world.world-name");

            if (!worldPlayerIsInName.equals(islandWorldName)) return;

            SettingHolder settingHolder = factoryPlayer.getSettingHolder();

            if (settingHolder.getSetting(SettingType.PLAYER_LAND) == null) return;
            Land playerLand = settingHolder.getSetting(SettingType.PLAYER_LAND);
            boolean playerInAccessibleLand = playerLand.isPlayerInAccessibleLand(event.getPlayer(), event.getBlock().getLocation());

            if (playerInAccessibleLand) return;
            event.setCancelled(true);
        });
    }

}
