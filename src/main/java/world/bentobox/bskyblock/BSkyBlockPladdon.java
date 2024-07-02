package world.bentobox.bskyblock;


import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.addons.GameModeAddon;
import world.bentobox.bentobox.api.addons.Pladdon;


public class BSkyBlockPladdon extends Pladdon {
    
    private GameModeAddon addon; 
    
    @Override
    public Addon getAddon() {
        if (addon == null) {
            addon = new BSkyBlock();
        }
        return addon;
    }
}
