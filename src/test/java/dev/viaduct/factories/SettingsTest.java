package dev.viaduct.factories;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author tastybento
 */
public class SettingsTest {

    Settings s;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        s = new Settings();
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setFriendlyName(java.lang.String)}.
     */
    @Test
    public void testSetFriendlyName() {
        s.setFriendlyName("name");
        assertEquals("name", s.getFriendlyName());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setWorldName(java.lang.String)}.
     */
    @Test
    public void testSetWorldName() {
        s.setWorldName("name");
        assertEquals("name", s.getWorldName());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setDifficulty(org.bukkit.Difficulty)}.
     */
    @Test
    public void testSetDifficulty() {
        s.setDifficulty(Difficulty.PEACEFUL);
        assertEquals(Difficulty.PEACEFUL, s.getDifficulty());
    }

    /**
     * Test method for {@link dev.viacduct.factories.Settings#setIslandDistance(int)}.
     */
    @Test
    public void testSetIslandDistance() {
        s.setIslandDistance(123);
        assertEquals(123, s.getIslandDistance());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setIslandProtectionRange(int)}.
     */
    @Test
    public void testSetIslandProtectionRange() {
        s.setIslandProtectionRange(123);
        assertEquals(123, s.getIslandProtectionRange());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setIslandStartX(int)}.
     */
    @Test
    public void testSetIslandStartX() {
        s.setIslandStartX(123);
        assertEquals(123, s.getIslandStartX());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setIslandStartZ(int)}.
     */
    @Test
    public void testSetIslandStartZ() {
        s.setIslandStartZ(123);
        assertEquals(123, s.getIslandStartZ());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setIslandXOffset(int)}.
     */
    @Test
    public void testSetIslandXOffset() {
        s.setIslandXOffset(123);
        assertEquals(123, s.getIslandXOffset());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setIslandZOffset(int)}.
     */
    @Test
    public void testSetIslandZOffset() {
        s.setIslandZOffset(123);
        assertEquals(123, s.getIslandZOffset());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setIslandHeight(int)}.
     */
    @Test
    public void testSetIslandHeight() {
        s.setIslandHeight(123);
        assertEquals(123, s.getIslandHeight());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setUseOwnGenerator(boolean)}.
     */
    @Test
    public void testSetUseOwnGenerator() {
        s.setUseOwnGenerator(true);
        assertTrue(s.isUseOwnGenerator());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setSeaHeight(int)}.
     */
    @Test
    public void testSetSeaHeight() {
        s.setSeaHeight(123);
        assertEquals(123, s.getSeaHeight());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setMaxIslands(int)}.
     */
    @Test
    public void testSetMaxIslands() {
        s.setMaxIslands(123);
        assertEquals(123, s.getMaxIslands());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setDefaultGameMode(org.bukkit.GameMode)}.
     */
    @Test
    public void testSetDefaultGameMode() {
        s.setDefaultGameMode(GameMode.CREATIVE);
        assertEquals(GameMode.CREATIVE, s.getDefaultGameMode());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setNetherGenerate(boolean)}.
     */
    @Test
    public void testSetNetherGenerate() {
        s.setNetherGenerate(true);
        assertTrue(s.isNetherGenerate());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setNetherIslands(boolean)}.
     */
    @Test
    public void testSetNetherIslands() {
        s.setNetherIslands(true);
        assertTrue(s.isNetherIslands());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setNetherRoof(boolean)}.
     */
    @Test
    public void testSetNetherRoof() {
        s.setNetherRoof(true);
        assertTrue(s.isNetherRoof());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setNetherSpawnRadius(int)}.
     */
    @Test
    public void testSetNetherSpawnRadius() {
        s.setNetherSpawnRadius(123);
        assertEquals(123, s.getNetherSpawnRadius());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setEndGenerate(boolean)}.
     */
    @Test
    public void testSetEndGenerate() {
        s.setEndGenerate(true);
        assertTrue(s.isEndGenerate());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setEndIslands(boolean)}.
     */
    @Test
    public void testSetEndIslands() {
        s.setEndIslands(true);
        assertTrue(s.isEndIslands());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setDragonSpawn(boolean)}.
     */
    @Test
    public void testSetDragonSpawn() {
        s.setDragonSpawn(true);
        assertTrue(s.isDragonSpawn());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setRemoveMobsWhitelist(java.util.Set)}.
     */
    @Test
    public void testSetRemoveMobsWhitelist() {
        Set<EntityType> wl = Collections.emptySet();
        s.setRemoveMobsWhitelist(wl);
        assertEquals(wl, s.getRemoveMobsWhitelist());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setWorldFlags(java.util.Map)}.
     */
    @Test
    public void testSetWorldFlags() {
        Map<String, Boolean> worldFlags = Collections.emptyMap();
        s.setWorldFlags(worldFlags);
        assertEquals(worldFlags, s.getWorldFlags());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setHiddenFlags(java.util.List)}.
     */
    @Test
    public void testSetVisibleSettings() {
        List<String> visibleSettings = Collections.emptyList();
        s.setHiddenFlags(visibleSettings);
        assertEquals(visibleSettings, s.getHiddenFlags());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setVisitorBannedCommands(java.util.List)}.
     */
    @Test
    public void testSetVisitorBannedCommands() {
        List<String> visitorBannedCommands = Collections.emptyList();
        s.setVisitorBannedCommands(visitorBannedCommands);
        assertEquals(visitorBannedCommands, s.getVisitorBannedCommands());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setMaxTeamSize(int)}.
     */
    @Test
    public void testSetMaxTeamSize() {
        s.setMaxTeamSize(123);
        assertEquals(123, s.getMaxTeamSize());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setMaxHomes(int)}.
     */
    @Test
    public void testSetMaxHomes() {
        s.setMaxHomes(123);
        assertEquals(123, s.getMaxHomes());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setResetLimit(int)}.
     */
    @Test
    public void testSetResetLimit() {
        s.setResetLimit(123);
        assertEquals(123, s.getResetLimit());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setLeaversLoseReset(boolean)}.
     */
    @Test
    public void testSetLeaversLoseReset() {
        s.setLeaversLoseReset(true);
        assertTrue(s.isLeaversLoseReset());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setKickedKeepInventory(boolean)}.
     */
    @Test
    public void testSetKickedKeepInventory() {
        s.setKickedKeepInventory(true);
        assertTrue(s.isKickedKeepInventory());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setOnJoinResetMoney(boolean)}.
     */
    @Test
    public void testSetOnJoinResetMoney() {
        s.setOnJoinResetMoney(true);
        assertTrue(s.isOnJoinResetMoney());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setOnJoinResetInventory(boolean)}.
     */
    @Test
    public void testSetOnJoinResetInventory() {
        s.setOnJoinResetInventory(true);
        assertTrue(s.isOnJoinResetInventory());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setOnJoinResetEnderChest(boolean)}.
     */
    @Test
    public void testSetOnJoinResetEnderChest() {
        s.setOnJoinResetEnderChest(true);
        assertTrue(s.isOnJoinResetEnderChest());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setOnLeaveResetMoney(boolean)}.
     */
    @Test
    public void testSetOnLeaveResetMoney() {
        s.setOnLeaveResetMoney(true);
        assertTrue(s.isOnLeaveResetMoney());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setOnLeaveResetInventory(boolean)}.
     */
    @Test
    public void testSetOnLeaveResetInventory() {
        s.setOnLeaveResetInventory(true);
        assertTrue(s.isOnLeaveResetInventory());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setOnLeaveResetEnderChest(boolean)}.
     */
    @Test
    public void testSetOnLeaveResetEnderChest() {
        s.setOnLeaveResetEnderChest(true);
        assertTrue(s.isOnLeaveResetEnderChest());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setDeathsCounted(boolean)}.
     */
    @Test
    public void testSetDeathsCounted() {
        s.setDeathsCounted(true);
        assertTrue(s.isDeathsCounted());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setDeathsMax(int)}.
     */
    @Test
    public void testSetDeathsMax() {
        s.setDeathsMax(123);
        assertEquals(123, s.getDeathsMax());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setTeamJoinDeathReset(boolean)}.
     */
    @Test
    public void testSetTeamJoinDeathReset() {
        s.setTeamJoinDeathReset(true);
        assertTrue(s.isTeamJoinDeathReset());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setGeoLimitSettings(java.util.List)}.
     */
    @Test
    public void testSetGeoLimitSettings() {
        List<String> geoLimitSettings = Collections.emptyList();
        s.setGeoLimitSettings(geoLimitSettings);
        assertEquals(geoLimitSettings, s.getGeoLimitSettings());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setIvSettings(java.util.List)}.
     */
    @Test
    public void testSetIvSettings() {
        List<String> ivSettings = Collections.emptyList();
        s.setIvSettings(ivSettings);
        assertEquals(ivSettings, s.getIvSettings());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setAllowSetHomeInNether(boolean)}.
     */
    @Test
    public void testSetAllowSetHomeInNether() {
        s.setAllowSetHomeInNether(true);
        assertTrue(s.isAllowSetHomeInNether());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setAllowSetHomeInTheEnd(boolean)}.
     */
    @Test
    public void testSetAllowSetHomeInTheEnd() {
        s.setAllowSetHomeInTheEnd(true);
        assertTrue(s.isAllowSetHomeInTheEnd());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setRequireConfirmationToSetHomeInNether(boolean)}.
     */
    @Test
    public void testSetRequireConfirmationToSetHomeInNether() {
        s.setRequireConfirmationToSetHomeInNether(true);
        assertTrue(s.isRequireConfirmationToSetHomeInNether());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setRequireConfirmationToSetHomeInTheEnd(boolean)}.
     */
    @Test
    public void testSetRequireConfirmationToSetHomeInTheEnd() {
        s.setRequireConfirmationToSetHomeInTheEnd(true);
        assertTrue(s.isRequireConfirmationToSetHomeInTheEnd());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setResetEpoch(long)}.
     */
    @Test
    public void testSetResetEpoch() {
        s.setResetEpoch(123);
        assertEquals(123, s.getResetEpoch());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#getPermissionPrefix()}.
     */
    @Test
    public void testGetPermissionPrefix() {
        assertEquals("factories", s.getPermissionPrefix());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#isWaterUnsafe()}.
     */
    @Test
    public void testIsWaterUnsafe() {
        assertFalse(s.isWaterUnsafe());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setDefaultBiome(org.bukkit.block.Biome)}.
     */
    @Test
    public void testSetDefaultBiome() {
        s.setDefaultBiome(Biome.BADLANDS);
        assertEquals(Biome.BADLANDS, s.getDefaultBiome());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#setBanLimit(int)}.
     */
    @Test
    public void testSetBanLimit() {
        s.setBanLimit(123);
        assertEquals(123, s.getBanLimit());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#getIslandCommand()}.
     */
    @Test
    public void testGetIslandCommand() {
        s.setPlayerCommandAliases("island");
        assertEquals("island", s.getPlayerCommandAliases());
    }

    /**
     * Test method for {@link world.bentobox.bskyblock.Settings#getAdminCommand()}.
     */
    @Test
    public void testGetAdminCommand() {
        s.setAdminCommandAliases("admin");
        assertEquals("admin", s.getAdminCommandAliases());
    }

}
