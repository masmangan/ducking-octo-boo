package org.freemars.earth.message;

import java.util.Vector;
import org.freerealm.player.DefaultMessage;
import org.freerealm.unit.Unit;

/**
 * @author Deniz ARIKAN
 */
public class UnitsSeizedMessage extends DefaultMessage {

    private Vector<Unit> seizedUnits;

    @Override
    public StringBuffer getText() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Our units have been seized by Earth government.");
        return stringBuffer;
    }

    public void setSeizedUnits(Vector<Unit> seizedUnits) {
        this.seizedUnits = seizedUnits;
    }

    public Vector<Unit> getSeizedUnits() {
        return seizedUnits;
    }
}
