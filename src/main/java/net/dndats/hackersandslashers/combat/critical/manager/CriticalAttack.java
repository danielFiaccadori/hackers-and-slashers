package net.dndats.hackersandslashers.combat.critical.manager;

public class CriticalAttack {

    private final String NAME;
    private final CriticalLogic LOGIC;

    public CriticalAttack(String name, CriticalLogic logic) {
        NAME = name;
        LOGIC = logic;
    }

    public String getName() {
        return NAME;
    }

    public CriticalLogic getLogic() {
        return LOGIC;
    }

}
