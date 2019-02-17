package world.bentobox.bskyblock;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;
import org.eclipse.jdt.annotation.NonNull;

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
    }

    private void loadSettings() {
        settings = new Config<>(this, Settings.class).loadConfigObject();
        if (settings == null) {
            // Disable
            logError("BSkyBlock settings could not load! Addon disabled.");
            setState(State.DISABLED);
        }
        new Config<>(this, Settings.class).saveConfigObject(settings);
    }

    @Override
    public void onEnable(){
        // Register commands
        playerCommand = new IslandCommand(this);
        adminCommand = new AdminCommand(this);
    }

    @Override
    public void onDisable() {
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
        String worldName = settings.getWorldName();
        if (getServer().getWorld(worldName) == null) {
            getLogger().info("Creating BSkyBlock world ...");
        }
        chunkGenerator = new ChunkGeneratorWorld(this);
        // Create the world if it does not exist
        islandWorld = WorldCreator.name(worldName).type(WorldType.FLAT).environment(World.Environment.NORMAL).generator(chunkGenerator)
                .createWorld();

        // Make the nether if it does not exist
        if (settings.isNetherGenerate()) {
            if (getServer().getWorld(worldName + NETHER) == null) {
                log("Creating BSkyBlock's Nether...");
            }
            if (!settings.isNetherIslands()) {
                netherWorld = WorldCreator.name(worldName + NETHER).type(WorldType.NORMAL).environment(World.Environment.NETHER).createWorld();
            } else {
                netherWorld = WorldCreator.name(worldName + NETHER).type(WorldType.FLAT).generator(chunkGenerator)
                        .environment(World.Environment.NETHER).createWorld();
            }
        }
        // Make the end if it does not exist
        if (settings.isEndGenerate()) {
            if (getServer().getWorld(worldName + THE_END) == null) {
                log("Creating BSkyBlock's End World...");
            }
            if (!settings.isEndIslands()) {
                endWorld = WorldCreator.name(worldName + THE_END).type(WorldType.NORMAL).environment(World.Environment.THE_END).createWorld();
            } else {
                endWorld = WorldCreator.name(worldName + THE_END).type(WorldType.FLAT).generator(chunkGenerator)
                        .environment(World.Environment.THE_END).createWorld();
            }
        }
    }

    @Override
    public WorldSettings getWorldSettings() {
        return settings;
    }

    @Override
    public @NonNull ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return chunkGenerator;
    }
}
