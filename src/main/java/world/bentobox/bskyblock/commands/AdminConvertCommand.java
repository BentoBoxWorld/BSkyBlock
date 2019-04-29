package world.bentobox.bskyblock.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
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
import world.bentobox.bentobox.managers.RanksManager;
import world.bentobox.bentobox.util.Util;
import world.bentobox.bskyblock.BSkyBlock;
import world.bentobox.bskyblock.Settings;
import world.bentobox.bskyblock.generators.ChunkGeneratorWorld;

public class AdminConvertCommand extends CompositeCommand {

    private Database<Players> handler;
    private Database<Names> names;

    private BSkyBlock gm;

    // Owner, island
    Map<UUID, Island> islands = new HashMap<>();
    Map<UUID, List<UUID>> teamMembers = new HashMap<>();

    private YamlConfiguration config;
    private int islandDistance;
    private int islandProtectionRange;
    private int islandXOffset;
    private int islandZOffset;
    private int islandStartX;
    private int islandStartZ;
    private int islandHeight;
    private int seaHeight;
    private String worldName;
    private boolean createNether;
    private boolean newNether;

    public AdminConvertCommand(CompositeCommand parent) {
        super(parent, "convert");
        this.gm = (BSkyBlock)getAddon();
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
        if (!args.isEmpty()) {
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
        user.sendRawMessage("Getting the worlds");
        try {
            getConfigs();
        } catch (InvalidConfigurationException e) {
            getAddon().logError("Config loading error: " + e.getMessage());
            return false;
        }
        try {
            getAskyBlockWorld();
        } catch (Exception e) {
            getAddon().logError("World loading error: " + e.getMessage());
            return false;
        }
        user.sendRawMessage("Converting players");
        try {
            convertplayers(user);
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
        WorldCreator.name(worldName).type(WorldType.FLAT).environment(World.Environment.NORMAL).generator(chunkGenerator).createWorld();

        // Get nether
        if (createNether && newNether) {
            WorldCreator.name(worldName + "_nether").type(WorldType.FLAT).environment(World.Environment.NORMAL).generator(chunkGenerator).createWorld();
        }

    }

    private void getConfigs() throws InvalidConfigurationException {
        // Get config items for world
        islandDistance = nullCheck(config.getInt("island.distance"));
        islandProtectionRange = nullCheck(config.getInt("island.protectionRange"));
        islandXOffset = nullCheck(config.getInt("island.xoffset"));
        islandZOffset = nullCheck(config.getInt("island.zoffset"));
        islandStartX = nullCheck(config.getInt("island.startx"));
        islandStartZ = nullCheck(config.getInt("island.startz"));
        islandHeight = nullCheck(config.getInt("island.islandlevel")) - 5;
        seaHeight = nullCheck(config.getInt("island.sealevel"));
        Settings s = gm.getSettings();
        s.setIslandXOffset(islandXOffset);
        s.setIslandZOffset(islandZOffset);
        s.setIslandDistance(islandDistance / 2);
        s.setIslandProtectionRange(islandProtectionRange / 2);
        s.setIslandStartX(islandStartX);
        s.setIslandStartZ(islandStartZ);
        s.setSeaHeight(seaHeight != 0 ? seaHeight - 1 : 0);
        s.setIslandHeight(islandHeight);
        gm.saveWorldSettings();

    }

    private void convertplayers(User user) throws InvalidConfigurationException, FileNotFoundException, IOException {
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
        List<File> playerFiles = Arrays.asList(playerFolder.listFiles(ymlFilter));
        user.sendRawMessage("There are " + playerFiles.size() + " player files to process");
        int count = 1;
        for (File file: playerFiles) {
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
                user.sendRawMessage("Saved " + (count++) + " players");
                // Handle island
                processIsland(user, uuid, player);
                // Rename file
                file.renameTo(new File(file.getParent(), file.getName() + ".done"));
            } catch (Exception e) {
                getAddon().logError("Error trying to import player file " + file.getName() + ": " + e.getMessage());
            }
        }
        user.sendRawMessage("Processing teams");
        processTeams();
        user.sendRawMessage("Storing islands");
        islands.values().forEach(i -> getIslands().getIslandCache().addIsland(i));
        user.sendRawMessage("done");
    }

    private void processTeams() {
        teamMembers.forEach((k,v) -> {
            islands.get(k).setOwner(k);
            v.forEach(member -> islands.get(k).addMember(member));
        });
    }

    private void processIsland(User user, UUID uuid, YamlConfiguration player) {
        user.sendRawMessage("Processing island");
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
        island.setProtectionRange(protectionRange / 2);
        island.setSpawn(isSpawn);
        island.setCenter(Util.getLocationString(islandLocation));
        island.setSettingsFlag(Flags.LOCK, isLocked);
        members.stream().map(UUID::fromString).forEach(island::addMember);
        island.setRank(island.getOwner(), RanksManager.OWNER_RANK);
        island.setCreatedDate(System.currentTimeMillis());
        island.setUpdatedDate(System.currentTimeMillis());
        island.setGameMode(getAddon().getDescription().getName());
        island.setRange(this.islandDistance / 2 );
        island.setGameMode(gm.getDescription().getName());
        islands.put(uuid, island);
    }
}
