package dev.viaduct.factories;

import dev.viaduct.factories.generators.ChunkGeneratorWorld;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.eclipse.jdt.annotation.Nullable;
import world.bentobox.bentobox.api.addons.GameModeAddon;
import world.bentobox.bentobox.api.commands.admin.DefaultAdminCommand;
import world.bentobox.bentobox.api.commands.island.DefaultPlayerCommand;
import world.bentobox.bentobox.api.configuration.Config;
import world.bentobox.bentobox.api.configuration.WorldSettings;

/**
 * Main BSkyBlock class - provides an island minigame in the sky
 *
 * @author tastybento
 * @author Poslovitch
 * @author xxAli
 * @author oshwab
 */
public class Factories extends GameModeAddon implements Listener {

    private static final String NETHER = "_nether";
    private static final String THE_END = "_the_end";

    // Settings
    private Settings settings;
    private ChunkGeneratorWorld chunkGenerator;
    private final Config<Settings> configObject = new Config<>(this, Settings.class);

    @Override
    public void onLoad() {
        // Save the default config from config.yml
        saveDefaultConfig();
        // Load settings from config.yml. This will check if there are any issues with it too.
        loadSettings();
        // Chunk generator
        chunkGenerator = settings.isUseOwnGenerator() ? null : new ChunkGeneratorWorld(this);
        // Register commands
        playerCommand = new DefaultPlayerCommand(this) {
        };
        adminCommand = new DefaultAdminCommand(this) {
        };
    }

    private boolean loadSettings() {
        // Load settings again to get worlds
        settings = configObject.loadConfigObject();
        if (settings == null) {
            // Disable
            logError("BSkyBlock settings could not load! Addon disabled.");
            setState(State.DISABLED);
            return false;
        }
        return true;
    }

    @Override
    public void onEnable() {
        // Register this
        registerListener(this);
    }

    @Override
    public void onDisable() {
        // Nothing to do here
    }

    @Override
    public void onReload() {
        if (loadSettings()) {
            log("Reloaded BSkyBlock settings");
        }
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
     *
     * @param worldName2      - the overworld name
     * @param env             - the environment
     * @param chunkGenerator2 - the chunk generator. If <tt>null</tt> then the generator will not be specified
     * @return world loaded or generated
     */
    private World getWorld(String worldName2, Environment env, ChunkGeneratorWorld chunkGenerator2) {
        // Set world name
        worldName2 = env.equals(World.Environment.NETHER) ? worldName2 + NETHER : worldName2;
        worldName2 = env.equals(World.Environment.THE_END) ? worldName2 + THE_END : worldName2;
        WorldCreator wc = WorldCreator.name(worldName2).type(WorldType.FLAT).environment(env);
        World w = settings.isUseOwnGenerator() ? wc.createWorld() : wc.generator(chunkGenerator2).createWorld();
        // Set spawn rates
        // Set spawn rates
        if (w != null && getSettings() != null) {
            if (getSettings().getSpawnLimitMonsters() > 0) {
                w.setSpawnLimit(SpawnCategory.MONSTER, getSettings().getSpawnLimitMonsters());
            }
            if (getSettings().getSpawnLimitAmbient() > 0) {
                w.setSpawnLimit(SpawnCategory.AMBIENT, getSettings().getSpawnLimitAmbient());
            }
            if (getSettings().getSpawnLimitAnimals() > 0) {
                w.setSpawnLimit(SpawnCategory.ANIMAL, getSettings().getSpawnLimitAnimals());
            }
            if (getSettings().getSpawnLimitWaterAnimals() > 0) {
                w.setSpawnLimit(SpawnCategory.WATER_ANIMAL, getSettings().getSpawnLimitWaterAnimals());
            }
            if (getSettings().getTicksPerAnimalSpawns() > 0) {
                w.setTicksPerSpawns(SpawnCategory.ANIMAL, getSettings().getTicksPerAnimalSpawns());
            }
            if (getSettings().getTicksPerMonsterSpawns() > 0) {
                w.setTicksPerSpawns(SpawnCategory.MONSTER, getSettings().getTicksPerMonsterSpawns());
            }
        }
        return w;

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
            configObject.saveConfigObject(settings);
        }
    }

    /* (non-Javadoc)
     * @see world.bentobox.bentobox.api.addons.Addon#allLoaded()
     */
    @Override
    public void allLoaded() {
        // Save settings. This will occur after all addons have loaded
        this.saveWorldSettings();
    }

}
