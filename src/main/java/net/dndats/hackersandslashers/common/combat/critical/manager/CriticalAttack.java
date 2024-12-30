package net.dndats.hackersandslashers.common.combat.critical.manager;

public class CriticalAttack {

    /*
      This class is a base for the creation of Critical Attacks
      To create a new Crit, instantiate a new CriticalAttack, passing the name and the logic as arguments.
    */

    private final String NAME;
    private final ICriticalLogic LOGIC;

    public CriticalAttack(String name, ICriticalLogic logic) {
        NAME = name;
        LOGIC = logic;
    }

    public String getName() {
        return NAME;
    }

    public ICriticalLogic getLogic() {
        return LOGIC;
    }

}
