package dev.viaduct.factories.generators.manual_generators;

import dev.viaduct.factories.generators.Generator;
import me.ogali.customdrops.api.CustomDropsAPI;
import me.ogali.customdrops.drops.domain.Drop;

public abstract class AbstractGenerator implements Generator {

    private final String id;

    public AbstractGenerator(String id, Drop customDropsDrop) {
        this.id = id;
        CustomDropsAPI.getInstance().createDrop(customDropsDrop);
    }

    @Override
    public String getId() {
        return id;
    }

}
