package dev.viaduct.factories.actions.impl;

import dev.viaduct.factories.FactoriesPlugin;
import dev.viaduct.factories.actions.Action;
import dev.viaduct.factories.domain.players.FactoryPlayer;
import dev.viaduct.factories.resources.Resource;

import java.util.Optional;

public class RemoveResourceAction implements Action {

    private final String resourceName;
    private final int amount;

    public RemoveResourceAction(String resourceName, int amount) {
        this.resourceName = resourceName;
        this.amount = amount;
    }

    @Override
    public void execute(FactoryPlayer factoryPlayer) {
        Optional<Resource> resourceOptional = FactoriesPlugin.getInstance().getResourceManager()
                .getResource(resourceName);

        resourceOptional.ifPresent(resource -> factoryPlayer.getBank()
                .removeFromResource(resource, factoryPlayer.getScoreboard(), amount));
    }

}
