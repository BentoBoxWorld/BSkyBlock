package dev.viaduct.factories.conditions.impl;

import dev.viaduct.factories.FactoriesPlugin;
import dev.viaduct.factories.actions.Action;
import dev.viaduct.factories.conditions.AbstractCondition;
import dev.viaduct.factories.domain.players.FactoryPlayer;
import dev.viaduct.factories.resources.Resource;

public class ResourceCondition extends AbstractCondition {

    private final Resource resource;
    private final double amount;

    public ResourceCondition(String resourceName, double amount, Action... actions) {
        super(actions);
        this.resource = FactoriesPlugin.getInstance()
                .getResourceManager()
                .getResource(resourceName)
                .get();
        this.amount = amount;
    }

    @Override
    public boolean isMet(FactoryPlayer factoryPlayer) {
        double playerResourceAmount = factoryPlayer.getResourceBank()
                .getResourceAmt(resource);

        return playerResourceAmount >= amount;
    }

    @Override
    public String toString() {
        return resource.getFormattedName() + amount + "x";
    }

}