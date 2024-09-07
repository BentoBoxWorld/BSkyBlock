package dev.viaduct.factories.utils;

import org.bukkit.Material;

public class MaterialUtils {

    public static boolean isStrippable(Material material) {
        String materialName = material.name();

        if (materialName.contains("stripped")) return false;
        return materialName
                .toLowerCase()
                .contains("wood");
    }

}
