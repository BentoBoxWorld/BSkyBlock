package dev.viaduct.factories.domain.lands;

import dev.viaduct.factories.FactoriesPlugin;
import dev.viaduct.factories.domain.players.FactoryPlayer;
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

    private int level = 0;

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
        setAccessibleLand();
    }

    public void setAccessibleLand() {
        int blockX = locOfCenterOfIsland.getBlockX();
        int blockY = locOfCenterOfIsland.getBlockY();
        int blockZ = locOfCenterOfIsland.getBlockZ();

        int accessibleLand = getSizeInBlocksForLevel(level);

        int lowestLeftX = blockX - accessibleLand;
        int lowestLeftY = blockY - accessibleLand;
        int lowestLeftZ = blockZ - accessibleLand;

        int highestRightX = blockX + accessibleLand;
        int highestRightY = blockY + accessibleLand;
        int highestRightZ = blockZ + accessibleLand;

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

    public void setLevel(int level) {
        this.level = level;
        setAccessibleLand();
    }

    private int getSizeInBlocksForLevel(int level) {
        return switch (level) {
            case 1 -> 20;
            case 2 -> 30;
            case 3 -> 40;
            case 4 -> 50;
            case 5 -> 60;
            default -> 10;
        };
    }

}