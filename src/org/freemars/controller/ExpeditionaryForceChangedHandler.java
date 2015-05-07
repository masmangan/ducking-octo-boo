package org.freemars.controller;

import java.util.HashMap;
import org.freemars.message.ExpeditionaryForceChangedMessage;
import org.freerealm.executor.CommandResult;
import org.freerealm.player.Player;
import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public class ExpeditionaryForceChangedHandler implements ControllerUpdateHandler {

    public void handleUpdate(FreeMarsController controller, CommandResult commandResult) {
        HashMap<UnitType, Integer> updatedUnits = (HashMap<UnitType, Integer>) commandResult.getParameter("updated_units");
        ExpeditionaryForceChangedMessage expeditionaryForceChangedMessage = new ExpeditionaryForceChangedMessage();
        expeditionaryForceChangedMessage.setTurnSent(controller.getFreeMarsModel().getNumberOfTurns());
        expeditionaryForceChangedMessage.setSubject("Expeditionary force changed");
        expeditionaryForceChangedMessage.setUpdatedUnits(updatedUnits);
        StringBuffer message = new StringBuffer();
        message.append("Our Earth government has updated its expeditionary force");
        expeditionaryForceChangedMessage.setText(message);
        Player targetPlayer = (Player) commandResult.getParameter("target_player");
        targetPlayer.addMessage(expeditionaryForceChangedMessage);
    }
}
