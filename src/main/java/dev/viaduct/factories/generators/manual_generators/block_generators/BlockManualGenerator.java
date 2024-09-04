package dev.viaduct.factories.generators.manual_generators.block_generators;

import dev.viaduct.factories.generators.manual_generators.ManualGenerator;
import lombok.Getter;
import me.ogali.customdrops.api.CustomDropsAPI;
import me.ogali.customdrops.drops.impl.BlockDrop;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@Getter
public abstract class BlockManualGenerator extends ManualGenerator {

    protected final Material generatingMaterial;
    private final Consumer<BlockBreakEvent> blockBreakEventConsumer;

    public BlockManualGenerator(String id, Material generatingMaterial) {
        super(id, new BlockDrop(new ItemStack(generatingMaterial), id));
        this.generatingMaterial = generatingMaterial;
        this.blockBreakEventConsumer = event -> {
            event.setCancelled(true);

            if (event.getBlock().getType() != generatingMaterial) return;

            event.getBlock().setType(Material.BEDROCK);
            drop(event);
        };
    }

    public void handleBlockBreak(BlockBreakEvent event) {
        blockBreakEventConsumer.accept(event);
    }

    @Override
    public void drop(Event event) {
        CustomDropsAPI.getInstance()
                .dropCustomDropsWithoutRegionCheck(event,
                        getCustomDropsDrop());
    }

}
