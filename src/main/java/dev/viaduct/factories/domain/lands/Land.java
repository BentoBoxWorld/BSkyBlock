package dev.viaduct.factories.domain.lands;

import dev.viaduct.factories.FactoriesPlugin;
import dev.viaduct.factories.domain.players.FactoryPlayer;
import dev.viaduct.factories.exceptions.MaxLevelReachedException;
import dev.viaduct.factories.upgrades.Upgrade;
import dev.viaduct.factories.upgrades.UpgradeManager;
import dev.viaduct.factories.upgrades.impl.LevelledUpgrade;
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
        setAccessibleLand();
    }

    public void setAccessibleLand() {
        int blockX = locOfCenterOfIsland.getBlockX();
        int blockY = locOfCenterOfIsland.getBlockY();
        int blockZ = locOfCenterOfIsland.getBlockZ();

        int accessibleLand = 0;
        try {
            accessibleLand = getSizeInBlocks();
        } catch (MaxLevelReachedException e) {
            e.printStackTrace();
        }

        int lowestLeftX = blockX - accessibleLand;
        int lowestLeftY = blockY - accessibleLand;
        int lowestLeftZ = blockZ - accessibleLand;

        int highestRightX = blockX + accessibleLand;
        int highestRightY = blockY + accessibleLand;
        int highestRightZ = blockZ + accessibleLand;

        this.playerAccessLocationLowestCorner = new Location(island.getWorld(), lowestLeftX, lowestLeftY, lowestLeftZ);
        this.playerAccessLocationHighestCorner = new Location(island.getWorld(), highestRightX, highestRightY, highestRightZ);
    }

    private int getSizeInBlocks() throws MaxLevelReachedException {
        Upgrade upgrade = FactoriesPlugin.getInstance()
                .getUpgradeManager()
                .getUpgrade(UpgradeManager.UpgradeName.LAND_SIZE_UPGRADE);
        int landSizeUpgradeLevel = factoryPlayer.getLevelledUpgradeHolder()
                .getUpgradeLevel(UpgradeManager.UpgradeName.LAND_SIZE_UPGRADE);

        if (landSizeUpgradeLevel == 0) {
            return upgrade.getBaseValue();
        }
        return ((LevelledUpgrade<Integer>) upgrade).getDataForLevel(landSizeUpgradeLevel).getValue();
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