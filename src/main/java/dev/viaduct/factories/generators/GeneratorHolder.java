package dev.viaduct.factories.generators;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GeneratorHolder {

    private final Map<Location, Generator> placedGenerators = new HashMap<>();

    public void addGenerator(Location location, Generator generator) {
        placedGenerators.put(location, generator);
    }

    public void removeGenerator(Location location) {
        placedGenerators.remove(location);
    }

    public Optional<Generator> getGenerator(Location location) {
        return Optional.ofNullable(placedGenerators.get(location));
    }

}
