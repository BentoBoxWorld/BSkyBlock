package dev.viaduct.factories.generators;

import me.ogali.customdrops.drops.domain.Drop;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public interface Generator {

    String getId();

    ItemStack getGeneratorPlaceItem();

    String getFormattedName();

    Drop getCustomDropsDrop();

    void drop(Event event);

    void generate(Location location);

}