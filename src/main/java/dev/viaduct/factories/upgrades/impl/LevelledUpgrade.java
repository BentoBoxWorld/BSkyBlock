package dev.viaduct.factories.upgrades.impl;

import dev.viaduct.factories.conditions.AbstractCondition;
import dev.viaduct.factories.conditions.ConditionHolder;
import dev.viaduct.factories.domain.players.FactoryPlayer;
import dev.viaduct.factories.upgrades.Upgrade;
import dev.viaduct.factories.utils.Chat;
import lombok.Getter;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class LevelledUpgrade implements Upgrade {

    private final Material icon;
    private final String displayName;
    private final List<String> description;

    @Getter
    private final ConditionHolder conditionHolder;

    public LevelledUpgrade(Material icon, String displayName, List<String> description, AbstractCondition... abstractConditions) {
        this.icon = icon;
        this.displayName = displayName;
        this.description = new ArrayList<>(description);
        this.conditionHolder = new ConditionHolder(abstractConditions);
    }

    @Override
    public void tryUpgrade(FactoryPlayer factoryPlayer) {
        if (!conditionHolder.allConditionsMet(factoryPlayer)) return;
        conditionHolder.executeActions(factoryPlayer);
    }

    @Override
    public Material getIcon() {
        return icon;
    }

    @Override
    public String getDisplayName() {
        return Chat.colorize(displayName);
    }

    @Override
    public List<String> getDescription() {
        return description;
    }

}
