package org.freemars.message;

import org.freerealm.player.SettlementRelatedMessage;

/**
 *
 * @author Deniz ARIKAN
 */
public class NewColonyFoundedMessage extends SettlementRelatedMessage {

    @Override
    public StringBuffer getText() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Colony of " + getSettlement().getName() + " founded.");
        return stringBuffer;
    }
}
