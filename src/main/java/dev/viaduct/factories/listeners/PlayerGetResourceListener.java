package dev.viaduct.factories.listeners;

import dev.viaduct.factories.banks.Bank;
import dev.viaduct.factories.players.FactoryPlayer;
import dev.viaduct.factories.registries.FactoryPlayerRegistry;
import dev.viaduct.factories.resources.ResourceManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerGetResourceListener implements Listener {

    private final FactoryPlayerRegistry factoryPlayerRegistry;

    public PlayerGetResourceListener(FactoryPlayerRegistry factoryPlayerRegistry) {
        this.factoryPlayerRegistry = factoryPlayerRegistry;
    }

    @EventHandler
    public void onResourceBreak(BlockBreakEvent event) {
        Block brokenBlock = event.getBlock();
        //  Get the material from broken block.
        Material material = brokenBlock.getType();

        //  Check to see if the event player's bank already contains an
        //  amount of the resource material. If so, increment the amount.
        factoryPlayerRegistry.get(event.getPlayer().getUniqueId())
                .ifPresent(factoryPlayer -> {
                    Bank factoryPlayerBank = factoryPlayer.getBank();
                    factoryPlayerBank.getResourceByMaterial(material)
                            .ifPresent(resource -> factoryPlayerBank.addToResource(resource, 1));
                });

        //  TODO: Update Factory Player Scoreboard

    }
}
