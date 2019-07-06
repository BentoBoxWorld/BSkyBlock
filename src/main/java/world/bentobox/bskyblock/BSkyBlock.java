package world.bentobox.bskyblock;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;
import org.eclipse.jdt.annotation.Nullable;

import world.bentobox.bentobox.api.addons.GameModeAddon;
import world.bentobox.bentobox.api.configuration.Config;
import world.bentobox.bentobox.api.configuration.WorldSettings;
import world.bentobox.bskyblock.commands.AdminCommand;
import world.bentobox.bskyblock.commands.IslandCommand;
import world.bentobox.bskyblock.generators.ChunkGeneratorWorld;

/**
 * Main BSkyBlock class - provides an island minigame in the sky
 * @author tastybento
 * @author Poslovitch
 */
public class BSkyBlock extends GameModeAddon {

    private static final String NETHER = "_nether";
    private static final String THE_END = "_the_end";

    // Settings
    private Settings settings;
    private ChunkGeneratorWorld chunkGenerator;

    @Override
    public void onLoad() {
        // Save the default config from config.yml
        saveDefaultConfig();
        // Load settings from config.yml. This will check if there are any issues with it too.
        loadSettings();
        // Chunk generator
        chunkGenerator = settings.isUseOwnGenerator() ? null : new ChunkGeneratorWorld(this);
        // Register commands
        playerCommand = new IslandCommand(this);
        adminCommand = new AdminCommand(this);
    }

    private void loadSettings() {
        // Load settings again to get worlds
        settings = new Config<>(this, Settings.class).loadConfigObject();
        if (settings == null) {
            // Disable
            logError("BSkyBlock settings could not load! Addon disabled.");
            setState(State.DISABLED);
            return;
        }
        this.saveWorldSettings();

    }

    @Override
    public void onEnable(){
        // Nothing to do here
    }

    @Override
    public void onDisable() {
        // Nothing to do here
    }

    @Override
    public void onReload() {
        loadSettings();
    }

    /**
     * @return the settings
     */
    public Settings getSettings() {
        return settings;
    }

    @Override
    public void createWorlds() {
        String worldName = settings.getWorldName().toLowerCase();
        if (getServer().getWorld(worldName) == null) {
            log("Creating BSkyBlock world ...");
        }

        // Create the world if it does not exist
        islandWorld = getWorld(worldName, World.Environment.NORMAL, chunkGenerator);
        Bukkit.getLogger().info("World spawn limits ambient " + islandWorld.getAmbientSpawnLimit());
        //islandWorld.setAnimalSpawnLimit(100);
        //islandWorld.setAmbientSpawnLimit(100);
        Bukkit.getLogger().info("World spawn limits animal " + islandWorld.getAnimalSpawnLimit());
        Bukkit.getLogger().info("World spawn limits monster " + islandWorld.getMonsterSpawnLimit());
        Bukkit.getLogger().info("World spawn limits water " + islandWorld.getWaterAnimalSpawnLimit());
        //islandWorld.setTicksPerAnimalSpawns(1);
        Bukkit.getLogger().info("World spawn limits ticks per animal " + islandWorld.getTicksPerAnimalSpawns());
        Bukkit.getLogger().info("World spawn limits ticks per monster " + islandWorld.getTicksPerMonsterSpawns());

        // Make the nether if it does not exist
        if (settings.isNetherGenerate()) {
            if (getServer().getWorld(worldName + NETHER) == null) {
                log("Creating BSkyBlock's Nether...");
            }
            netherWorld = settings.isNetherIslands() ? getWorld(worldName, World.Environment.NETHER, chunkGenerator) : getWorld(worldName, World.Environment.NETHER, null);
        }
        // Make the end if it does not exist
        if (settings.isEndGenerate()) {
            if (getServer().getWorld(worldName + THE_END) == null) {
                log("Creating BSkyBlock's End World...");
            }
            endWorld = settings.isEndIslands() ? getWorld(worldName, World.Environment.THE_END, chunkGenerator) : getWorld(worldName, World.Environment.THE_END, null);
        }
    }

    /**
     * Gets a world or generates a new world if it does not exist
     * @param worldName2 - the overworld name
     * @param env - the environment
     * @param chunkGenerator2 - the chunk generator. If <tt>null</tt> then the generator will not be specified
     * @return world loaded or generated
     */
    private World getWorld(String worldName2, Environment env, ChunkGeneratorWorld chunkGenerator2) {
        // Set world name
        worldName2 = env.equals(World.Environment.NETHER) ? worldName2 + NETHER : worldName2;
        worldName2 = env.equals(World.Environment.THE_END) ? worldName2 + THE_END : worldName2;
        WorldCreator wc = WorldCreator.name(worldName2).type(WorldType.FLAT).environment(env);
        return settings.isUseOwnGenerator() ? wc.createWorld() : wc.generator(chunkGenerator2).createWorld();
    }

    @Override
    public WorldSettings getWorldSettings() {
        return getSettings();
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return chunkGenerator;
    }

    @Override
    public void saveWorldSettings() {
        if (settings != null) {
            new Config<>(this, Settings.class).saveConfigObject(settings);
        }
    }
}
