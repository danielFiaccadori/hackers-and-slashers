package net.dndats.hackersandslashers.combat.critical.manager;

public class CriticalAttack {

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
