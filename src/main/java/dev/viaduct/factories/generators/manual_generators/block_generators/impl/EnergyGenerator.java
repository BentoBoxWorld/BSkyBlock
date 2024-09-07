package dev.viaduct.factories.generators.manual_generators.block_generators.impl;

import org.bukkit.Color;
import org.bukkit.Material;

import java.util.concurrent.TimeUnit;

public class EnergyGenerator extends BlockManualGenerator {

    public EnergyGenerator(String id) {
        super(id, Material.AMETHYST_BLOCK, 30, TimeUnit.SECONDS,
                true, true, Color.FUCHSIA);
    }

    @Override
    public String getFormattedName() {
        return "#9966ccEnergy Generator";
    }

}
