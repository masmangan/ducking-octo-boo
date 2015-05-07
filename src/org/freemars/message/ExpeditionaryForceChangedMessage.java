package org.freemars.message;

import java.util.HashMap;
import org.freerealm.player.DefaultMessage;
import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public class ExpeditionaryForceChangedMessage extends DefaultMessage {

    private HashMap<UnitType, Integer> updatedUnits;

    public HashMap<UnitType, Integer> getUpdatedUnits() {
        return updatedUnits;
    }

    public void setUpdatedUnits(HashMap<UnitType, Integer> updatedUnits) {
        this.updatedUnits = updatedUnits;
    }
}
