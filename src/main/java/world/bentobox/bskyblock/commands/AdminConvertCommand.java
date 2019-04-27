package world.bentobox.bskyblock.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.Database;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.database.objects.Names;
import world.bentobox.bentobox.database.objects.Players;
import world.bentobox.bentobox.lists.Flags;
import world.bentobox.bentobox.managers.PlayersManager;
import world.bentobox.bentobox.util.Util;
import world.bentobox.bskyblock.BSkyBlock;
import world.bentobox.bskyblock.generators.ChunkGeneratorWorld;

public class AdminConvertCommand extends CompositeCommand {

    private Database<Players> handler;
    private Database<Names> names;

    // Owner, island
    Map<UUID, Island> islands = new HashMap<>();
    Map<UUID, List<UUID>> teamMembers = new HashMap<>();

    private YamlConfiguration config;
    private int islandDistance;
    private int islandProtectionRange;
    private int islandXOffset;
    private int islandZOffset;
    private long islandStartX;
    private long islandStartZ;
    private int islandHeight;
    private int seaHeight;
    private String worldName;
    private boolean createNether;
    private boolean newNether;
    private Object islandWorld;
    private Object netherWorld;

    public AdminConvertCommand(CompositeCommand parent) {
        super(parent, "convert");
    }

    @Override
    public void setup() {
        setPermission("admin.convert");
        setOnlyPlayer(false);
        setParametersHelp("commands.admin.convert.parameters");
        setDescription("commands.admin.convert.description");
        // Set up the database handler to store and retrieve Players classes
        handler = new Database<>(getPlugin(), Players.class);
        // Set up the names database
        names = new Database<>(getPlugin(), Names.class);
    }

    @Override
    public boolean canExecute(User user, String label, List<String> args) {
        if (args.size() > 1 || (args.isEmpty() && !user.isPlayer())) {
            // Show help
            showHelp(this, user);
            return false;
        }
        // Load ASkyBlock world
        File ASBconfig = new File(getPlugin().getDataFolder(), "../ASkyBlock/config.yml");
        if (!ASBconfig.exists()) {
            user.sendRawMessage("Cannot find ASkyBlock config.yml file!");
            return false;
        }
        config = new YamlConfiguration();
        try {
            config.load(ASBconfig);
        } catch (IOException | InvalidConfigurationException e) {
            user.sendRawMessage("Cannot load ASkyBlock config.yml file! " + e.getMessage());
            return false;
        }

        return true;
    }
    @Override
    public boolean execute(User user, String label, List<String> args) {
        try {
            getAskyBlockWorld();
        } catch (Exception e) {
            getAddon().logError("World loading error: " + e.getMessage());
            return false;
        }
        try {
            convertplayers();
        } catch (Exception e) {
            getAddon().logError("Player conversion error: " + e.getMessage());
            return false;
        }
        return true;
    }

    private <T> T nullCheck(T object) throws InvalidConfigurationException {
        if (object == null) {
            throw new InvalidConfigurationException("Expected ASkyBlock config items not found!");
        }
        return object;
    }

    private void getAskyBlockWorld() throws InvalidConfigurationException {
        worldName = nullCheck(config.getString("general.worldName"));
        createNether = nullCheck(config.getBoolean("general.createnether"));
        newNether = nullCheck(config.getBoolean("general.newnether"));
        // Open up the world
        ChunkGeneratorWorld chunkGenerator = new ChunkGeneratorWorld((BSkyBlock)getAddon());
        islandWorld = WorldCreator.name(worldName).type(WorldType.FLAT).environment(World.Environment.NORMAL).generator(chunkGenerator).createWorld();

        // Make the nether if it does not exist
        if (createNether && newNether) {
            netherWorld = WorldCreator.name(worldName + "_nether").type(WorldType.FLAT).environment(World.Environment.NORMAL).generator(chunkGenerator).createWorld();
        }

    }

    private void convertWorld() {
        // Get config items for world
        try {
            islandDistance = nullCheck(config.getInt("island.distance"));
            islandProtectionRange = nullCheck(config.getInt("island.protectionRange"));
            islandXOffset = nullCheck(config.getInt("island.xoffset"));
            islandZOffset = nullCheck(config.getInt("island.zoffset"));
            islandStartX = nullCheck(config.getLong("island.startx"));
            islandStartZ = nullCheck(config.getLong("island.startz"));
            islandHeight = nullCheck(config.getInt("island.islandlevel")) - 5;
            seaHeight = nullCheck(config.getInt("island.sealevel"));
        } catch (Exception e) {
            getAddon().logError(e.getMessage());
            return;
        }


    }

    private void convertplayers() throws InvalidConfigurationException, FileNotFoundException, IOException {
        File playerFolder = new File(getPlugin().getDataFolder(), "../ASkyBlock/players");
        if (!playerFolder.exists()) {
            throw new InvalidConfigurationException("Expected ASkyBlock player folder not found!");
        }
        FilenameFilter ymlFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith(".yml")) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        PlayersManager pm = getPlugin().getPlayers();
        YamlConfiguration player = new YamlConfiguration();
        for (File file: playerFolder.listFiles(ymlFilter)) {
            try {
                player.load(file);
                String uniqueId = file.getName().substring(0, file.getName().length() - 4);
                UUID uuid = UUID.fromString(uniqueId);
                Players p = new Players();
                if (pm.isKnown(uuid)) {
                    p = pm.getPlayer(uuid);
                } else {
                    // New player
                    p.setUniqueId(uniqueId);
                    p.setPlayerName(player.getString("playerName"));
                    names.saveObject(new Names(player.getString("playerName"), uuid));
                }
                p.getDeaths().putIfAbsent(this.getWorld().getName(), player.getInt("deaths"));
                p.setLocale(player.getString("locale"));
                handler.saveObject(p);

                // Handle island
                processIsland(uuid, player);
                // Rename file
                file.renameTo(new File(file.getParent(), file.getName() + ".done"));
            } catch (Exception e) {
                getAddon().logError("Error trying to import player file " + file.getName() + ": " + e.getMessage());
            }
        }
        processTeams();
    }

    private void processTeams() {
        teamMembers.forEach((k,v) -> {
            islands.get(k).setOwner(k);
            v.forEach(member -> islands.get(k).addMember(member));
        });
    }

    private void processIsland(UUID uuid, YamlConfiguration player) {
        boolean hasIsland = player.getBoolean("hasIsland");
        if (!hasIsland) {
            // Unless the player has an island, ignore
            return;
        }
        String islandLocation = player.getString("islandLocation");
        String teamLeader = player.getString("teamLeader");
        List<String> members = player.getStringList("members");
        String islandInfo = player.getString("islandInfo","");
        int protectionRange = 100;
        boolean isLocked = false;
        boolean isSpawn = false;
        if (!islandInfo.isEmpty()) {
            String[] split = islandInfo.split(":");
            try {
                protectionRange = Integer.parseInt(split[3]);
                isLocked = split[6].equalsIgnoreCase("true");
                isSpawn = split[5].equals("spawn");
            } catch (Exception e) {
                getAddon().logError("Problem parsing island settings");
            }
        }
        // Deal with owners or team leaders
        Island island = new Island();
        island.setUniqueId(getWorld().getName() + "_i_" + UUID.randomUUID().toString());
        island.setOwner(teamLeader.isEmpty() ? uuid : UUID.fromString(teamLeader));
        island.setProtectionRange(protectionRange * 2);
        island.setSpawn(isSpawn);
        island.setCenter(Util.getLocationString(islandLocation));
        island.setSettingsFlag(Flags.LOCK, isLocked);
        members.stream().map(UUID::fromString).forEach(island::addMember);
        island.setCreatedDate(System.currentTimeMillis());
        island.setUpdatedDate(System.currentTimeMillis());
        islands.put(uuid, island);
    }
}
