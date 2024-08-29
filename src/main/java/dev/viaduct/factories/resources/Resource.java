package dev.viaduct.factories.resources;

import lombok.Getter;

@Getter
public abstract class Resource {

    private final String name;

    protected Resource(String name) {
        this.name = name;
    }

}
