package dev.viaduct.factories.registries;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Registry<K, V> {

    protected final Map<K, V> registry = new HashMap<>();

    public void register(K key, V value) {
        registry.put(key, value);
    }

    public void unregister(K key) {
        registry.remove(key);
    }

    public Optional<V> get(K key) {
        return Optional.ofNullable(registry.get(key));
    }

    public Map<K, V> getRegistryMap() {
        return registry;
    }

    public Collection<V> getAllValues() {
        return registry.values().stream().toList();
    }

}