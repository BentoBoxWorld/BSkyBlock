package dev.viaduct.factories.generators.manual_generators.block_generators.impl;

import dev.viaduct.factories.displays.ProgressDisplay;
import dev.viaduct.factories.events.ProgressDisplayCompletionEvent;
import dev.viaduct.factories.generators.items.GeneratorPlaceItem;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class OakWoodGenerator extends BlockManualGenerator {

    public OakWoodGenerator(String id) {
        super(id, Material.OAK_WOOD, 10, TimeUnit.SECONDS,
                true, true, Color.RED);
    }

    @Override
    public String getFormattedName() {
        return "#a8996fOak Wood Generator";
    }

}
