package org.freemars.random.event;

import org.freerealm.player.SettlementRelatedMessage;

/**
 * @author Deniz ARIKAN
 */
public class ColonistsReturningToEarthMessage extends SettlementRelatedMessage {

    private int leavingColonists;

    @Override
    public StringBuffer getText() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Colonists losing hope in Mars colonization have left ");
        stringBuffer.append(getSettlement().getName());
        stringBuffer.append(" decreasing population by ");
        stringBuffer.append(leavingColonists);
        stringBuffer.append(".");
        return stringBuffer;
    }

    public int getLeavingColonists() {
        return leavingColonists;
    }

    public void setLeavingColonists(int leavingColonists) {
        this.leavingColonists = leavingColonists;
    }
}
