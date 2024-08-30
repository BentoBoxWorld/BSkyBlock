package dev.viaduct.factories.banks;

import dev.viaduct.factories.FactoriesPlugin;
import dev.viaduct.factories.registries.FactoryPlayerRegistry;
import dev.viaduct.factories.resources.Resource;
import lombok.Getter;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class Bank {

    private final Map<Resource, Double> resourceMap;

    public Bank() {
        resourceMap = new HashMap<>();
        FactoriesPlugin.getInstance()
                .getResourceManager()
                .getResourceSet()
                .forEach(resource -> resourceMap.put(resource, 0.0));
    }

    public void addToResource(Resource resource, double amount) {
        resourceMap.put(resource, resourceMap.getOrDefault(resource, 0.0) + amount);
    }

    public void removeFromResource(Resource resource, double amount) {
        resourceMap.put(resource, resourceMap.getOrDefault(resource, 0.0) - amount);
    }

    public double getResourceAmt(Resource resource) {
        return resourceMap.get(resource);
    }

    public double getResourceAmt(String resourceName) {
        Optional<Resource> resourceOptional = FactoriesPlugin.getInstance()
                .getResourceManager()
                .getResource(resourceName);
        if (resourceOptional.isEmpty()) return 0;
        return resourceMap.get(resourceOptional.get());
    }

    public boolean isResourceMaterial(Material material) {
        return resourceMap.keySet().stream()
                .anyMatch(resource -> resource.isValidMaterial(material));
    }

    public Optional<Resource> getResourceByMaterial(Material material) {
        return resourceMap.keySet().stream()
                .filter(resource -> resource.isValidMaterial(material))
                .findAny();
    }

}
