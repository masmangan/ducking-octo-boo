package org.freemars.random.event;

import org.freerealm.player.SettlementRelatedMessage;
import java.text.DecimalFormat;

/**
 * @author Deniz ARIKAN
 */
public class FertilizerDonationMessage extends SettlementRelatedMessage {

    private int fertilizerAmountDonated;

    @Override
    public StringBuffer getText() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\"Mars is the FUTURE\" organization donates ");
        stringBuffer.append(new DecimalFormat().format(getFertilizerAmountDonated()));
        stringBuffer.append(" tons of fertilizer to ");
        stringBuffer.append(getSettlement().getName());
        return stringBuffer;
    }

    public int getFertilizerAmountDonated() {
        return fertilizerAmountDonated;
    }

    public void setFertilizerAmountDonated(int fertilizerAmountDonated) {
        this.fertilizerAmountDonated = fertilizerAmountDonated;
    }
}
