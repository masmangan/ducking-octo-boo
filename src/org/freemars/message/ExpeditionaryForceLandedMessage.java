package org.freemars.message;

import java.util.HashMap;
import org.freerealm.player.DefaultMessage;
import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public class ExpeditionaryForceLandedMessage extends DefaultMessage {

    private int attackWave;
    private HashMap<UnitType, Integer> landedUnits;

    public int getAttackWave() {
        return attackWave;
    }

    public void setAttackWave(int attackWave) {
        this.attackWave = attackWave;
    }

    public HashMap<UnitType, Integer> getLandedUnits() {
        return landedUnits;
    }

    public void setLandedUnits(HashMap<UnitType, Integer> landedUnits) {
        this.landedUnits = landedUnits;
    }

}
