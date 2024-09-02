package dev.viaduct.factories;

import com.google.common.base.Enums;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.eclipse.jdt.annotation.NonNull;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.configuration.ConfigComment;
import world.bentobox.bentobox.api.configuration.ConfigEntry;
import world.bentobox.bentobox.api.configuration.StoreAt;
import world.bentobox.bentobox.api.configuration.WorldSettings;
import world.bentobox.bentobox.api.flags.Flag;
import world.bentobox.bentobox.database.objects.adapters.Adapter;
import world.bentobox.bentobox.database.objects.adapters.FlagBooleanSerializer;

import java.util.*;

/**
 * All the plugin settings are here
 *
 * @author Tastybento
 */
@StoreAt(filename = "config.yml", path = "addons/factories") // Explicitly call out what name this should have.
@ConfigComment("factories Configuration [version]")
public class Settings implements WorldSettings {

    /* Commands */
    @ConfigComment("Island Command. What command users will run to access their island.")
    @ConfigComment("To define alias, just separate commands with white space.")
    @ConfigEntry(path = "factories.command.island", since = "1.3.0")
    private String playerCommandAliases = "plot p fac f";

    @ConfigComment("The island admin command.")
    @ConfigComment("To define alias, just separate commands with white space.")
    @ConfigEntry(path = "factories.command.admin", since = "1.3.0")
    private String adminCommandAliases = "plotadmin padmin pa faca fadmin fad";

    @ConfigComment("The default action for new player command call.")
    @ConfigComment("Sub-command of main player command that will be run on first player command call.")
    @ConfigComment("By default it is sub-command 'create'.")
    @ConfigEntry(path = "factories.command.new-player-action", since = "1.13.1")
    private String defaultNewPlayerAction = "create";

    @ConfigComment("The default action for player command.")
    @ConfigComment("Sub-command of main player command that will be run on each player command call.")
    @ConfigComment("By default it is sub-command 'go'.")
    @ConfigEntry(path = "factories.command.default-action", since = "1.13.1")
    private String defaultPlayerAction = "go";

    /*      WORLD       */
    @ConfigComment("Friendly name for this world. Used in admin commands. Must be a single word")
    @ConfigEntry(path = "world.friendly-name")
    private String friendlyName = "factories";

    @ConfigComment("Name of the world - if it does not exist then it will be generated.")
    @ConfigComment("It acts like a prefix for nether and end (e.g. factories_world, factories_world_nether, factories_world_end)")
    @ConfigEntry(path = "world.world-name")
    private String worldName = "factories_world";

    @ConfigComment("World difficulty setting - PEACEFUL, EASY, NORMAL, HARD")
    @ConfigComment("Other plugins may override this setting")
    @ConfigEntry(path = "world.difficulty")
    private Difficulty difficulty = Difficulty.NORMAL;

    @ConfigComment("Spawn limits. These override the limits set in bukkit.yml")
    @ConfigComment("If set to a negative number, the server defaults will be used")
    @ConfigEntry(path = "world.spawn-limits.monsters", since = "1.11.2")
    private int spawnLimitMonsters = -1;
    @ConfigEntry(path = "world.spawn-limits.animals", since = "1.11.2")
    private int spawnLimitAnimals = -1;
    @ConfigEntry(path = "world.spawn-limits.water-animals", since = "1.11.2")
    private int spawnLimitWaterAnimals = -1;
    @ConfigEntry(path = "world.spawn-limits.ambient", since = "1.11.2")
    private int spawnLimitAmbient = -1;
    @ConfigComment("Setting to 0 will disable animal spawns, but this is not recommended. Minecraft default is 400.")
    @ConfigComment("A negative value uses the server default")
    @ConfigEntry(path = "world.spawn-limits.ticks-per-animal-spawns", since = "1.11.2")
    private int ticksPerAnimalSpawns = -1;
    @ConfigComment("Setting to 0 will disable monster spawns, but this is not recommended. Minecraft default is 400.")
    @ConfigComment("A negative value uses the server default")
    @ConfigEntry(path = "world.spawn-limits.ticks-per-monster-spawns", since = "1.11.2")
    private int ticksPerMonsterSpawns = -1;

    @ConfigComment("Radius of island in blocks. (So distance between islands is twice this)")
    @ConfigComment("It is the same for every dimension : Overworld, Nether and End.")
    @ConfigComment("This value cannot be changed mid-game and the plugin will not start if it is different.")
    @ConfigEntry(path = "world.distance-between-islands", needsReset = true)
    private int islandDistance = 400;

    @ConfigComment("Default protection range radius in blocks. Cannot be larger than distance.")
    @ConfigComment("Admins can change protection sizes for players individually using /bsbadmin range set <player> <new range>")
    @ConfigComment("or set this permission: factories.island.range.<number>")
    @ConfigEntry(path = "world.protection-range")
    private int islandProtectionRange = 50;

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
    @ConfigComment("It is the y coordinate of the bedrock block in the schem.")
    @ConfigEntry(path = "world.island-height")
    private int islandHeight = 120;

    @ConfigComment("Use your own world generator for this world.")
    @ConfigComment("In this case, the plugin will not generate anything.")
    @ConfigComment("If used, you must specify the world name and generator in the bukkit.yml file.")
    @ConfigComment("See https://bukkit.gamepedia.com/Bukkit.yml#.2AOPTIONAL.2A_worlds")
    @ConfigEntry(path = "world.use-own-generator")
    private boolean useOwnGenerator;

    @ConfigComment("Sea height (don't changes this mid-game unless you delete the world)")
    @ConfigComment("Minimum is 0, which means you are playing Skyblock!")
    @ConfigComment("If sea height is less than about 10, then players will drop right through it")
    @ConfigComment("if it exists. Makes for an interesting variation on skyblock.")
    @ConfigEntry(path = "world.sea-height", needsReset = true)
    private int seaHeight = 0;

    @ConfigComment("Maximum number of islands in the world. Set to -1 or 0 for unlimited.")
    @ConfigComment("If the number of islands is greater than this number, it will stop players from creating islands.")
    @ConfigEntry(path = "world.max-islands")
    private int maxIslands = -1;

    @ConfigComment("The number of concurrent islands a player can have in the world")
    @ConfigComment("A value of 0 will use the BentoBox config.yml default")
    @ConfigEntry(path = "world.concurrent-islands")
    private int concurrentIslands = 0;

    @ConfigComment("Disallow players to have other islands if they are in a team.")
    @ConfigEntry(path = "world.disallow-team-member-islands")
    boolean disallowTeamMemberIslands = true;

    @ConfigComment("The default game mode for this world. Players will be set to this mode when they create")
    @ConfigComment("a new island for example. Options are SURVIVAL, CREATIVE, ADVENTURE, SPECTATOR")
    @ConfigEntry(path = "world.default-game-mode")
    private GameMode defaultGameMode = GameMode.SURVIVAL;

    @ConfigComment("The default biome for the overworld")
    @ConfigEntry(path = "world.default-biome")
    private Biome defaultBiome = Biome.PLAINS;
    @ConfigComment("The default biome for the nether world (this may affect what mobs can spawn)")
    @ConfigEntry(path = "world.default-nether-biome")
    private Biome defaultNetherBiome = Enums.getIfPresent(Biome.class, "NETHER").or(Enums.getIfPresent(Biome.class, "NETHER_WASTES").or(Biome.BADLANDS));
    @ConfigComment("The default biome for the end world (this may affect what mobs can spawn)")
    @ConfigEntry(path = "world.default-end-biome")
    private Biome defaultEndBiome = Biome.THE_END;

    @ConfigComment("The maximum number of players a player can ban at any one time in this game mode.")
    @ConfigComment("The permission acidisland.ban.maxlimit.X where X is a number can also be used per player")
    @ConfigComment("-1 = unlimited")
    @ConfigEntry(path = "world.ban-limit")
    private int banLimit = -1;

    // Nether
    @ConfigComment("Generate Nether - if this is false, the nether world will not be made and access to")
    @ConfigComment("the nether will not occur. Other plugins may still enable portal usage.")
    @ConfigComment("Note: Some default challenges will not be possible if there is no nether.")
    @ConfigComment("Note that with a standard nether all players arrive at the same portal and entering a")
    @ConfigComment("portal will return them back to their islands.")
    @ConfigEntry(path = "world.nether.generate")
    private boolean netherGenerate = false;

    @ConfigComment("Islands in Nether. Change to false for standard vanilla nether.")
    @ConfigEntry(path = "world.nether.islands", needsReset = true)
    private boolean netherIslands = true;

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

    @ConfigComment("This option indicates if nether portals should be linked via dimensions.")
    @ConfigComment("Option will simulate vanilla portal mechanics that links portals together")
    @ConfigComment("or creates a new portal, if there is not a portal in that dimension.")
    @ConfigComment("This option requires `allow-nether=true` in server.properties.")
    @ConfigEntry(path = "world.nether.create-and-link-portals", since = "1.14.4")
    private boolean makeNetherPortals = false;

    // End
    @ConfigComment("End Nether - if this is false, the end world will not be made and access to")
    @ConfigComment("the end will not occur. Other plugins may still enable portal usage.")
    @ConfigEntry(path = "world.end.generate")
    private boolean endGenerate = false;

    @ConfigComment("Islands in The End. Change to false for standard vanilla end.")
    @ConfigEntry(path = "world.end.islands", needsReset = true)
    private boolean endIslands = true;

    @ConfigComment("This option indicates if obsidian platform in the end should be generated")
    @ConfigComment("when player enters the end world.")
    @ConfigComment("This option requires `allow-end=true` in bukkit.yml.")
    @ConfigEntry(path = "world.end.create-obsidian-platform", since = "1.14.4")
    private boolean makeEndPortals = false;

    @ConfigEntry(path = "world.end.dragon-spawn", experimental = true)
    private boolean dragonSpawn = false;

    @ConfigComment("Mob white list - these mobs will NOT be removed when logging in or doing /island")
    @ConfigEntry(path = "world.remove-mobs-whitelist")
    private Set<EntityType> removeMobsWhitelist = new HashSet<>();

    @ConfigComment("World flags. These are boolean settings for various flags for this world")
    @ConfigEntry(path = "world.flags")
    private Map<String, Boolean> worldFlags = new HashMap<>();

    @ConfigComment("These are the default protection settings for new islands.")
    @ConfigComment("The value is the minimum island rank required allowed to do the action")
    @ConfigComment("Ranks are the following:")
    @ConfigComment("  VISITOR   = 0")
    @ConfigComment("  COOP      = 200")
    @ConfigComment("  TRUSTED   = 400")
    @ConfigComment("  MEMBER    = 500")
    @ConfigComment("  SUB-OWNER = 900")
    @ConfigComment("  OWNER     = 1000")
    @ConfigEntry(path = "world.default-island-flags")
    private Map<String, Integer> defaultIslandFlagNames = new HashMap<>();

    @ConfigComment("These are the default settings for new islands")
    @ConfigEntry(path = "world.default-island-settings")
    @Adapter(FlagBooleanSerializer.class)
    private Map<String, Integer> defaultIslandSettingNames = new HashMap<>();

    @ConfigComment("These settings/flags are hidden from users")
    @ConfigComment("Ops can toggle hiding in-game using SHIFT-LEFT-CLICK on flags in settings")
    @ConfigEntry(path = "world.hidden-flags", since = "1.4.1")
    private List<String> hiddenFlags = new ArrayList<>();

    @ConfigComment("Visitor banned commands - Visitors to islands cannot use these commands in this world")
    @ConfigEntry(path = "world.visitor-banned-commands")
    private List<String> visitorBannedCommands = new ArrayList<>();

    @ConfigComment("Falling banned commands - players cannot use these commands when falling")
    @ConfigComment("if the PREVENT_TELEPORT_WHEN_FALLING world setting flag is active")
    @ConfigEntry(path = "world.falling-banned-commands", since = "1.8.0")
    private List<String> fallingBannedCommands = new ArrayList<>();

    // ---------------------------------------------

    /*      ISLAND      */
    @ConfigComment("Default max team size")
    @ConfigComment("Permission size cannot be less than the default below. ")
    @ConfigEntry(path = "island.max-team-size")
    private int maxTeamSize = 4;

    @ConfigComment("Default maximum number of coop rank members per island")
    @ConfigComment("Players can have the factories.coop.maxsize.<number> permission to be bigger but")
    @ConfigComment("permission size cannot be less than the default below. ")
    @ConfigEntry(path = "island.max-coop-size", since = "1.13.0")
    private int maxCoopSize = 4;

    @ConfigComment("Default maximum number of trusted rank members per island")
    @ConfigComment("Players can have the factories.trust.maxsize.<number> permission to be bigger but")
    @ConfigComment("permission size cannot be less than the default below. ")
    @ConfigEntry(path = "island.max-trusted-size", since = "1.13.0")
    private int maxTrustSize = 4;

    @ConfigComment("Default maximum number of homes a player can have. Min = 1")
    @ConfigComment("Accessed via /is sethome <number> or /is go <number>")
    @ConfigEntry(path = "island.max-homes")
    private int maxHomes = 5;

    // Reset
    @ConfigComment("How many resets a player is allowed (manage with /bsbadmin reset add/remove/reset/set command)")
    @ConfigComment("Value of -1 means unlimited, 0 means hardcore - no resets.")
    @ConfigComment("Example, 2 resets means they get 2 resets or 3 islands lifetime")
    @ConfigEntry(path = "island.reset.reset-limit")
    private int resetLimit = -1;

    @ConfigComment("Kicked or leaving players lose resets")
    @ConfigComment("Players who leave a team will lose an island reset chance")
    @ConfigComment("If a player has zero resets left and leaves a team, they cannot make a new")
    @ConfigComment("island by themselves and can only join a team.")
    @ConfigComment("Leave this true to avoid players exploiting free islands")
    @ConfigEntry(path = "island.reset.leavers-lose-reset")
    private boolean leaversLoseReset = false;

    @ConfigComment("Allow kicked players to keep their inventory.")
    @ConfigComment("Overrides the on-leave inventory reset for kicked players.")
    @ConfigEntry(path = "island.reset.kicked-keep-inventory")
    private boolean kickedKeepInventory = false;

    @ConfigComment("What the addon should reset when the player joins or creates an island")
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

    @ConfigComment("Reset health - if true, the player's health will be reset.")
    @ConfigEntry(path = "island.reset.on-join.health", since = "1.8.0")
    private boolean onJoinResetHealth = true;

    @ConfigComment("Reset hunger - if true, the player's hunger will be reset.")
    @ConfigEntry(path = "island.reset.on-join.hunger", since = "1.8.0")
    private boolean onJoinResetHunger = true;

    @ConfigComment("Reset experience points - if true, the player's experience will be reset.")
    @ConfigEntry(path = "island.reset.on-join.exp", since = "1.8.0")
    private boolean onJoinResetXP = false;


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

    @ConfigComment("Reset health - if true, the player's health will be reset.")
    @ConfigEntry(path = "island.reset.on-leave.health", since = "1.8.0")
    private boolean onLeaveResetHealth = false;

    @ConfigComment("Reset hunger - if true, the player's hunger will be reset.")
    @ConfigEntry(path = "island.reset.on-leave.hunger", since = "1.8.0")
    private boolean onLeaveResetHunger = false;

    @ConfigComment("Reset experience - if true, the player's experience will be reset.")
    @ConfigEntry(path = "island.reset.on-leave.exp", since = "1.8.0")
    private boolean onLeaveResetXP = false;

    @ConfigComment("Reset Ender Chest - if true, the player's Ender Chest will be cleared.")
    @ConfigEntry(path = "island.reset.on-leave.ender-chest")
    private boolean onLeaveResetEnderChest = false;

    @ConfigComment("Toggles the automatic island creation upon the player's first login on your server.")
    @ConfigComment("If set to true,")
    @ConfigComment("   * Upon connecting to your server for the first time, the player will be told that")
    @ConfigComment("    an island will be created for him.")
    @ConfigComment("  * Make sure you have a Blueprint Bundle called \"default\": this is the one that will")
    @ConfigComment("    be used to create the island.")
    @ConfigComment("  * An island will be created for the player without needing him to run the create command.")
    @ConfigComment("If set to false, this will disable this feature entirely.")
    @ConfigComment("Warning:")
    @ConfigComment("  * If you are running multiple gamemodes on your server, and all of them have")
    @ConfigComment("    this feature enabled, an island in all the gamemodes will be created simultaneously.")
    @ConfigComment("    However, it is impossible to know on which island the player will be teleported to afterwards.")
    @ConfigComment("  * Island creation can be resource-intensive, please consider the options below to help mitigate")
    @ConfigComment("    the potential issues, especially if you expect a lot of players to connect to your server")
    @ConfigComment("    in a limited period of time.")
    @ConfigEntry(path = "island.create-island-on-first-login.enable", since = "1.9.0")
    private boolean createIslandOnFirstLoginEnabled;

    @ConfigComment("Time in seconds after the player logged in, before his island gets created.")
    @ConfigComment("If set to 0 or less, the island will be created directly upon the player's login.")
    @ConfigComment("It is recommended to keep this value under a minute's time.")
    @ConfigEntry(path = "island.create-island-on-first-login.delay", since = "1.9.0")
    private int createIslandOnFirstLoginDelay = 5;

    @ConfigComment("Toggles whether the island creation should be aborted if the player logged off while the")
    @ConfigComment("delay (see the option above) has not worn off yet.")
    @ConfigComment("If set to true,")
    @ConfigComment("  * If the player has logged off the server while the delay (see the option above) has not")
    @ConfigComment("    worn off yet, this will cancel the island creation.")
    @ConfigComment("  * If the player relogs afterward, since he will not be recognized as a new player, no island")
    @ConfigComment("    would be created for him.")
    @ConfigComment("  * If the island creation started before the player logged off, it will continue.")
    @ConfigComment("If set to false, the player's island will be created even if he went offline in the meantime.")
    @ConfigComment("Note this option has no effect if the delay (see the option above) is set to 0 or less.")
    @ConfigEntry(path = "island.create-island-on-first-login.abort-on-logout", since = "1.9.0")
    private boolean createIslandOnFirstLoginAbortOnLogout = true;

    @ConfigComment("Toggles whether the player should be teleported automatically to his island when it is created.")
    @ConfigComment("If set to false, the player will be told his island is ready but will have to teleport to his island using the command.")
    @ConfigEntry(path = "island.teleport-player-to-island-when-created", since = "1.10.0")
    private boolean teleportPlayerToIslandUponIslandCreation = true;

    @ConfigComment("Create Nether or End islands if they are missing when a player goes through a portal.")
    @ConfigComment("Nether and End islands are usually pasted when a player makes their island, but if they are")
    @ConfigComment("missing for some reason, you can switch this on.")
    @ConfigComment("Note that bedrock removal glitches can exploit this option.")
    @ConfigEntry(path = "island.create-missing-nether-end-islands", since = "1.10.0")
    private boolean pasteMissingIslands = false;

    // Commands
    @ConfigComment("List of commands to run when a player joins an island or creates one.")
    @ConfigComment("These commands are run by the console, unless otherwise stated using the [SUDO] prefix,")
    @ConfigComment("in which case they are executed by the player.")
    @ConfigComment("")
    @ConfigComment("Available placeholders for the commands are the following:")
    @ConfigComment("   * [name]: name of the player")
    @ConfigComment("")
    @ConfigComment("Here are some examples of valid commands to execute:")
    @ConfigComment("   * \"[SUDO] bbox version\"")
    @ConfigComment("   * \"bsbadmin deaths set [player] 0\"")
    @ConfigEntry(path = "island.commands.on-join", since = "1.8.0")
    private List<String> onJoinCommands = new ArrayList<>();

    @ConfigComment("List of commands to run when a player leaves an island, resets his island or gets kicked from it.")
    @ConfigComment("These commands are run by the console, unless otherwise stated using the [SUDO] prefix,")
    @ConfigComment("in which case they are executed by the player.")
    @ConfigComment("")
    @ConfigComment("Available placeholders for the commands are the following:")
    @ConfigComment("   * [name]: name of the player")
    @ConfigComment("")
    @ConfigComment("Here are some examples of valid commands to execute:")
    @ConfigComment("   * '[SUDO] bbox version'")
    @ConfigComment("   * 'bsbadmin deaths set [player] 0'")
    @ConfigComment("")
    @ConfigComment("Note that player-executed commands might not work, as these commands can be run with said player being offline.")
    @ConfigEntry(path = "island.commands.on-leave", since = "1.8.0")
    private List<String> onLeaveCommands = new ArrayList<>();

    @ConfigComment("List of commands that should be executed when the player respawns after death if Flags.ISLAND_RESPAWN is true.")
    @ConfigComment("These commands are run by the console, unless otherwise stated using the [SUDO] prefix,")
    @ConfigComment("in which case they are executed by the player.")
    @ConfigComment("")
    @ConfigComment("Available placeholders for the commands are the following:")
    @ConfigComment("   * [name]: name of the player")
    @ConfigComment("")
    @ConfigComment("Here are some examples of valid commands to execute:")
    @ConfigComment("   * '[SUDO] bbox version'")
    @ConfigComment("   * 'bsbadmin deaths set [player] 0'")
    @ConfigComment("")
    @ConfigComment("Note that player-executed commands might not work, as these commands can be run with said player being offline.")
    @ConfigEntry(path = "island.commands.on-respawn", since = "1.14.0")
    private List<String> onRespawnCommands = new ArrayList<>();

    // Sethome
    @ConfigComment("Allow setting home in the nether. Only available on nether islands, not vanilla nether.")
    @ConfigEntry(path = "island.sethome.nether.allow")
    private boolean allowSetHomeInNether = true;

    @ConfigEntry(path = "island.sethome.nether.require-confirmation")
    private boolean requireConfirmationToSetHomeInNether = true;

    @ConfigComment("Allow setting home in the end. Only available on end islands, not vanilla end.")
    @ConfigEntry(path = "island.sethome.the-end.allow")
    private boolean allowSetHomeInTheEnd = true;

    @ConfigEntry(path = "island.sethome.the-end.require-confirmation")
    private boolean requireConfirmationToSetHomeInTheEnd = true;

    // Deaths
    @ConfigComment("Whether deaths are counted or not.")
    @ConfigEntry(path = "island.deaths.counted")
    private boolean deathsCounted = true;

    @ConfigComment("Maximum number of deaths to count. The death count can be used by add-ons.")
    @ConfigEntry(path = "island.deaths.max")
    private int deathsMax = 10;

    @ConfigComment("When a player joins a team, reset their death count")
    @ConfigEntry(path = "island.deaths.team-join-reset")
    private boolean teamJoinDeathReset = true;

    @ConfigComment("Reset player death count when they start a new island or reset an island")
    @ConfigEntry(path = "island.deaths.reset-on-new-island", since = "1.6.0")
    private boolean deathsResetOnNewIsland = true;

    // ---------------------------------------------
    /*      PROTECTION      */

    @ConfigComment("Geo restrict mobs.")
    @ConfigComment("Mobs that exit the island space where they were spawned will be removed.")
    @ConfigEntry(path = "protection.geo-limit-settings")
    private List<String> geoLimitSettings = new ArrayList<>();

    @ConfigComment("factories blocked mobs.")
    @ConfigComment("List of mobs that should not spawn in factories.")
    @ConfigEntry(path = "protection.block-mobs", since = "1.13.1")
    private List<String> mobLimitSettings = new ArrayList<>();

    // Invincible visitor settings
    @ConfigComment("Invincible visitors. List of damages that will not affect visitors.")
    @ConfigComment("Make list blank if visitors should receive all damages")
    @ConfigEntry(path = "protection.invincible-visitors")
    private List<String> ivSettings = new ArrayList<>();

    //---------------------------------------------------------------------------------------/
    @ConfigComment("These settings should not be edited")
    @ConfigEntry(path = "do-not-edit-these-settings.reset-epoch")
    private long resetEpoch = 0;

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
     * @deprecated since 1.21
     */
    @Override
    public Map<Flag, Integer> getDefaultIslandFlags() {
        return Collections.emptyMap();
    }


    /**
     * @return the defaultIslandSettings
     * @deprecated since 1.21
     */
    @Override
    public Map<Flag, Integer> getDefaultIslandSettings() {
        return Collections.emptyMap();
    }


    /**
     * Return map of flags ID's linked to default rank for new island.
     * This is necessary so users could specify any flag names in settings file from other plugins and addons.
     * Otherwise, Flag reader would mark flag as invalid and remove it.
     *
     * @return default rank settings for new islands.
     * @since 1.21
     */
    @Override
    public Map<String, Integer> getDefaultIslandFlagNames() {
        return this.defaultIslandFlagNames;
    }


    /**
     * Return map of flags ID's linked to default settings for new island.
     * This is necessary so users could specify any flag names in settings file from other plugins and addons.
     * Otherwise, Flag reader would mark flag as invalid and remove it.
     *
     * @return default settings for new islands.
     * @since 1.21
     */
    @Override
    public Map<String, Integer> getDefaultIslandSettingNames() {
        return this.defaultIslandSettingNames;
    }


    /**
     * @return the hidden flags
     */
    @Override
    public List<String> getHiddenFlags() {
        return hiddenFlags;
    }

    /**
     * @return the visitorBannedCommands
     */
    @Override
    public List<String> getVisitorBannedCommands() {
        return visitorBannedCommands;
    }

    /**
     * @return the fallingBannedCommands
     */
    @Override
    public List<String> getFallingBannedCommands() {
        return fallingBannedCommands;
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
     * @return the leaversLoseReset
     */
    @Override
    public boolean isLeaversLoseReset() {
        return leaversLoseReset;
    }

    /**
     * @return the kickedKeepInventory
     */
    @Override
    public boolean isKickedKeepInventory() {
        return kickedKeepInventory;
    }


    /**
     * This method returns the createIslandOnFirstLoginEnabled boolean value.
     *
     * @return the createIslandOnFirstLoginEnabled value
     * @since 1.9.0
     */
    @Override
    public boolean isCreateIslandOnFirstLoginEnabled() {
        return createIslandOnFirstLoginEnabled;
    }


    /**
     * This method returns the createIslandOnFirstLoginDelay int value.
     *
     * @return the createIslandOnFirstLoginDelay value
     * @since 1.9.0
     */
    @Override
    public int getCreateIslandOnFirstLoginDelay() {
        return createIslandOnFirstLoginDelay;
    }


    /**
     * This method returns the createIslandOnFirstLoginAbortOnLogout boolean value.
     *
     * @return the createIslandOnFirstLoginAbortOnLogout value
     * @since 1.9.0
     */
    @Override
    public boolean isCreateIslandOnFirstLoginAbortOnLogout() {
        return createIslandOnFirstLoginAbortOnLogout;
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
     * @return the isDeathsCounted
     */
    @Override
    public boolean isDeathsCounted() {
        return deathsCounted;
    }

    /**
     * @return the allowSetHomeInNether
     */
    @Override
    public boolean isAllowSetHomeInNether() {
        return allowSetHomeInNether;
    }

    /**
     * @return the allowSetHomeInTheEnd
     */
    @Override
    public boolean isAllowSetHomeInTheEnd() {
        return allowSetHomeInTheEnd;
    }

    /**
     * @return the requireConfirmationToSetHomeInNether
     */
    @Override
    public boolean isRequireConfirmationToSetHomeInNether() {
        return requireConfirmationToSetHomeInNether;
    }

    /**
     * @return the requireConfirmationToSetHomeInTheEnd
     */
    @Override
    public boolean isRequireConfirmationToSetHomeInTheEnd() {
        return requireConfirmationToSetHomeInTheEnd;
    }

    /**
     * @return the deathsMax
     */
    @Override
    public int getDeathsMax() {
        return deathsMax;
    }

    /**
     * @return the teamJoinDeathReset
     */
    @Override
    public boolean isTeamJoinDeathReset() {
        return teamJoinDeathReset;
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
     * @return the resetEpoch
     */
    @Override
    public long getResetEpoch() {
        return resetEpoch;
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
     * Sets default island flag names.
     *
     * @param defaultIslandFlagNames the default island flag names
     */
    public void setDefaultIslandFlagNames(Map<String, Integer> defaultIslandFlagNames) {
        this.defaultIslandFlagNames = defaultIslandFlagNames;
    }


    /**
     * Sets default island setting names.
     *
     * @param defaultIslandSettingNames the default island setting names
     */
    public void setDefaultIslandSettingNames(Map<String, Integer> defaultIslandSettingNames) {
        this.defaultIslandSettingNames = defaultIslandSettingNames;
    }


    /**
     * @param hiddenFlags the hidden flags to set
     */
    public void setHiddenFlags(List<String> hiddenFlags) {
        this.hiddenFlags = hiddenFlags;
    }

    /**
     * @param visitorBannedCommands the visitorBannedCommands to set
     */
    public void setVisitorBannedCommands(List<String> visitorBannedCommands) {
        this.visitorBannedCommands = visitorBannedCommands;
    }

    /**
     * @param fallingBannedCommands the fallingBannedCommands to set
     */
    public void setFallingBannedCommands(List<String> fallingBannedCommands) {
        this.fallingBannedCommands = fallingBannedCommands;
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
     * @param createIslandOnFirstLoginEnabled the createIslandOnFirstLoginEnabled to set
     */
    public void setCreateIslandOnFirstLoginEnabled(boolean createIslandOnFirstLoginEnabled) {
        this.createIslandOnFirstLoginEnabled = createIslandOnFirstLoginEnabled;
    }

    /**
     * @param createIslandOnFirstLoginDelay the createIslandOnFirstLoginDelay to set
     */
    public void setCreateIslandOnFirstLoginDelay(int createIslandOnFirstLoginDelay) {
        this.createIslandOnFirstLoginDelay = createIslandOnFirstLoginDelay;
    }

    /**
     * @param createIslandOnFirstLoginAbortOnLogout the createIslandOnFirstLoginAbortOnLogout to set
     */
    public void setCreateIslandOnFirstLoginAbortOnLogout(boolean createIslandOnFirstLoginAbortOnLogout) {
        this.createIslandOnFirstLoginAbortOnLogout = createIslandOnFirstLoginAbortOnLogout;
    }

    /**
     * @param deathsCounted the deathsCounted to set
     */
    public void setDeathsCounted(boolean deathsCounted) {
        this.deathsCounted = deathsCounted;
    }

    /**
     * @param deathsMax the deathsMax to set
     */
    public void setDeathsMax(int deathsMax) {
        this.deathsMax = deathsMax;
    }

    /**
     * @param teamJoinDeathReset the teamJoinDeathReset to set
     */
    public void setTeamJoinDeathReset(boolean teamJoinDeathReset) {
        this.teamJoinDeathReset = teamJoinDeathReset;
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
     * @param allowSetHomeInNether the allowSetHomeInNether to set
     */
    public void setAllowSetHomeInNether(boolean allowSetHomeInNether) {
        this.allowSetHomeInNether = allowSetHomeInNether;
    }

    /**
     * @param allowSetHomeInTheEnd the allowSetHomeInTheEnd to set
     */
    public void setAllowSetHomeInTheEnd(boolean allowSetHomeInTheEnd) {
        this.allowSetHomeInTheEnd = allowSetHomeInTheEnd;
    }

    /**
     * @param requireConfirmationToSetHomeInNether the requireConfirmationToSetHomeInNether to set
     */
    public void setRequireConfirmationToSetHomeInNether(boolean requireConfirmationToSetHomeInNether) {
        this.requireConfirmationToSetHomeInNether = requireConfirmationToSetHomeInNether;
    }

    /**
     * @param requireConfirmationToSetHomeInTheEnd the requireConfirmationToSetHomeInTheEnd to set
     */
    public void setRequireConfirmationToSetHomeInTheEnd(boolean requireConfirmationToSetHomeInTheEnd) {
        this.requireConfirmationToSetHomeInTheEnd = requireConfirmationToSetHomeInTheEnd;
    }

    /**
     * @param resetEpoch the resetEpoch to set
     */
    @Override
    public void setResetEpoch(long resetEpoch) {
        this.resetEpoch = resetEpoch;
    }

    @Override
    public String getPermissionPrefix() {
        return "factories";
    }

    @Override
    public boolean isWaterUnsafe() {
        return false;
    }

    /**
     * @return default biome
     */
    public Biome getDefaultBiome() {
        return defaultBiome;
    }

    /**
     * @param defaultBiome the defaultBiome to set
     */
    public void setDefaultBiome(Biome defaultBiome) {
        this.defaultBiome = defaultBiome;
    }

    /**
     * @return the banLimit
     */
    @Override
    public int getBanLimit() {
        return banLimit;
    }

    /**
     * @param banLimit the banLimit to set
     */
    public void setBanLimit(int banLimit) {
        this.banLimit = banLimit;
    }

    /**
     * @return the playerCommandAliases
     */
    @Override
    public String getPlayerCommandAliases() {
        return playerCommandAliases;
    }

    /**
     * @param playerCommandAliases the playerCommandAliases to set
     */
    public void setPlayerCommandAliases(String playerCommandAliases) {
        this.playerCommandAliases = playerCommandAliases;
    }

    /**
     * @return the adminCommandAliases
     */
    @Override
    public String getAdminCommandAliases() {
        return adminCommandAliases;
    }

    /**
     * @param adminCommandAliases the adminCommandAliases to set
     */
    public void setAdminCommandAliases(String adminCommandAliases) {
        this.adminCommandAliases = adminCommandAliases;
    }

    /**
     * @return the deathsResetOnNew
     */
    @Override
    public boolean isDeathsResetOnNewIsland() {
        return deathsResetOnNewIsland;
    }

    /**
     * @param deathsResetOnNew the deathsResetOnNew to set
     */
    public void setDeathsResetOnNewIsland(boolean deathsResetOnNew) {
        this.deathsResetOnNewIsland = deathsResetOnNew;
    }

    /**
     * @return the onJoinCommands
     */
    @Override
    public List<String> getOnJoinCommands() {
        return onJoinCommands;
    }

    /**
     * @param onJoinCommands the onJoinCommands to set
     */
    public void setOnJoinCommands(List<String> onJoinCommands) {
        this.onJoinCommands = onJoinCommands;
    }

    /**
     * @return the onLeaveCommands
     */
    @Override
    public List<String> getOnLeaveCommands() {
        return onLeaveCommands;
    }

    /**
     * @param onLeaveCommands the onLeaveCommands to set
     */
    public void setOnLeaveCommands(List<String> onLeaveCommands) {
        this.onLeaveCommands = onLeaveCommands;
    }

    /**
     * @return the onRespawnCommands
     */
    @NonNull
    @Override
    public List<String> getOnRespawnCommands() {
        return onRespawnCommands;
    }

    /**
     * Sets on respawn commands.
     *
     * @param onRespawnCommands the on respawn commands
     */
    public void setOnRespawnCommands(List<String> onRespawnCommands) {
        this.onRespawnCommands = onRespawnCommands;
    }


    /**
     * @return the onJoinResetHealth
     */
    @Override
    public boolean isOnJoinResetHealth() {
        return onJoinResetHealth;
    }

    /**
     * @param onJoinResetHealth the onJoinResetHealth to set
     */
    public void setOnJoinResetHealth(boolean onJoinResetHealth) {
        this.onJoinResetHealth = onJoinResetHealth;
    }

    /**
     * @return the onJoinResetHunger
     */
    @Override
    public boolean isOnJoinResetHunger() {
        return onJoinResetHunger;
    }

    /**
     * @param onJoinResetHunger the onJoinResetHunger to set
     */
    public void setOnJoinResetHunger(boolean onJoinResetHunger) {
        this.onJoinResetHunger = onJoinResetHunger;
    }

    /**
     * @return the onJoinResetXP
     */
    @Override
    public boolean isOnJoinResetXP() {
        return onJoinResetXP;
    }

    /**
     * @param onJoinResetXP the onJoinResetXP to set
     */
    public void setOnJoinResetXP(boolean onJoinResetXP) {
        this.onJoinResetXP = onJoinResetXP;
    }

    /**
     * @return the onLeaveResetHealth
     */
    @Override
    public boolean isOnLeaveResetHealth() {
        return onLeaveResetHealth;
    }

    /**
     * @param onLeaveResetHealth the onLeaveResetHealth to set
     */
    public void setOnLeaveResetHealth(boolean onLeaveResetHealth) {
        this.onLeaveResetHealth = onLeaveResetHealth;
    }

    /**
     * @return the onLeaveResetHunger
     */
    @Override
    public boolean isOnLeaveResetHunger() {
        return onLeaveResetHunger;
    }

    /**
     * @param onLeaveResetHunger the onLeaveResetHunger to set
     */
    public void setOnLeaveResetHunger(boolean onLeaveResetHunger) {
        this.onLeaveResetHunger = onLeaveResetHunger;
    }

    /**
     * @return the onLeaveResetXP
     */
    @Override
    public boolean isOnLeaveResetXP() {
        return onLeaveResetXP;
    }

    /**
     * @param onLeaveResetXP the onLeaveResetXP to set
     */
    public void setOnLeaveResetXP(boolean onLeaveResetXP) {
        this.onLeaveResetXP = onLeaveResetXP;
    }

    /**
     * @return the pasteMissingIslands
     */
    @Override
    public boolean isPasteMissingIslands() {
        return pasteMissingIslands;
    }

    /**
     * @param pasteMissingIslands the pasteMissingIslands to set
     */
    public void setPasteMissingIslands(boolean pasteMissingIslands) {
        this.pasteMissingIslands = pasteMissingIslands;
    }

    /**
     * Toggles whether the player should be teleported automatically to his island when it is created.
     *
     * @return {@code true} if the player should be teleported automatically to his island when it is created,
     * {@code false} otherwise.
     * @since 1.10.0
     */
    @Override
    public boolean isTeleportPlayerToIslandUponIslandCreation() {
        return teleportPlayerToIslandUponIslandCreation;
    }

    /**
     * @param teleportPlayerToIslandUponIslandCreation the teleportPlayerToIslandUponIslandCreation to set
     * @since 1.10.0
     */
    public void setTeleportPlayerToIslandUponIslandCreation(boolean teleportPlayerToIslandUponIslandCreation) {
        this.teleportPlayerToIslandUponIslandCreation = teleportPlayerToIslandUponIslandCreation;
    }

    /**
     * @return the spawnLimitMonsters
     */
    public int getSpawnLimitMonsters() {
        return spawnLimitMonsters;
    }

    /**
     * @param spawnLimitMonsters the spawnLimitMonsters to set
     */
    public void setSpawnLimitMonsters(int spawnLimitMonsters) {
        this.spawnLimitMonsters = spawnLimitMonsters;
    }

    /**
     * @return the spawnLimitAnimals
     */
    public int getSpawnLimitAnimals() {
        return spawnLimitAnimals;
    }

    /**
     * @param spawnLimitAnimals the spawnLimitAnimals to set
     */
    public void setSpawnLimitAnimals(int spawnLimitAnimals) {
        this.spawnLimitAnimals = spawnLimitAnimals;
    }

    /**
     * @return the spawnLimitWaterAnimals
     */
    public int getSpawnLimitWaterAnimals() {
        return spawnLimitWaterAnimals;
    }

    /**
     * @param spawnLimitWaterAnimals the spawnLimitWaterAnimals to set
     */
    public void setSpawnLimitWaterAnimals(int spawnLimitWaterAnimals) {
        this.spawnLimitWaterAnimals = spawnLimitWaterAnimals;
    }

    /**
     * @return the spawnLimitAmbient
     */
    public int getSpawnLimitAmbient() {
        return spawnLimitAmbient;
    }

    /**
     * @param spawnLimitAmbient the spawnLimitAmbient to set
     */
    public void setSpawnLimitAmbient(int spawnLimitAmbient) {
        this.spawnLimitAmbient = spawnLimitAmbient;
    }

    /**
     * @return the ticksPerAnimalSpawns
     */
    public int getTicksPerAnimalSpawns() {
        return ticksPerAnimalSpawns;
    }

    /**
     * @param ticksPerAnimalSpawns the ticksPerAnimalSpawns to set
     */
    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {
        this.ticksPerAnimalSpawns = ticksPerAnimalSpawns;
    }

    /**
     * @return the ticksPerMonsterSpawns
     */
    public int getTicksPerMonsterSpawns() {
        return ticksPerMonsterSpawns;
    }

    /**
     * @param ticksPerMonsterSpawns the ticksPerMonsterSpawns to set
     */
    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {
        this.ticksPerMonsterSpawns = ticksPerMonsterSpawns;
    }

    /**
     * @return the maxCoopSize
     */
    @Override
    public int getMaxCoopSize() {
        return maxCoopSize;
    }

    /**
     * @param maxCoopSize the maxCoopSize to set
     */
    public void setMaxCoopSize(int maxCoopSize) {
        this.maxCoopSize = maxCoopSize;
    }

    /**
     * @return the maxTrustSize
     */
    @Override
    public int getMaxTrustSize() {
        return maxTrustSize;
    }

    /**
     * @param maxTrustSize the maxTrustSize to set
     */
    public void setMaxTrustSize(int maxTrustSize) {
        this.maxTrustSize = maxTrustSize;
    }

    /**
     * @return the defaultNewPlayerAction
     */
    @Override
    public String getDefaultNewPlayerAction() {
        return defaultNewPlayerAction;
    }

    /**
     * @param defaultNewPlayerAction the defaultNewPlayerAction to set
     */
    public void setDefaultNewPlayerAction(String defaultNewPlayerAction) {
        this.defaultNewPlayerAction = defaultNewPlayerAction;
    }

    /**
     * @return the defaultPlayerAction
     */
    @Override
    public String getDefaultPlayerAction() {
        return defaultPlayerAction;
    }

    /**
     * @param defaultPlayerAction the defaultPlayerAction to set
     */
    public void setDefaultPlayerAction(String defaultPlayerAction) {
        this.defaultPlayerAction = defaultPlayerAction;
    }

    /**
     * @return the mobLimitSettings
     */
    @Override
    public List<String> getMobLimitSettings() {
        return mobLimitSettings;
    }

    /**
     * @param mobLimitSettings the mobLimitSettings to set
     */
    public void setMobLimitSettings(List<String> mobLimitSettings) {
        this.mobLimitSettings = mobLimitSettings;
    }

    /**
     * @return the defaultNetherBiome
     */
    public Biome getDefaultNetherBiome() {
        return defaultNetherBiome;
    }

    /**
     * @param defaultNetherBiome the defaultNetherBiome to set
     */
    public void setDefaultNetherBiome(Biome defaultNetherBiome) {
        this.defaultNetherBiome = defaultNetherBiome;
    }

    /**
     * @return the defaultEndBiome
     */
    public Biome getDefaultEndBiome() {
        return defaultEndBiome;
    }

    /**
     * @param defaultEndBiome the defaultEndBiome to set
     */
    public void setDefaultEndBiome(Biome defaultEndBiome) {
        this.defaultEndBiome = defaultEndBiome;
    }

    /**
     * @return the makeNetherPortals
     */
    @Override
    public boolean isMakeNetherPortals() {
        return makeNetherPortals;
    }

    /**
     * @return the makeEndPortals
     */
    @Override
    public boolean isMakeEndPortals() {
        return makeEndPortals;
    }

    /**
     * Sets make nether portals.
     *
     * @param makeNetherPortals the make nether portals
     */
    public void setMakeNetherPortals(boolean makeNetherPortals) {
        this.makeNetherPortals = makeNetherPortals;
    }

    /**
     * Sets make end portals.
     *
     * @param makeEndPortals the make end portals
     */
    public void setMakeEndPortals(boolean makeEndPortals) {
        this.makeEndPortals = makeEndPortals;
    }

    /**
     * @return the concurrentIslands
     */
    @Override
    public int getConcurrentIslands() {
        if (concurrentIslands <= 0) {
            return BentoBox.getInstance().getSettings().getIslandNumber();
        }
        return concurrentIslands;
    }

    /**
     * @param concurrentIslands the concurrentIslands to set
     */
    public void setConcurrentIslands(int concurrentIslands) {
        this.concurrentIslands = concurrentIslands;
    }

    /**
     * @return the disallowTeamMemberIslands
     */
    @Override
    public boolean isDisallowTeamMemberIslands() {
        return disallowTeamMemberIslands;
    }

    /**
     * @param disallowTeamMemberIslands the disallowTeamMemberIslands to set
     */
    public void setDisallowTeamMemberIslands(boolean disallowTeamMemberIslands) {
        this.disallowTeamMemberIslands = disallowTeamMemberIslands;
    }

}
