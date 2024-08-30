package dev.viaduct.factories.resources;

import lombok.Getter;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public abstract class Resource {

    private final String name;
    private final double incrementAmount;
    private final List<Material> validMaterialsList;

    protected Resource(String name, double incrementAmount) {
        this.name = name;
        this.incrementAmount = incrementAmount;
        this.validMaterialsList = new ArrayList<>();
    }

    protected Resource(String name, double incrementAmount, Material... materials) {
        this.name = name;
        this.incrementAmount = incrementAmount;
        this.validMaterialsList = Arrays.stream(materials).toList();
    }

    public boolean isValidMaterial(Material material) {
        return validMaterialsList.contains(material);
    }

    public abstract String getFormattedName();

}
