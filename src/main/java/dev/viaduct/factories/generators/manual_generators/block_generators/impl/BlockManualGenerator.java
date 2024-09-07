package dev.viaduct.factories.generators.manual_generators.block_generators.impl;

import dev.viaduct.factories.displays.ProgressDisplay;
import dev.viaduct.factories.domain.players.FactoryPlayer;
import dev.viaduct.factories.generators.items.GeneratorPlaceItem;
import dev.viaduct.factories.generators.manual_generators.block_generators.BlockGenerator;
import dev.viaduct.factories.utils.MaterialUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public abstract class BlockManualGenerator extends BlockGenerator {

    private final ProgressDisplay progressDisplay;

    public BlockManualGenerator(String id, Material generatingMaterial, long progressTime, TimeUnit progressTimeUnit,
                                boolean glowing, boolean despawnOnCompletion, Color glowColor) {
        super(id, generatingMaterial);
        this.progressDisplay = new ProgressDisplay(this, progressTime, progressTimeUnit,
                glowing, despawnOnCompletion, glowColor);
    }

    @Override
    public ItemStack getGeneratorPlaceItem() {
        return new GeneratorPlaceItem(this)
                .getFinalItem();
    }

    @Override
    public Consumer<PlayerInteractEvent> getInteractConsumer(FactoryPlayer factoryPlayer) {
        return event -> {
            if (event.getAction() != Action.LEFT_CLICK_BLOCK
                    && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

            assert event.getClickedBlock() != null;

            // Checks if the block is strippable and the item in the player's hand is an axe
            boolean isStrippable = MaterialUtils.isStrippable(event.getClickedBlock().getType());

            if (event.getItem() != null &&
                    event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                    event.getItem().getType().name().contains("_AXE") &&
                    isStrippable) {
                event.setCancelled(true);
                return;
            }

            if (event.getAction() == Action.LEFT_CLICK_BLOCK &&
                    event.getPlayer().isSneaking() &&
                    event.getItem() == null &&
                    event.getClickedBlock().getType() != generatingMaterial) {
                event.setCancelled(true);
                Block clickedBlock = event.getClickedBlock();
                Location location = clickedBlock.getLocation();

                clickedBlock.setType(Material.AIR);

                assert location.getWorld() != null;

                location.getWorld().dropItem(location, getGeneratorPlaceItem());
                factoryPlayer.getGeneratorHolder().removeGenerator(location);
                getProgressDisplay().despawnDisplay(location);
                return;
            }

            if (event.getClickedBlock().getType() == generatingMaterial) return;
            event.setCancelled(true);
            getProgressDisplay().incrementProgress(event.getClickedBlock().getLocation());
        };
    }

    @Override
    public ProgressDisplay getProgressDisplay() {
        return progressDisplay;
    }

    @Override
    public void generate(Location location) {
        location.getBlock().setType(generatingMaterial);
    }

}