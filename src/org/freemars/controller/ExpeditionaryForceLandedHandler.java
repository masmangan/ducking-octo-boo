package org.freemars.controller;

import java.util.HashMap;
import org.freemars.message.ExpeditionaryForceLandedMessage;
import org.freerealm.executor.CommandResult;
import org.freerealm.player.Player;
import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public class ExpeditionaryForceLandedHandler implements ControllerUpdateHandler {

    public void handleUpdate(FreeMarsController controller, CommandResult commandResult) {
        HashMap<UnitType, Integer> landedUnits = (HashMap<UnitType, Integer>) commandResult.getParameter("landedUnits");
        ExpeditionaryForceLandedMessage expeditionaryForceLandedMessage = new ExpeditionaryForceLandedMessage();
        expeditionaryForceLandedMessage.setTurnSent(controller.getFreeMarsModel().getNumberOfTurns());
        expeditionaryForceLandedMessage.setSubject("Expeditionary force landed");
        StringBuffer message = new StringBuffer();
        int attackWave = (Integer) commandResult.getParameter("attackWave");
        switch (attackWave) {
            case 1:
                message.append("First wave of the expeditionary force has landed");
                break;
            case 2:
                message.append("Second wave of the expeditionary force has landed");
                break;
            case 3:
                message.append("Final wave of the expeditionary force has landed");
                break;
        }
        expeditionaryForceLandedMessage.setText(message);
        expeditionaryForceLandedMessage.setAttackWave(attackWave);
        expeditionaryForceLandedMessage.setLandedUnits(landedUnits);
        Player player = (Player) commandResult.getParameter("targetPlayer");
        player.addMessage(expeditionaryForceLandedMessage);
    }
}
