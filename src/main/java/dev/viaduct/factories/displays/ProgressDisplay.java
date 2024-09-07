package dev.viaduct.factories.displays;

import dev.viaduct.factories.events.ProgressDisplayCompletionEvent;
import dev.viaduct.factories.generators.manual_generators.block_generators.impl.BlockManualGenerator;
import dev.viaduct.factories.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ProgressDisplay {

    private final BlockManualGenerator generator;
    private final Material displayMaterial;
    private final boolean glowing;
    private final boolean despawnOnCompletion;
    private final Color glowColor;

    private final float progressPerSecond;
    private final Map<Location, DisplayProgress> displayProgressMap;

    public ProgressDisplay(BlockManualGenerator generator, long regenerationTime, TimeUnit timeUnit, boolean glowing,
                           boolean despawnOnCompletion, Color glowColor) {
        this.generator = generator;
        this.displayMaterial = generator.getGeneratingMaterial();
        this.glowing = glowing;
        this.despawnOnCompletion = despawnOnCompletion;
        this.glowColor = glowColor;
        this.displayProgressMap = new HashMap<>();

        this.progressPerSecond = (1.0f / (timeUnit.toSeconds(regenerationTime))) / 2;
    }

    public void spawnDisplay(Location location) {
        if (location.getWorld() == null) return;

        // Now spawn the display at the calculated location
        location.getWorld().spawn(location, BlockDisplay.class, blockDisplay -> {
            blockDisplay.setBlock(displayMaterial.createBlockData());
            blockDisplay.setGlowColorOverride(glowColor);
            blockDisplay.setGlowing(glowing);

            // No rotation applied here, the block will scale based on the player's direction
            Transformation transformation = new Transformation(
                    new Vector3f(-0.005f, 0, -0.005f), // Initial position
                    new Quaternionf(),
                    new Vector3f(1.01f, 0, 1.01f), // Initial scaling
                    new Quaternionf()
            );
            blockDisplay.setTransformation(transformation);

            displayProgressMap.put(location, new DisplayProgress(blockDisplay, 0.0f));
        });
    }

    public void incrementProgress(Location location) {
        // If the location is not in the map, add it and set the progress
        // Otherwise, increment the progress
        if (!displayProgressMap.containsKey(location)) return;

        // If the progress is 100%
        if (displayProgressMap.get(location).getProgress() + progressPerSecond >= 1.0f) {
            // Call the event
            Bukkit.getPluginManager().callEvent(new ProgressDisplayCompletionEvent(generator, location));

            // Update the display with the final progress
            updateDisplayWithProgress(displayProgressMap.get(location).getDisplay());

            if (!despawnOnCompletion) return;

            // Despawn the display
            displayProgressMap.get(location).getDisplay().remove();
            displayProgressMap.remove(location);

            return;
        }

        // Increment the progress
        updateDisplayWithProgress(displayProgressMap.get(location).getDisplay());
        displayProgressMap.get(location).setProgress(displayProgressMap.get(location).getProgress() + progressPerSecond);
    }

    public void despawnDisplay(Location location) {
        if (!displayProgressMap.containsKey(location)) return;

        displayProgressMap.get(location).getDisplay().remove();
        displayProgressMap.remove(location);
    }

    private void updateDisplayWithProgress(Display display) {
        Transformation currentTransformation = display.getTransformation();
        Vector3f currentScale = currentTransformation.getScale();

        Transformation transformation = new Transformation(
                new Vector3f(-0.005f, 0, -0.005f), // Initial position
                new Quaternionf(), // Keep the current rotation
                new Vector3f(1.01f, currentScale.y + progressPerSecond, 1.01f), // Adjust scaling along X or Z
                new Quaternionf() // No additional rotation
        );

        display.setTransformation(transformation);
    }

}