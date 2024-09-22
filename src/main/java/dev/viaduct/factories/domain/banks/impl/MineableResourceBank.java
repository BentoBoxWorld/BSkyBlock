package dev.viaduct.factories.domain.banks.impl;

import dev.viaduct.factories.domain.banks.Bank;
import dev.viaduct.factories.resources.Resource;
import dev.viaduct.factories.resources.mineable.MineableResource;
import lombok.Getter;
import org.bukkit.Material;

import java.util.Optional;

@Getter
public class MineableResourceBank extends Bank {

    public MineableResourceBank() { super(); }

    public boolean isResourceMaterial(Material material) {
        return resourceMap.keySet().stream().map(resource -> (MineableResource)resource)
                .anyMatch(resource -> resource.isValidMaterial(material));
    }

    public Optional<MineableResource> getResourceByMaterial(Material material) {
        return resourceMap.keySet().stream().map(resource -> (MineableResource)resource)
                .filter(resource -> resource.isValidMaterial(material))
                .findAny();
    }
}
