package dev.viaduct.factories.listeners;

import dev.viaduct.factories.generators.manual_generators.block_generators.BlockManualGenerator;
import dev.viaduct.factories.registries.impl.FactoryPlayerRegistry;
import dev.viaduct.factories.registries.impl.GeneratorRegistry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GeneratorListener implements Listener {

    private final FactoryPlayerRegistry factoryPlayerRegistry;
    private final GeneratorRegistry generatorRegistry;

    public GeneratorListener(FactoryPlayerRegistry factoryPlayerRegistry, GeneratorRegistry generatorRegistry) {
        this.factoryPlayerRegistry = factoryPlayerRegistry;
        this.generatorRegistry = generatorRegistry;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        generatorRegistry.getGeneratorFromPlacedItem(event.getItemInHand())
                .ifPresent(generator -> factoryPlayerRegistry.get(event.getPlayer().getUniqueId())
                        .ifPresent(factoryPlayer -> factoryPlayer.addGenerator(event.getBlockPlaced().getLocation(),
                                generator)));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        factoryPlayerRegistry.get(event.getPlayer().getUniqueId())
                .flatMap(factoryPlayer -> factoryPlayer.getGeneratorHolder().getGenerator(event.getBlock().getLocation()))
                .ifPresent(generator -> {
                    if (!(generator instanceof BlockManualGenerator blockManualGenerator)) return;
                    blockManualGenerator.handleBlockBreak(event);
                });
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        factoryPlayerRegistry.get(event.getPlayer().getUniqueId())
                .ifPresent(factoryPlayer -> factoryPlayer.getGeneratorHolder().getGenerator(event.getClickedBlock().getLocation())
                        .ifPresent(generator -> {
                            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                                if (!event.getPlayer().isSneaking()) return;
                                event.setCancelled(true);
                                Block clickedBlock = event.getClickedBlock();
                                Location location = clickedBlock.getLocation();


                                clickedBlock.setType(Material.AIR);
                                location.getWorld().dropItem(location, generator.getGeneratorPlaceItem());
                                factoryPlayer.getGeneratorHolder().removeGenerator(location);
                            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                event.setCancelled(true);
                                generator.generate(event.getClickedBlock().getLocation());
                            }
                        }));
    }

}