package net.dndats.hackersandslashers.api.manager;

import net.dndats.hackersandslashers.api.interfaces.ICritical;
import net.dndats.hackersandslashers.api.interfaces.ICriticalLogic;

public class RangedCritical implements ICritical {

    /**
      This class is an abstraction layer for the creation of melee critical types.
      To create a new Crit, instantiate a new RangedCritical, providing a name and the logic as arguments.
    */

    private final String NAME;
    private final ICriticalLogic LOGIC;

    public RangedCritical(String name, ICriticalLogic logic) {
        NAME = name;
        LOGIC = logic;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ICriticalLogic getLogic() {
        return LOGIC;
    }

}
