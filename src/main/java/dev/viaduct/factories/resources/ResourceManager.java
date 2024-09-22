package dev.viaduct.factories.resources;

import dev.viaduct.factories.resources.currency.impl.Credit;
import dev.viaduct.factories.resources.mineable.MineableResource;
import dev.viaduct.factories.resources.mineable.impl.Stone;
import dev.viaduct.factories.resources.mineable.impl.Wood;
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
        registerResource(new Credit());

    }

    public boolean isResourceMaterial(Material material) {
        //  get stream of resources in set.
        return resourceSet.stream().filter(resource -> resource instanceof MineableResource)
                .map(resource -> (MineableResource)resource)
                //    for every valid material of each resource...
                .map(MineableResource::getValidMaterialsList)
                //    check if it contains parameter material.
                .anyMatch(materialsList -> materialsList.contains(material));
    }

}
