package world.bentobox.bskyblock;


import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.addons.Pladdon;


public class BSkyBlockPladdon extends Pladdon {

    @Override
    public Addon getAddon() {
        return new BSkyBlock();
    }
}
