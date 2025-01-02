package net.dndats.hackersandslashers.common.combat.critical.interfaces;

public interface ICritical {

    /**
     * This interface provides a base for the creation of a critical type
     */

    String getName();
    ICriticalLogic getLogic();

}
