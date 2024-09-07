package dev.viaduct.factories.generators.items;

import dev.viaduct.factories.generators.manual_generators.block_generators.impl.BlockManualGenerator;
import dev.viaduct.factories.registries.impl.GeneratorRegistry;
import dev.viaduct.factories.utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class GeneratorPlaceItem extends ItemBuilder {

    private final String generatorId;

    public GeneratorPlaceItem(BlockManualGenerator generator) {
        super(generator.getGeneratingMaterial());
        setName(generator.getFormattedName());
        glowing();

        this.generatorId = generator.getId();
    }

    public ItemStack getFinalItem() {
        ItemStack item = build();
        ItemMeta itemMeta = item.getItemMeta();

        assert itemMeta != null;
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();

        persistentDataContainer.set(GeneratorRegistry.GENERATOR_ID_KEY, PersistentDataType.STRING,
                        generatorId);

        item.setItemMeta(itemMeta);

        return item;
    }

}
