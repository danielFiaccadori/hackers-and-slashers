package net.dndats.hackersandslashers.common.combat.critical.manager;

import net.dndats.hackersandslashers.common.combat.critical.interfaces.ICritical;
import net.dndats.hackersandslashers.common.combat.critical.interfaces.ICriticalLogic;

public class MeleeCritical implements ICritical {

    /*
      This class is an abstraction layer for the creation of melee critical types.
      To create a new Crit, instantiate a new RangedCritical, providing a name and the logic as arguments.
    */

    private final String NAME;
    private final ICriticalLogic LOGIC;

    public MeleeCritical(String name, ICriticalLogic logic) {
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
