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

    protected Resource(String name, double incrementAmount) {
        this.name = name;
        this.incrementAmount = incrementAmount;
    }

    protected Resource(String name, double incrementAmount, Material... materials) {
        this.name = name;
        this.incrementAmount = incrementAmount;
    }

    public abstract String getFormattedName();

}
