package dev.viaduct.factories.events;

import dev.viaduct.factories.generators.Generator;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class ProgressDisplayCompletionEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Generator generator;
    private final Location displayLocation;

    public ProgressDisplayCompletionEvent(Generator generator, Location displayLocation) {
        this.generator = generator;
        this.displayLocation = displayLocation;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
