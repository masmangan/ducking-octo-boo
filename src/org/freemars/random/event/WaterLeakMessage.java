package org.freemars.random.event;

import org.freerealm.player.SettlementRelatedMessage;
import java.text.DecimalFormat;

/**
 * @author Deniz ARIKAN
 */
public class WaterLeakMessage extends SettlementRelatedMessage {

    private int amountLost;

    @Override
    public StringBuffer getText() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("A leak causes the loss of ");
        stringBuffer.append(new DecimalFormat().format(getAmountLost()));
        stringBuffer.append(" water in ");
        stringBuffer.append(getSettlement().getName());
        return stringBuffer;
    }

    public int getAmountLost() {
        return amountLost;
    }

    public void setAmountLost(int amountLost) {
        this.amountLost = amountLost;
    }
}
