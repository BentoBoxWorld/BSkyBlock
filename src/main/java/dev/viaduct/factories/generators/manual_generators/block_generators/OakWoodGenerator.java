package dev.viaduct.factories.generators.manual_generators.block_generators;

import dev.viaduct.factories.generators.items.GeneratorPlaceItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class OakWoodGenerator extends BlockManualGenerator {

    public OakWoodGenerator(String id) {
        super(id, Material.OAK_WOOD);
    }

    @Override
    public ItemStack getGeneratorPlaceItem() {
        return new GeneratorPlaceItem(this)
                .getFinalItem();
    }

    @Override
    public String getFormattedName() {
        return "#a8996fOak Wood Generator";
    }

    @Override
    public void generate(Location location) {
        location.getBlock().setType(generatingMaterial);
    }

}
