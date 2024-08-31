package dev.viaduct.factories.domain.lands;

import dev.viaduct.factories.domain.players.FactoryPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;

public class Land {

    private final Island island;
    private final Location locOfCenterOfIsland;

    public Land(FactoryPlayer factoryPlayer) {
        this.island = BentoBox.getInstance()
                .getIslands()
                .getIsland(Bukkit.getWorld("bskyblock_world"),
                        factoryPlayer.getPlayer().getUniqueId());
        this.locOfCenterOfIsland = island.getCenter();
    }

    // Todo: Use player accessible land size instead of hardcoding 3
    public void setAccessibleLand() {
        int blockX = locOfCenterOfIsland.getBlockX();
        int blockY = locOfCenterOfIsland.getBlockY();
        int blockZ = locOfCenterOfIsland.getBlockZ();

        int lowestLeftX = blockX - 3;
        int lowestLeftY = blockY - 3;
        int lowestLeftZ = blockZ - 3;
        int highestRightX = blockX + 3;
        int highestRightY = blockY + 3;
        int highestRightZ = blockZ + 3;

        island.getWorld().getBlockAt(lowestLeftX, lowestLeftY, lowestLeftZ).setType(Material.REDSTONE_BLOCK);
        island.getWorld().getBlockAt(highestRightX, highestRightY, highestRightZ).setType(Material.REDSTONE_BLOCK);
    }


}