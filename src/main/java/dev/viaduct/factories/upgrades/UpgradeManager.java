package dev.viaduct.factories.upgrades;

import dev.viaduct.factories.actions.impl.ChatMessageAction;
import dev.viaduct.factories.actions.impl.PlaySoundAction;
import dev.viaduct.factories.upgrades.impl.LandLevelledUpgrade;
import lombok.Getter;
import org.bukkit.Sound;

import java.util.HashMap;
import java.util.Map;

@Getter
public class UpgradeManager {
    //  Default level-up sound and player message for successful upgrades.
    public static final PlaySoundAction UPGRADE_SOUND = new PlaySoundAction(Sound.ENTITY_PLAYER_LEVELUP);
    public static final ChatMessageAction UPGRADE_MSG = new ChatMessageAction("Upgrade successful!");

    public enum UpgradeName {
        LAND_SIZE_UPGRADE
    }

    private final Map<UpgradeName, Upgrade> upgradeMap;

    public UpgradeManager() {
        upgradeMap = new HashMap<>();
    }

    public Upgrade getUpgrade(UpgradeName upgradeName) {
        return upgradeMap.get(upgradeName);
    }

    public void init() {
        upgradeMap.put(UpgradeName.LAND_SIZE_UPGRADE,
                new LandLevelledUpgrade());
    }

}
