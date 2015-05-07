package org.freemars.random.event;

import java.text.DecimalFormat;
import org.freerealm.player.DefaultMessage;

/**
 * @author Deniz ARIKAN
 */
public class CreditDonationMessage extends DefaultMessage {

    private int creditsDonated;

    @Override
    public StringBuffer getText() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\"Mars is the FUTURE\" organization donates ");
        stringBuffer.append(new DecimalFormat().format(getCreditsDonated()));
        stringBuffer.append(" credits to our colonial government");
        return stringBuffer;
    }

    public int getCreditsDonated() {
        return creditsDonated;
    }

    public void setCreditsDonated(int creditsDonated) {
        this.creditsDonated = creditsDonated;
    }
}
