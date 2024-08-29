package dev.viaduct.factories.registries;

import java.util.HashMap;
import java.util.Map;

public class RegistryManager {

    private final Map<Class<? extends Registry<?, ?>>, Registry<?, ?>> registryMap;

    public RegistryManager() {
        this.registryMap = new HashMap<>();
    }

    public <T, R extends Registry<T, ?>> void registerRegistry(Class<R> registryClass, R registry) {
        this.registryMap.put(registryClass, registry);
    }

    public <T, R extends Registry<T, ?>> R getRegistry(Class<R> registryClass) {
        return (R) this.registryMap.get(registryClass);
    }

}
