package bentobox.addon.bskyblock;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;

import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.configuration.ConfigComment;
import world.bentobox.bentobox.api.configuration.ConfigEntry;
import world.bentobox.bentobox.api.configuration.StoreAt;
import world.bentobox.bentobox.api.configuration.WorldSettings;
import world.bentobox.bentobox.api.flags.Flag;
import world.bentobox.bentobox.database.objects.DataObject;
import world.bentobox.bentobox.database.objects.adapters.Adapter;
import world.bentobox.bentobox.database.objects.adapters.FlagSerializer;
import world.bentobox.bentobox.database.objects.adapters.FlagSerializer2;

/**
 * All the plugin settings are here
 * @author Tastybento
 */
@StoreAt(filename="config.yml") // Explicitly call out what name this should have.
@ConfigComment("BSkyBlock Configuration [version]")
@ConfigComment("This config file is dynamic and saved when the server is shutdown.")
@ConfigComment("You cannot edit it while the server is running because changes will")
@ConfigComment("be lost! Use in-game settings GUI or edit when server is offline.")
@ConfigComment("")
public class Settings implements DataObject, WorldSettings {

    @ConfigComment("Use economy or not. If true, an economy plugin is required. If false, no money is used or give.")
    @ConfigComment("If there is no economy plugin present anyway, money will be automatically disabled.")
    @ConfigEntry(path = "general.use-economy")
    private boolean useEconomy = true;

    @ConfigComment("Starting money - this is how much money new players will have as their")
    @ConfigComment("balance at the start of an island.")
    @ConfigEntry(path = "general.starting-money")
    private double startingMoney = 10.0;

    @ConfigComment("Allow obsidian to be scooped up with an empty bucket back into lava")
    @ConfigComment("This only works if there is a single block of obsidian (no obsidian within 10 blocks)")
    @ConfigComment("Recommendation is to keep this true so that newbies don't bother you or reset their")
    @ConfigComment("island unnecessarily.")
    @ConfigEntry(path = "general.allow-obsidian-scooping")
    private boolean allowObsidianScooping = true;

    @ConfigComment("Time in seconds that players have to confirm sensitive commands, e.g. island reset")
    @ConfigEntry(path = "general.confirmation-time")
    private int confirmationTime = 20;

    // ---------------------------------------------

    /*      WORLD       */
    @ConfigComment("Friendly name for this world. Used in admin commands. Must be a single word")
    @ConfigEntry(path = "world.friendly-name", needsReset = true)
    private String friendlyName = "BSkyBlock";

    @ConfigComment("Name of the world - if it does not exist then it will be generated.")
    @ConfigComment("It acts like a prefix for nether and end (e.g. BSkyBlock, BSkyBlock_nether, BSkyBlock_end)")
    @ConfigEntry(path = "world.world-name", needsReset = true)
    private String worldName = "BSkyBlock-world";

    @ConfigComment("World difficulty setting - PEACEFUL, EASY, NORMAL, HARD")
    @ConfigComment("Other plugins may override this setting")
    @ConfigEntry(path = "world.difficulty")
    private Difficulty difficulty = Difficulty.NORMAL;

    @ConfigComment("Radius of island in blocks. (So distance between islands is twice this)")
    @ConfigComment("Will be rounded up to the nearest 16 blocks.")
    @ConfigComment("It is the same for every dimension : Overworld, Nether and End.")
    @ConfigComment("This value cannot be changed mid-game and the plugin will not start if it is different.")
    @ConfigEntry(path = "world.distance-between-islands")
    private int islandDistance = 192;

    @ConfigComment("Default protection range radius in blocks. Cannot be larger than distance.")
    @ConfigComment("Admins can change protection sizes for players individually using /bsadmin setrange")
    @ConfigComment("or set this permission: bskyblock.island.range.<number>")
    @ConfigEntry(path = "world.protection-range")
    private int islandProtectionRange = 100;

    @ConfigComment("Start islands at these coordinates. This is where new islands will start in the")
    @ConfigComment("world. These must be a factor of your island distance, but the plugin will auto")
    @ConfigComment("calculate the closest location on the grid. Islands develop around this location")
    @ConfigComment("both positively and negatively in a square grid.")
    @ConfigComment("If none of this makes sense, leave it at 0,0.")
    @ConfigEntry(path = "world.start-x", needsReset = true)
    private int islandStartX = 0;

    @ConfigEntry(path = "world.start-z", needsReset = true)
    private int islandStartZ = 0;

    @ConfigEntry(path = "world.offset-x")
    private int islandXOffset;
    @ConfigEntry(path = "world.offset-z")
    private int islandZOffset;

    @ConfigComment("Island height - Lowest is 5.")
    @ConfigComment("It is the y coordinate of the bedrock block in the schem")
    @ConfigEntry(path = "world.island-height")
    private int islandHeight = 100;

    @ConfigComment("Use your own world generator for this world. In this case, the plugin will not generate")
    @ConfigComment("anything.")
    @ConfigEntry(path = "world.use-own-generator")
    private boolean useOwnGenerator;

    @ConfigComment("Sea height (don't changes this mid-game unless you delete the world)")
    @ConfigComment("Minimum is 0, which means you are playing Skyblock!")
    @ConfigComment("If sea height is less than about 10, then players will drop right through it")
    @ConfigComment("if it exists. Makes for an interesting variation on skyblock.")
    @ConfigEntry(path = "world.sea-height")
    private int seaHeight = 0;

    @ConfigComment("Maximum number of islands in the world. Set to 0 for unlimited. ")
    @ConfigComment("If the number of islands is greater than this number, no new island will be created.")
    @ConfigEntry(path = "world.max-islands")
    private int maxIslands = -1;

    @ConfigComment("The default game mode for this world. Players will be set to this mode when they create")
    @ConfigComment("a new island for example. Options are SURVIVAL, CREATIVE, ADVENTURE, SPECTATOR")
    @ConfigEntry(path = "world.default-game-mode")
    private GameMode defaultGameMode = GameMode.SURVIVAL;

    // Nether
    @ConfigComment("Generate Nether - if this is false, the nether world will not be made and access to")
    @ConfigComment("the nether will not occur. Other plugins may still enable portal usage.")
    @ConfigComment("Note: Some challenges will not be possible if there is no nether.")
    @ConfigComment("Note that with a standard nether all players arrive at the same portal and entering a")
    @ConfigComment("portal will return them back to their islands.")
    @ConfigEntry(path = "world.nether.generate")
    private boolean netherGenerate = true;

    @ConfigComment("Islands in Nether. Change to false for standard vanilla nether.")
    @ConfigEntry(path = "world.nether.islands", needsReset = true)
    private boolean netherIslands = true;

    @ConfigComment("Nether trees are made if a player grows a tree in the nether (gravel and glowstone)")
    @ConfigComment("Applies to both vanilla and islands Nether")
    @ConfigEntry(path = "world.nether.trees")
    private boolean netherTrees = true;

    @ConfigComment("Make the nether roof, if false, there is nothing up there")
    @ConfigComment("Change to false if lag is a problem from the generation")
    @ConfigComment("Only applies to islands Nether")
    @ConfigEntry(path = "world.nether.roof")
    private boolean netherRoof = true;

    @ConfigComment("Nether spawn protection radius - this is the distance around the nether spawn")
    @ConfigComment("that will be protected from player interaction (breaking blocks, pouring lava etc.)")
    @ConfigComment("Minimum is 0 (not recommended), maximum is 100. Default is 25.")
    @ConfigComment("Only applies to vanilla nether")
    @ConfigEntry(path = "world.nether.spawn-radius")
    private int netherSpawnRadius = 32;

    // End
    @ConfigEntry(path = "world.end.generate")
    private boolean endGenerate = true;

    @ConfigEntry(path = "world.end.islands", needsReset = true)
    private boolean endIslands = true;

    @ConfigEntry(path = "world.end.dragon-spawn")
    private boolean dragonSpawn = false;

    @ConfigComment("Mob white list - these mobs will NOT be removed when logging in or doing /island")
    @ConfigEntry(path = "world.remove-mobs-whitelist")
    private Set<EntityType> removeMobsWhitelist = new HashSet<>();

    @ConfigComment("World flags. These are boolean settings for various flags for this world")
    @ConfigEntry(path = "world.flags")
    private Map<String, Boolean> worldFlags = new HashMap<>();

    @ConfigComment("These are the default protection settings for new islands.")
    @ConfigComment("The value is the minimum island rank required allowed to do the action")
    @ConfigComment("Ranks are: Visitor = 0, Member = 900, Owner = 1000")
    @ConfigEntry(path = "world.default-island-flags")
    @Adapter(FlagSerializer.class)
    private Map<Flag, Integer> defaultIslandFlags = new HashMap<>();

    @ConfigComment("These are the default settings for new islands")
    @ConfigEntry(path = "world.default-island-settings")
    @Adapter(FlagSerializer2.class)
    private Map<Flag, Integer> defaultIslandSettings = new HashMap<>();

    @ConfigComment("These are the settings visible to users.")
    @ConfigEntry(path = "world.visible-settings")
    private List<String> visibleSettings = new ArrayList<>();

    @ConfigComment("Visitor banned commands - Visitors to islands cannot use these commands in this world")
    @ConfigEntry(path = "world.visitor-banned-commands")
    private List<String> visitorBannedCommands = new ArrayList<>();

    // ---------------------------------------------

    /*      ISLAND      */
    // Entities
    @ConfigEntry(path = "island.limits.entities")
    private Map<EntityType, Integer> entityLimits = new EnumMap<>(EntityType.class);
    @ConfigEntry(path = "island.limits.tile-entities")
    private Map<String, Integer> tileEntityLimits = new HashMap<>();

    @ConfigComment("Default max team size")
    @ConfigComment("Use this permission to set for specific user groups: askyblock.team.maxsize.<number>")
    @ConfigComment("Permission size cannot be less than the default below. ")
    @ConfigEntry(path = "island.max-team-size")
    private int maxTeamSize = 4;
    @ConfigComment("Default maximum number of homes a player can have. Min = 1")
    @ConfigComment("Accessed via sethome <number> or go <number>")
    @ConfigComment("Use this permission to set for specific user groups: askyblock.island.maxhomes.<number>")
    @ConfigEntry(path = "island.max-homes")
    private int maxHomes = 5;

    // Reset
    @ConfigComment("How many resets a player is allowed (override with /asadmin clearreset <player>)")
    @ConfigComment("Value of -1 means unlimited, 0 means hardcore - no resets.")
    @ConfigComment("Example, 2 resets means they get 2 resets or 3 islands lifetime")
    @ConfigEntry(path = "island.reset.reset-limit")
    private int resetLimit = -1;

    @ConfigEntry(path = "island.require-confirmation.reset")
    private boolean resetConfirmation = true;

    @ConfigComment("Kicked or leaving players lose resets")
    @ConfigComment("Players who leave a team will lose an island reset chance")
    @ConfigComment("If a player has zero resets left and leaves a team, they cannot make a new")
    @ConfigComment("island by themselves and can only join a team.")
    @ConfigComment("Leave this true to avoid players exploiting free islands")
    @ConfigEntry(path = "island.reset.leavers-lose-reset")
    private boolean leaversLoseReset = false;

    @ConfigComment("Allow kicked players to keep their inventory.")
    @ConfigComment("If false, kicked player's inventory will be thrown at the island leader if the")
    @ConfigComment("kicked player is online and in the island world.")
    @ConfigEntry(path = "island.reset.kicked-keep-inventory")
    private boolean kickedKeepInventory = false;

    @ConfigComment("What the plugin should reset when the player joins or creates an island")
    @ConfigComment("Reset Money - if this is true, will reset the player's money to the starting money")
    @ConfigComment("Recommendation is that this is set to true, but if you run multi-worlds")
    @ConfigComment("make sure your economy handles multi-worlds too.")
    @ConfigEntry(path = "island.reset.on-join.money")
    private boolean onJoinResetMoney = false;

    @ConfigComment("Reset inventory - if true, the player's inventory will be cleared.")
    @ConfigComment("Note: if you have MultiInv running or a similar inventory control plugin, that")
    @ConfigComment("plugin may still reset the inventory when the world changes.")
    @ConfigEntry(path = "island.reset.on-join.inventory")
    private boolean onJoinResetInventory = false;

    @ConfigComment("Reset Ender Chest - if true, the player's Ender Chest will be cleared.")
    @ConfigEntry(path = "island.reset.on-join.ender-chest")
    private boolean onJoinResetEnderChest = false;

    @ConfigComment("What the plugin should reset when the player leaves or is kicked from an island")
    @ConfigComment("Reset Money - if this is true, will reset the player's money to the starting money")
    @ConfigComment("Recommendation is that this is set to true, but if you run multi-worlds")
    @ConfigComment("make sure your economy handles multi-worlds too.")
    @ConfigEntry(path = "island.reset.on-leave.money")
    private boolean onLeaveResetMoney = false;

    @ConfigComment("Reset inventory - if true, the player's inventory will be cleared.")
    @ConfigComment("Note: if you have MultiInv running or a similar inventory control plugin, that")
    @ConfigComment("plugin may still reset the inventory when the world changes.")
    @ConfigEntry(path = "island.reset.on-leave.inventory")
    private boolean onLeaveResetInventory = false;

    @ConfigComment("Reset Ender Chest - if true, the player's Ender Chest will be cleared.")
    @ConfigEntry(path = "island.reset.on-leave.ender-chest")
    private boolean onLeaveResetEnderChest = false;

    @ConfigComment("Have player's respawn on their island if they die")
    @ConfigEntry(path = "island.respawn-on-island")
    private boolean respawnOnIsland = true;

    // Deaths
    @ConfigComment("Maximum number of deaths to count. The death count can be used by add-ons.")
    @ConfigEntry(path = "island.deaths.max")
    private int deathsMax = 10;

    @ConfigEntry(path = "island.deaths.sum-team")
    private boolean deathsSumTeam = false;

    @ConfigComment("When a player joins a team, reset their death count")
    @ConfigEntry(path = "island.deaths.team-join-reset")
    private boolean teamJoinDeathReset = true;

    // ---------------------------------------------

    /*      PROTECTION      */
    private int togglePvPCooldown;

    @ConfigComment("Geo restrict mobs.")
    @ConfigComment("Mobs that exit the island space where they were spawned will be removed.")
    @ConfigEntry(path = "protection.geo-limit-settings")
    private List<String> geoLimitSettings = new ArrayList<>();

    // Invincible visitor settings
    @ConfigComment("Invincible visitors. List of damages that will not affect visitors.")
    @ConfigComment("Make list blank if visitors should receive all damages")
    @ConfigEntry(path = "protection.invincible-visitors")
    private List<String> ivSettings = new ArrayList<>();

    //---------------------------------------------------------------------------------------/

    @ConfigEntry(path = "panel.close-on-click-outside")
    private boolean closePanelOnClickOutside = true;

    //---------------------------------------------------------------------------------------/
    @ConfigComment("These settings should not be edited")
    @ConfigEntry(path = "do-not-edit-these-settings.reset-epoch")
    private long resetEpoch = 0;

    private String uniqueId = "config";

    /**
     * @return the useEconomy
     */
    public boolean isUseEconomy() {
        return useEconomy;
    }

    /**
     * @return the startingMoney
     */
    public double getStartingMoney() {
        return startingMoney;
    }

    /**
     * @return the allowObsidianScooping
     */
    public boolean isAllowObsidianScooping() {
        return allowObsidianScooping;
    }

    /**
     * @return the confirmationTime
     */
    public int getConfirmationTime() {
        return confirmationTime;
    }

    /**
     * @return the friendlyName
     */
    @Override
    public String getFriendlyName() {
        return friendlyName;
    }

    /**
     * @return the worldName
     */
    @Override
    public String getWorldName() {
        return worldName;
    }

    /**
     * @return the difficulty
     */
    @Override
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * @return the islandDistance
     */
    @Override
    public int getIslandDistance() {
        return islandDistance;
    }

    /**
     * @return the islandProtectionRange
     */
    @Override
    public int getIslandProtectionRange() {
        return islandProtectionRange;
    }

    /**
     * @return the islandStartX
     */
    @Override
    public int getIslandStartX() {
        return islandStartX;
    }

    /**
     * @return the islandStartZ
     */
    @Override
    public int getIslandStartZ() {
        return islandStartZ;
    }

    /**
     * @return the islandXOffset
     */
    @Override
    public int getIslandXOffset() {
        return islandXOffset;
    }

    /**
     * @return the islandZOffset
     */
    @Override
    public int getIslandZOffset() {
        return islandZOffset;
    }

    /**
     * @return the islandHeight
     */
    @Override
    public int getIslandHeight() {
        return islandHeight;
    }

    /**
     * @return the useOwnGenerator
     */
    @Override
    public boolean isUseOwnGenerator() {
        return useOwnGenerator;
    }

    /**
     * @return the seaHeight
     */
    @Override
    public int getSeaHeight() {
        return seaHeight;
    }

    /**
     * @return the maxIslands
     */
    @Override
    public int getMaxIslands() {
        return maxIslands;
    }

    /**
     * @return the defaultGameMode
     */
    @Override
    public GameMode getDefaultGameMode() {
        return defaultGameMode;
    }

    /**
     * @return the netherGenerate
     */
    @Override
    public boolean isNetherGenerate() {
        return netherGenerate;
    }

    /**
     * @return the netherIslands
     */
    @Override
    public boolean isNetherIslands() {
        return netherIslands;
    }

    /**
     * @return the netherTrees
     */
    @Override
    public boolean isNetherTrees() {
        return netherTrees;
    }

    /**
     * @return the netherRoof
     */
    public boolean isNetherRoof() {
        return netherRoof;
    }

    /**
     * @return the netherSpawnRadius
     */
    @Override
    public int getNetherSpawnRadius() {
        return netherSpawnRadius;
    }

    /**
     * @return the endGenerate
     */
    @Override
    public boolean isEndGenerate() {
        return endGenerate;
    }

    /**
     * @return the endIslands
     */
    @Override
    public boolean isEndIslands() {
        return endIslands;
    }

    /**
     * @return the dragonSpawn
     */
    @Override
    public boolean isDragonSpawn() {
        return dragonSpawn;
    }

    /**
     * @return the removeMobsWhitelist
     */
    @Override
    public Set<EntityType> getRemoveMobsWhitelist() {
        return removeMobsWhitelist;
    }

    /**
     * @return the worldFlags
     */
    @Override
    public Map<String, Boolean> getWorldFlags() {
        return worldFlags;
    }

    /**
     * @return the defaultIslandFlags
     */
    @Override
    public Map<Flag, Integer> getDefaultIslandFlags() {
        return defaultIslandFlags;
    }

    /**
     * @return the defaultIslandSettings
     */
    @Override
    public Map<Flag, Integer> getDefaultIslandSettings() {
        return defaultIslandSettings;
    }

    /**
     * @return the visibleSettings
     */
    @Override
    public List<String> getVisibleSettings() {
        return visibleSettings;
    }

    /**
     * @return the visitorBannedCommands
     */
    @Override
    public List<String> getVisitorBannedCommands() {
        return visitorBannedCommands;
    }

    /**
     * @return the entityLimits
     */
    @Override
    public Map<EntityType, Integer> getEntityLimits() {
        return entityLimits;
    }

    /**
     * @return the tileEntityLimits
     */
    @Override
    public Map<String, Integer> getTileEntityLimits() {
        return tileEntityLimits;
    }

    /**
     * @return the maxTeamSize
     */
    @Override
    public int getMaxTeamSize() {
        return maxTeamSize;
    }

    /**
     * @return the maxHomes
     */
    @Override
    public int getMaxHomes() {
        return maxHomes;
    }

    /**
     * @return the resetLimit
     */
    @Override
    public int getResetLimit() {
        return resetLimit;
    }

    /**
     * @return the resetConfirmation
     */
    public boolean isResetConfirmation() {
        return resetConfirmation;
    }

    /**
     * @return the leaversLoseReset
     */
    public boolean isLeaversLoseReset() {
        return leaversLoseReset;
    }

    /**
     * @return the kickedKeepInventory
     */
    public boolean isKickedKeepInventory() {
        return kickedKeepInventory;
    }

    /**
     * @return the onJoinResetMoney
     */
    @Override
    public boolean isOnJoinResetMoney() {
        return onJoinResetMoney;
    }

    /**
     * @return the onJoinResetInventory
     */
    @Override
    public boolean isOnJoinResetInventory() {
        return onJoinResetInventory;
    }

    /**
     * @return the onJoinResetEnderChest
     */
    @Override
    public boolean isOnJoinResetEnderChest() {
        return onJoinResetEnderChest;
    }

    /**
     * @return the onLeaveResetMoney
     */
    @Override
    public boolean isOnLeaveResetMoney() {
        return onLeaveResetMoney;
    }

    /**
     * @return the onLeaveResetInventory
     */
    @Override
    public boolean isOnLeaveResetInventory() {
        return onLeaveResetInventory;
    }

    /**
     * @return the onLeaveResetEnderChest
     */
    @Override
    public boolean isOnLeaveResetEnderChest() {
        return onLeaveResetEnderChest;
    }

    /**
     * @return the respawnOnIsland
     */
    public boolean isRespawnOnIsland() {
        return respawnOnIsland;
    }

    /**
     * @return the deathsMax
     */
    @Override
    public int getDeathsMax() {
        return deathsMax;
    }

    /**
     * @return the deathsSumTeam
     */
    public boolean isDeathsSumTeam() {
        return deathsSumTeam;
    }

    /**
     * @return the teamJoinDeathReset
     */
    @Override
    public boolean isTeamJoinDeathReset() {
        return teamJoinDeathReset;
    }

    /**
     * @return the togglePvPCooldown
     */
    public int getTogglePvPCooldown() {
        return togglePvPCooldown;
    }

    /**
     * @return the geoLimitSettings
     */
    @Override
    public List<String> getGeoLimitSettings() {
        return geoLimitSettings;
    }

    /**
     * @return the ivSettings
     */
    @Override
    public List<String> getIvSettings() {
        return ivSettings;
    }

    /**
     * @return the closePanelOnClickOutside
     */
    public boolean isClosePanelOnClickOutside() {
        return closePanelOnClickOutside;
    }

    /**
     * @return the resetEpoch
     */
    @Override
    public long getResetEpoch() {
        return resetEpoch;
    }

    /**
     * @return the uniqueId
     */
    @Override
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * @param useEconomy the useEconomy to set
     */
    public void setUseEconomy(boolean useEconomy) {
        this.useEconomy = useEconomy;
    }

    /**
     * @param startingMoney the startingMoney to set
     */
    public void setStartingMoney(double startingMoney) {
        this.startingMoney = startingMoney;
    }

    /**
     * @param allowObsidianScooping the allowObsidianScooping to set
     */
    public void setAllowObsidianScooping(boolean allowObsidianScooping) {
        this.allowObsidianScooping = allowObsidianScooping;
    }

    /**
     * @param confirmationTime the confirmationTime to set
     */
    public void setConfirmationTime(int confirmationTime) {
        this.confirmationTime = confirmationTime;
    }

    /**
     * @param friendlyName the friendlyName to set
     */
    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    /**
     * @param worldName the worldName to set
     */
    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    /**
     * @param difficulty the difficulty to set
     */
    @Override
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * @param islandDistance the islandDistance to set
     */
    public void setIslandDistance(int islandDistance) {
        this.islandDistance = islandDistance;
    }

    /**
     * @param islandProtectionRange the islandProtectionRange to set
     */
    public void setIslandProtectionRange(int islandProtectionRange) {
        this.islandProtectionRange = islandProtectionRange;
    }

    /**
     * @param islandStartX the islandStartX to set
     */
    public void setIslandStartX(int islandStartX) {
        this.islandStartX = islandStartX;
    }

    /**
     * @param islandStartZ the islandStartZ to set
     */
    public void setIslandStartZ(int islandStartZ) {
        this.islandStartZ = islandStartZ;
    }

    /**
     * @param islandXOffset the islandXOffset to set
     */
    public void setIslandXOffset(int islandXOffset) {
        this.islandXOffset = islandXOffset;
    }

    /**
     * @param islandZOffset the islandZOffset to set
     */
    public void setIslandZOffset(int islandZOffset) {
        this.islandZOffset = islandZOffset;
    }

    /**
     * @param islandHeight the islandHeight to set
     */
    public void setIslandHeight(int islandHeight) {
        this.islandHeight = islandHeight;
    }

    /**
     * @param useOwnGenerator the useOwnGenerator to set
     */
    public void setUseOwnGenerator(boolean useOwnGenerator) {
        this.useOwnGenerator = useOwnGenerator;
    }

    /**
     * @param seaHeight the seaHeight to set
     */
    public void setSeaHeight(int seaHeight) {
        this.seaHeight = seaHeight;
    }

    /**
     * @param maxIslands the maxIslands to set
     */
    public void setMaxIslands(int maxIslands) {
        this.maxIslands = maxIslands;
    }

    /**
     * @param defaultGameMode the defaultGameMode to set
     */
    public void setDefaultGameMode(GameMode defaultGameMode) {
        this.defaultGameMode = defaultGameMode;
    }

    /**
     * @param netherGenerate the netherGenerate to set
     */
    public void setNetherGenerate(boolean netherGenerate) {
        this.netherGenerate = netherGenerate;
    }

    /**
     * @param netherIslands the netherIslands to set
     */
    public void setNetherIslands(boolean netherIslands) {
        this.netherIslands = netherIslands;
    }

    /**
     * @param netherTrees the netherTrees to set
     */
    public void setNetherTrees(boolean netherTrees) {
        this.netherTrees = netherTrees;
    }

    /**
     * @param netherRoof the netherRoof to set
     */
    public void setNetherRoof(boolean netherRoof) {
        this.netherRoof = netherRoof;
    }

    /**
     * @param netherSpawnRadius the netherSpawnRadius to set
     */
    public void setNetherSpawnRadius(int netherSpawnRadius) {
        this.netherSpawnRadius = netherSpawnRadius;
    }

    /**
     * @param endGenerate the endGenerate to set
     */
    public void setEndGenerate(boolean endGenerate) {
        this.endGenerate = endGenerate;
    }

    /**
     * @param endIslands the endIslands to set
     */
    public void setEndIslands(boolean endIslands) {
        this.endIslands = endIslands;
    }

    /**
     * @param dragonSpawn the dragonSpawn to set
     */
    public void setDragonSpawn(boolean dragonSpawn) {
        this.dragonSpawn = dragonSpawn;
    }

    /**
     * @param removeMobsWhitelist the removeMobsWhitelist to set
     */
    public void setRemoveMobsWhitelist(Set<EntityType> removeMobsWhitelist) {
        this.removeMobsWhitelist = removeMobsWhitelist;
    }

    /**
     * @param worldFlags the worldFlags to set
     */
    public void setWorldFlags(Map<String, Boolean> worldFlags) {
        this.worldFlags = worldFlags;
    }

    /**
     * @param defaultIslandFlags the defaultIslandFlags to set
     */
    public void setDefaultIslandFlags(Map<Flag, Integer> defaultIslandFlags) {
        this.defaultIslandFlags = defaultIslandFlags;
    }

    /**
     * @param defaultIslandSettings the defaultIslandSettings to set
     */
    public void setDefaultIslandSettings(Map<Flag, Integer> defaultIslandSettings) {
        this.defaultIslandSettings = defaultIslandSettings;
    }

    /**
     * @param visibleSettings the visibleSettings to set
     */
    public void setVisibleSettings(List<String> visibleSettings) {
        this.visibleSettings = visibleSettings;
    }

    /**
     * @param visitorBannedCommands the visitorBannedCommands to set
     */
    public void setVisitorBannedCommands(List<String> visitorBannedCommands) {
        this.visitorBannedCommands = visitorBannedCommands;
    }

    /**
     * @param entityLimits the entityLimits to set
     */
    public void setEntityLimits(Map<EntityType, Integer> entityLimits) {
        this.entityLimits = entityLimits;
    }

    /**
     * @param tileEntityLimits the tileEntityLimits to set
     */
    public void setTileEntityLimits(Map<String, Integer> tileEntityLimits) {
        this.tileEntityLimits = tileEntityLimits;
    }

    /**
     * @param maxTeamSize the maxTeamSize to set
     */
    public void setMaxTeamSize(int maxTeamSize) {
        this.maxTeamSize = maxTeamSize;
    }

    /**
     * @param maxHomes the maxHomes to set
     */
    public void setMaxHomes(int maxHomes) {
        this.maxHomes = maxHomes;
    }

    /**
     * @param resetLimit the resetLimit to set
     */
    public void setResetLimit(int resetLimit) {
        this.resetLimit = resetLimit;
    }

    /**
     * @param resetConfirmation the resetConfirmation to set
     */
    public void setResetConfirmation(boolean resetConfirmation) {
        this.resetConfirmation = resetConfirmation;
    }

    /**
     * @param leaversLoseReset the leaversLoseReset to set
     */
    public void setLeaversLoseReset(boolean leaversLoseReset) {
        this.leaversLoseReset = leaversLoseReset;
    }

    /**
     * @param kickedKeepInventory the kickedKeepInventory to set
     */
    public void setKickedKeepInventory(boolean kickedKeepInventory) {
        this.kickedKeepInventory = kickedKeepInventory;
    }

    /**
     * @param onJoinResetMoney the onJoinResetMoney to set
     */
    public void setOnJoinResetMoney(boolean onJoinResetMoney) {
        this.onJoinResetMoney = onJoinResetMoney;
    }

    /**
     * @param onJoinResetInventory the onJoinResetInventory to set
     */
    public void setOnJoinResetInventory(boolean onJoinResetInventory) {
        this.onJoinResetInventory = onJoinResetInventory;
    }

    /**
     * @param onJoinResetEnderChest the onJoinResetEnderChest to set
     */
    public void setOnJoinResetEnderChest(boolean onJoinResetEnderChest) {
        this.onJoinResetEnderChest = onJoinResetEnderChest;
    }

    /**
     * @param onLeaveResetMoney the onLeaveResetMoney to set
     */
    public void setOnLeaveResetMoney(boolean onLeaveResetMoney) {
        this.onLeaveResetMoney = onLeaveResetMoney;
    }

    /**
     * @param onLeaveResetInventory the onLeaveResetInventory to set
     */
    public void setOnLeaveResetInventory(boolean onLeaveResetInventory) {
        this.onLeaveResetInventory = onLeaveResetInventory;
    }

    /**
     * @param onLeaveResetEnderChest the onLeaveResetEnderChest to set
     */
    public void setOnLeaveResetEnderChest(boolean onLeaveResetEnderChest) {
        this.onLeaveResetEnderChest = onLeaveResetEnderChest;
    }

    /**
     * @param respawnOnIsland the respawnOnIsland to set
     */
    public void setRespawnOnIsland(boolean respawnOnIsland) {
        this.respawnOnIsland = respawnOnIsland;
    }

    /**
     * @param deathsMax the deathsMax to set
     */
    public void setDeathsMax(int deathsMax) {
        this.deathsMax = deathsMax;
    }

    /**
     * @param deathsSumTeam the deathsSumTeam to set
     */
    public void setDeathsSumTeam(boolean deathsSumTeam) {
        this.deathsSumTeam = deathsSumTeam;
    }

    /**
     * @param teamJoinDeathReset the teamJoinDeathReset to set
     */
    public void setTeamJoinDeathReset(boolean teamJoinDeathReset) {
        this.teamJoinDeathReset = teamJoinDeathReset;
    }

    /**
     * @param togglePvPCooldown the togglePvPCooldown to set
     */
    public void setTogglePvPCooldown(int togglePvPCooldown) {
        this.togglePvPCooldown = togglePvPCooldown;
    }

    /**
     * @param geoLimitSettings the geoLimitSettings to set
     */
    public void setGeoLimitSettings(List<String> geoLimitSettings) {
        this.geoLimitSettings = geoLimitSettings;
    }

    /**
     * @param ivSettings the ivSettings to set
     */
    public void setIvSettings(List<String> ivSettings) {
        this.ivSettings = ivSettings;
    }

    /**
     * @param closePanelOnClickOutside the closePanelOnClickOutside to set
     */
    public void setClosePanelOnClickOutside(boolean closePanelOnClickOutside) {
        this.closePanelOnClickOutside = closePanelOnClickOutside;
    }

    /**
     * @param resetEpoch the resetEpoch to set
     */
    @Override
    public void setResetEpoch(long resetEpoch) {
        this.resetEpoch = resetEpoch;
    }

    /**
     * @param uniqueId the uniqueId to set
     */
    @Override
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public Optional<Addon> getAddon() {
        return Optional.of(BSkyBlock.getInstance());
    }

    @Override
    public String getPermissionPrefix() {
        return "bskyblock";
    }

    @Override
    public boolean isWaterUnsafe() {
        return false;
    }


}