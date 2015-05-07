package org.freemars.random.event;

import org.freerealm.player.SettlementRelatedMessage;

/**
 * @author Deniz ARIKAN
 */
public class FreeColonistsMessage extends SettlementRelatedMessage {

    private int newColonists;

    @Override
    public StringBuffer getText() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Colonists funding their own transit have arrived in ");
        stringBuffer.append(getSettlement().getName());
        stringBuffer.append(" increasing population by ");
        stringBuffer.append(newColonists);
        stringBuffer.append(".");
        return stringBuffer;
    }

    public int getNewColonists() {
        return newColonists;
    }

    public void setNewColonists(int newColonists) {
        this.newColonists = newColonists;
    }
}
