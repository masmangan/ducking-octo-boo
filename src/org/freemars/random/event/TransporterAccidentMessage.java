package org.freemars.random.event;

import org.freerealm.player.SettlementRelatedMessage;

/**
 * @author Deniz ARIKAN
 */
public class TransporterAccidentMessage extends SettlementRelatedMessage {

    private int colonistsLost;

    @Override
    public StringBuffer getText() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("A transporter accident in ");
        stringBuffer.append(getSettlement().getName());
        stringBuffer.append(" causes the loss of ");
        stringBuffer.append(colonistsLost);
        stringBuffer.append(" colonists");
        return stringBuffer;
    }

    public int getColonistsLost() {
        return colonistsLost;
    }

    public void setColonistsLost(int colonistsLost) {
        this.colonistsLost = colonistsLost;
    }
}
