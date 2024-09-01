package dev.viaduct.factories.domain.lands;

import dev.viaduct.factories.FactoriesPlugin;
import dev.viaduct.factories.domain.players.FactoryPlayer;
import dev.viaduct.factories.settings.SettingType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;

@Getter
public class Land {

    private final Island island;
    private final Location locOfCenterOfIsland;
    private final FactoryPlayer factoryPlayer;

    private Location playerAccessLocationLowestCorner;
    private Location playerAccessLocationHighestCorner;

    public Land(FactoryPlayer factoryPlayer) {
        // Redo this
        this.island = BentoBox.getInstance()
                .getIslands()
                .getIsland(Bukkit.getWorld(FactoriesPlugin.getInstance()
                                .getConfig()
                                .getString("world.world-name")),
                        factoryPlayer.getPlayer().getUniqueId());
        this.locOfCenterOfIsland = island.getCenter();
        this.factoryPlayer = factoryPlayer;
    }

    public void setAccessibleLand() {
        int blockX = locOfCenterOfIsland.getBlockX();
        int blockY = locOfCenterOfIsland.getBlockY();
        int blockZ = locOfCenterOfIsland.getBlockZ();

        double accessibleLand = factoryPlayer.getSettingHolder()
                .getSetting(SettingType.ACCESSIBLE_LAND_BLOCKS);

        int lowestLeftX = (int) (blockX - accessibleLand);
        int lowestLeftY = (int) (blockY - accessibleLand);
        int lowestLeftZ = (int) (blockZ - accessibleLand);

        int highestRightX = (int) (blockX + accessibleLand);
        int highestRightY = (int) (blockY + accessibleLand);
        int highestRightZ = (int) (blockZ + accessibleLand);

        this.playerAccessLocationLowestCorner = new Location(island.getWorld(), lowestLeftX, lowestLeftY, lowestLeftZ);
        this.playerAccessLocationHighestCorner = new Location(island.getWorld(), highestRightX, highestRightY, highestRightZ);
    }

    public boolean isPlayerInAccessibleLand(Player player, Location location) {
        if (player.getUniqueId() != factoryPlayer.getPlayer().getUniqueId()) return true;

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return x >= playerAccessLocationLowestCorner.getX() && x <= playerAccessLocationHighestCorner.getX() &&
                        y >= playerAccessLocationLowestCorner.getY() && y <= playerAccessLocationHighestCorner.getY() &&
                        z >= playerAccessLocationLowestCorner.getZ() && z <= playerAccessLocationHighestCorner.getZ();
    }

}