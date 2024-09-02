package dev.viaduct.factories.resources;

import dev.viaduct.factories.resources.impl.Stone;
import dev.viaduct.factories.resources.impl.Wood;
import lombok.Getter;
import org.bukkit.Material;

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

    public boolean isResourceMaterial(Material material) {
        //  get stream of resources in set.
        return resourceSet.stream()
                //    for every valid material of each resource...
                .map(Resource::getValidMaterialsList)
                //    check if it contains parameter material.
                .anyMatch(materialsList -> materialsList.contains(material));
    }

}
