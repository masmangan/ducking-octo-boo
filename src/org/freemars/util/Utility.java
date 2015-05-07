package org.freemars.util;

import org.freemars.earth.EarthFlightProperty;
import org.freerealm.player.Player;
import org.freerealm.unit.Unit;

/**
 *
 * @author Deniz ARIKAN
 */
public class Utility {

    public static Unit getNextPlayableUnit(Player player, Unit unit) {
        Unit tryUnit = unit;
        int triedUnitCount = 0;
        while (triedUnitCount <= player.getUnitCount()) {
            tryUnit = player.getNextUnit(tryUnit);
            if (isPlayable(tryUnit)) {
                return tryUnit;
            } else {
                triedUnitCount = triedUnitCount + 1;
            }
        }
        return null;
    }

    private static boolean isPlayable(Unit unit) {
        if (unit == null) {
            return false;
        }
        if (unit.getStatus() == Unit.UNIT_SUSPENDED) {
            return false;
        }
        if (unit.getCurrentOrder() != null) {
            return false;
        }
        if (unit.getAutomater() != null) {
            return false;
        }
        EarthFlightProperty earthFlightProperty = (EarthFlightProperty) unit.getType().getProperty("EarthFlight");
        if (earthFlightProperty != null && !unit.isSkippedForCurrentTurn()) {
            return true;
        }
        if (unit.getMovementPoints() == 0) {
            return false;
        }
        return true;
    }
}
