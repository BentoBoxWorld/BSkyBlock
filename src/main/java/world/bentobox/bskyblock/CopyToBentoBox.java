package world.bentobox.bskyblock;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.Files;

import world.bentobox.bentobox.api.events.BentoBoxReadyEvent;

public class CopyToBentoBox extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBentoBoxReady(BentoBoxReadyEvent e) {
        getLogger().severe("BSkyBlock.jar must be in the BentoBox/addons folder! Trying to move it there...");
        File addons = new File(getFile().getParent(), "BentoBox/addons");
        if (addons.exists()) {
            File to = new File(addons, getFile().getName());
            if (!to.exists()) {

                try {
                    Files.move(getFile(), to);
                    getLogger().severe(getFile().getName() + " moved successfully. Restart server now to activate!");

                } catch (IOException ex) {
                    getLogger().severe("Failed to move it. " + ex.getMessage());
                    getLogger().severe("Move " + getFile().getName() + " manually into the BentoBox/addons folder. Then restart server.");
                }
            }
        } else {
            getLogger().severe("BentoBox folders do not exist! " + addons.getAbsolutePath());
        }

    }
}
