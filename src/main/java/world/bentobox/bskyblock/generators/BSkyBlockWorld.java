package world.bentobox.bskyblock.generators;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import world.bentobox.bskyblock.BSkyBlock;
import world.bentobox.bentobox.util.Util;

/**
 * Creates the worlds and registers them with BentoBox
 * @author tastybento
 *
 */
public class BSkyBlockWorld {

    private static final String NETHER = "_nether";
    private static final String THE_END = "_the_end";
    private World islandWorld;
    private World netherWorld;
    private World endWorld;

    /**
     * Create and register worlds with BentoBox
     * @param addon - addon
     */
    public BSkyBlockWorld(BSkyBlock addon) {
        String worldName = addon.getSettings().getWorldName();
        if (addon.getServer().getWorld(worldName) == null) {
            addon.getLogger().info("Creating BSkyBlock worlds...");
        }
        // Create the world if it does not exist
        islandWorld = WorldCreator.name(worldName).type(WorldType.FLAT).environment(World.Environment.NORMAL).generator(new ChunkGeneratorWorld(addon))
                .createWorld();
        addon.getPlugin().registerWorld(islandWorld, addon.getSettings());

        // Make the nether if it does not exist
        if (addon.getSettings().isNetherGenerate()) {
            if (addon.getServer().getWorld(worldName + NETHER) == null) {
                addon.log("Creating BSkyBlock's Nether...");
            }
            if (!addon.getSettings().isNetherIslands()) {
                netherWorld = WorldCreator.name(worldName + NETHER).type(WorldType.NORMAL).environment(World.Environment.NETHER).createWorld();
            } else {
                netherWorld = WorldCreator.name(worldName + NETHER).type(WorldType.FLAT).generator(new ChunkGeneratorWorld(addon))
                        .environment(World.Environment.NETHER).createWorld();
            }
        }
        // Make the end if it does not exist
        if (addon.getSettings().isEndGenerate()) {
            if (addon.getServer().getWorld(worldName + THE_END) == null) {
                addon.log("Creating BSkyBlock's End World...");
            }
            if (!addon.getSettings().isEndIslands()) {
                endWorld = WorldCreator.name(worldName + THE_END).type(WorldType.NORMAL).environment(World.Environment.THE_END).createWorld();
            } else {
                endWorld = WorldCreator.name(worldName + THE_END).type(WorldType.FLAT).generator(new ChunkGeneratorWorld(addon))
                        .environment(World.Environment.THE_END).createWorld();
            }
        }

        // Load schematics
        addon.getPlugin().getSchemsManager().loadIslands(islandWorld);

    }


    /**
     * Checks if a player is in any of the island worlds
     * @param loc - player to check
     * @return true if in a world or false if not
     */
    public boolean inWorld(Location loc) {
        return Util.sameWorld(loc.getWorld(), islandWorld);
    }

    public World getOverWorld() {
        return islandWorld;
    }

    public World getNetherWorld() {
        return netherWorld;
    }

    public World getEndWorld() {
        return endWorld;
    }

}
