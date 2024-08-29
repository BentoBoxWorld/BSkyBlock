package dev.viaduct.factories.banks;

import dev.viaduct.factories.resources.Resource;

import java.util.HashMap;
import java.util.Map;

public class Bank {

    private final Map<Resource, Double> resourceMap;

    public Bank() {
        resourceMap = new HashMap<>();
    }

    public void addToResource(Resource resource, double amount) {
        resourceMap.put(resource, resourceMap.getOrDefault(resource, 0.0) + amount);
    }

    public void removeFromResource(Resource resource, double amount) {
        resourceMap.put(resource, resourceMap.getOrDefault(resource, 0.0) - amount);
    }

}
