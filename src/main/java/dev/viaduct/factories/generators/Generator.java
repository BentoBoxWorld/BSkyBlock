package dev.viaduct.factories.generators;

import dev.viaduct.factories.displays.ProgressDisplay;
import dev.viaduct.factories.domain.players.FactoryPlayer;
import dev.viaduct.factories.events.ProgressDisplayCompletionEvent;
import me.ogali.customdrops.drops.domain.Drop;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public interface Generator {

    String getId();

    ItemStack getGeneratorPlaceItem();

    String getFormattedName();

    ProgressDisplay getProgressDisplay();

    Consumer<BlockPlaceEvent> getPlaceConsumer(FactoryPlayer factoryPlayer);

    Consumer<BlockBreakEvent> getBreakConsumer();

    Consumer<PlayerInteractEvent> getInteractConsumer(FactoryPlayer factoryPlayer);

    Consumer<ProgressDisplayCompletionEvent> getDisplayCompletionConsumer();

    void drop(Event event);

    void generate(Location location);

}