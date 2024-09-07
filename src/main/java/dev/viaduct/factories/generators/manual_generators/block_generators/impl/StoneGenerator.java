package dev.viaduct.factories.generators.manual_generators.block_generators.impl;

import org.bukkit.Color;
import org.bukkit.Material;

import java.util.concurrent.TimeUnit;

public class StoneGenerator extends BlockManualGenerator {

    public StoneGenerator(String id) {
        super(id, Material.STONE, 15, TimeUnit.SECONDS, true,
                true, Color.GREEN);
    }

    @Override
    public String getFormattedName() {
        return "#bfbfbfStone Generator";
    }

}
