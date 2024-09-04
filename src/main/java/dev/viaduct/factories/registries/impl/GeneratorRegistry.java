package dev.viaduct.factories.registries.impl;

import dev.viaduct.factories.FactoriesPlugin;
import dev.viaduct.factories.generators.Generator;
import dev.viaduct.factories.generators.manual_generators.block_generators.BlockManualGenerator;
import dev.viaduct.factories.generators.manual_generators.block_generators.OakWoodGenerator;
import dev.viaduct.factories.registries.Registry;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

public class GeneratorRegistry extends Registry<String, Generator> {


    public static final NamespacedKey GENERATOR_ID_KEY = new NamespacedKey(FactoriesPlugin.getInstance(), "generator_id");

    public void initialize() {
        BlockManualGenerator oakWoodGenerator = new OakWoodGenerator("oak_wood_generator");

        register(oakWoodGenerator.getId(), oakWoodGenerator);
    }

    public Optional<Generator> getGeneratorFromPlacedItem(ItemStack itemStack) {
        if (itemStack.getItemMeta() == null) return Optional.empty();
        if (itemStack.getItemMeta().getPersistentDataContainer().isEmpty()) return Optional.empty();
        return get(itemStack.getItemMeta().getPersistentDataContainer().get(GENERATOR_ID_KEY,
                PersistentDataType.STRING));
    }

}