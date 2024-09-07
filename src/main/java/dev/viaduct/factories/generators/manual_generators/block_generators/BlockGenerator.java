package dev.viaduct.factories.generators.manual_generators.block_generators;

import dev.viaduct.factories.domain.players.FactoryPlayer;
import dev.viaduct.factories.events.ProgressDisplayCompletionEvent;
import dev.viaduct.factories.generators.manual_generators.AbstractGenerator;
import dev.viaduct.factories.utils.Chat;
import lombok.Getter;
import me.ogali.customdrops.api.CustomDropsAPI;
import me.ogali.customdrops.drops.domain.Drop;
import me.ogali.customdrops.drops.impl.BlockDrop;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Particle;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@Getter
public abstract class BlockGenerator extends AbstractGenerator {

    protected final Material generatingMaterial;

    public BlockGenerator(String id, Material generatingMaterial) {
        super(id, new BlockDrop(new ItemStack(generatingMaterial), id));
        this.generatingMaterial = generatingMaterial;
    }

    @Override
    public Consumer<BlockPlaceEvent> getPlaceConsumer(FactoryPlayer factoryPlayer) {
        return event -> {
            factoryPlayer.addGenerator(event.getBlock().getLocation(),
                    this);

            event.getBlock().setType(Material.BEDROCK);
            getProgressDisplay().spawnDisplay(event.getBlock().getLocation());

            Chat.tell(factoryPlayer.getPlayer(), "&eClick block to generate.");
            event.getPlayer().playNote(event.getBlock().getLocation(), Instrument.BIT, new Note(7));
        };
    }

    @Override
    public Consumer<BlockBreakEvent> getBreakConsumer() {
        return event -> {
            event.setCancelled(true);

            if (event.getBlock().getType() != generatingMaterial) return;

            drop(event);
            event.getBlock().setType(Material.BEDROCK);
            getProgressDisplay().spawnDisplay(event.getBlock().getLocation());
        };
    }

    @Override
    public Consumer<ProgressDisplayCompletionEvent> getDisplayCompletionConsumer() {
        return event -> {
            event.getDisplayLocation().getBlock().setType(generatingMaterial);
            event.getDisplayLocation().getWorld().spawnParticle(Particle.VILLAGER_ANGRY,
                    event.getDisplayLocation(), 10);
        };
    }

    @Override
    public void drop(Event event) {
        Drop dropById = CustomDropsAPI.getInstance()
                .getDropById(getId());
        dropById.drop(event, true);
    }

}