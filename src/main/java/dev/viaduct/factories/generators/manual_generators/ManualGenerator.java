package dev.viaduct.factories.generators.manual_generators;

import dev.viaduct.factories.generators.Generator;
import me.ogali.customdrops.api.CustomDropsAPI;
import me.ogali.customdrops.drops.domain.Drop;

public abstract class ManualGenerator implements Generator {

    private final String id;
    private final Drop customDropsDrop;

    public ManualGenerator(String id, Drop customDropsDrop) {
        this.id = id;
        this.customDropsDrop = customDropsDrop;
        CustomDropsAPI.getInstance().createDrop(customDropsDrop);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Drop getCustomDropsDrop() {
        return customDropsDrop;
    }

}
