package org.freemars.controller;

import org.freemars.message.EarthTaxRateChangedMessage;
import org.freerealm.executor.CommandResult;

/**
 *
 * @author Deniz ARIKAN
 */
public class EarthTaxRateChangedHandler implements ControllerUpdateHandler {

    public void handleUpdate(FreeMarsController controller, CommandResult commandResult) {
        EarthTaxRateChangedMessage earthTaxRateChangedMessage = new EarthTaxRateChangedMessage();
        earthTaxRateChangedMessage.setTurnSent(controller.getFreeMarsModel().getNumberOfTurns());
        earthTaxRateChangedMessage.setSubject("Earth tax rate changed");
        byte previousTaxRate = (Byte) commandResult.getParameter("previousTaxRate");
        byte newTaxRate = (Byte) commandResult.getParameter("newTaxRate");
        StringBuffer message = new StringBuffer();
        if (newTaxRate > previousTaxRate) {
            message.append("Earth tax rate increased to " + newTaxRate + "%");
        } else {
            message.append("Earth tax rate decreased to " + newTaxRate + "%");
        }
        earthTaxRateChangedMessage.setText(message);
        controller.getFreeMarsModel().getActivePlayer().addMessage(earthTaxRateChangedMessage);
    }
}
