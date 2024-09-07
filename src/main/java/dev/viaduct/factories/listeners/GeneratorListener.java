package dev.viaduct.factories.listeners;

import dev.viaduct.factories.events.ProgressDisplayCompletionEvent;
import dev.viaduct.factories.generators.manual_generators.block_generators.impl.BlockManualGenerator;
import dev.viaduct.factories.registries.impl.FactoryPlayerRegistry;
import dev.viaduct.factories.registries.impl.GeneratorRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
                        .ifPresent(factoryPlayer -> generator.getPlaceConsumer(factoryPlayer).accept(event)));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        factoryPlayerRegistry.get(event.getPlayer().getUniqueId())
                .flatMap(factoryPlayer -> factoryPlayer.getGeneratorHolder().getGenerator(event.getBlock().getLocation()))
                .ifPresent(generator -> generator.getBreakConsumer().accept(event));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        factoryPlayerRegistry.get(event.getPlayer().getUniqueId())
                .ifPresent(factoryPlayer -> factoryPlayer.getGeneratorHolder().getGenerator(event.getClickedBlock().getLocation())
                        .ifPresent(generator -> generator.getInteractConsumer(factoryPlayer).accept(event)));
    }

    @EventHandler
    public void onDisplayComplete(ProgressDisplayCompletionEvent event) {
        if (!(event.getGenerator() instanceof BlockManualGenerator blockManualGenerator)) return;
        blockManualGenerator.getDisplayCompletionConsumer().accept(event);
    }

}