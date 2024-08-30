package dev.viaduct.factories.resources;

import dev.viaduct.factories.resources.impl.Stone;
import dev.viaduct.factories.resources.impl.Wood;
import lombok.Getter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Getter
public class ResourceManager {

    private final Set<Resource> resourceSet;

    public ResourceManager() {
        this.resourceSet = new HashSet<>();
    }

    public void registerResource(Resource resource) {
        resourceSet.add(resource);
    }

    public Optional<Resource> getResource(String name) {
        return resourceSet.stream()
                .filter(resource -> resource.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public void registerResources() {
        registerResource(new Wood());
        registerResource(new Stone());
    }

}
