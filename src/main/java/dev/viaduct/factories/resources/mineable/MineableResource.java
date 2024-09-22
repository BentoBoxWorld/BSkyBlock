package dev.viaduct.factories.resources.mineable;

import dev.viaduct.factories.resources.Resource;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
public class MineableResource extends Resource {

    private final List<Material> validMaterialsList;

    public MineableResource(String name, double incrementAmount, List<Material> validMaterialsList) {
        super(name, incrementAmount);
        this.validMaterialsList = validMaterialsList;
    }

    public boolean isValidMaterial(Material material) {
        return validMaterialsList.contains(material);
    }

    @Override
    public String getFormattedName() {
        return "";
    }
}
