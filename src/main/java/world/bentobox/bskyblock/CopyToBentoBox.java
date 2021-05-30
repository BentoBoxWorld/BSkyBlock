package world.bentobox.bskyblock;

import org.bukkit.plugin.java.JavaPlugin;

import world.bentobox.bentobox.BentoBox;

public class CopyToBentoBox extends JavaPlugin {

    @Override
    public void onEnable() {
        BentoBox.getInstance().getAddonsManager().registerAddon(this, new BSkyBlock());

    }
}
