package net.dndats.hackersandslashers.common.combat.critical.manager;

import net.dndats.hackersandslashers.common.combat.critical.interfaces.ICritical;
import net.dndats.hackersandslashers.common.combat.critical.interfaces.ICriticalLogic;

public class RangedCritical implements ICritical {

    /*
      This class is a base for the creation of ranged Critical Attacks
      To create a new Crit, instantiate a new RangedCritical, passing the name and the logic as arguments.
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
