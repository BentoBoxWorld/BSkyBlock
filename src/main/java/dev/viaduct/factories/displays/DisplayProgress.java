package dev.viaduct.factories.displays;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.BlockDisplay;

@Getter
public class DisplayProgress {

    private final BlockDisplay display;

    @Setter
    private float progress;

    public DisplayProgress(BlockDisplay display, float progress) {
        this.display = display;
        this.progress = progress;
    }

}