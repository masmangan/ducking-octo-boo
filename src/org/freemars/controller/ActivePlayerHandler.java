package org.freemars.controller;

import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.freemars.player.FreeMarsPlayer;
import org.freemars.ai.AIPlayer;
import org.freemars.ai.ExpeditionaryForcePlayer;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.SignalPlayerEndTurnCommand;
import org.freerealm.player.Player;
import org.freerealm.unit.Unit;

/**
 *
 * @author Deniz ARIKAN
 */
public class ActivePlayerHandler implements ControllerUpdateHandler {

    public void handleUpdate(FreeMarsController freeMarsController, CommandResult commandResult) {
        FreeMarsPlayer activePlayer = freeMarsController.getFreeMarsModel().getActivePlayer();
        if (activePlayer.getStatus() != Player.STATUS_REMOVED) {
            if (activePlayer instanceof AIPlayer) {
                new FreeMarsAIPlayerActivatedHandler((AIPlayer) activePlayer).handleUpdate(freeMarsController, commandResult);
            } else if (activePlayer instanceof ExpeditionaryForcePlayer) {
                new ExpeditionaryForcePlayerActivatedHandler((ExpeditionaryForcePlayer) activePlayer).handleUpdate(freeMarsController, commandResult);
            } else {
                new HumanPlayerActivatedHandler(activePlayer).handleUpdate(freeMarsController, commandResult);
            }
        } else {
            String logInfo = "Status of player with id ";
            logInfo = logInfo + activePlayer.getId() + " and name \"" + activePlayer.getName() + "\" is \"removed\". Auto skipping turn.";
            Logger.getLogger(ActivePlayerHandler.class).info(logInfo);
            freeMarsController.execute(new SignalPlayerEndTurnCommand(activePlayer));
        }
    }

    protected void manageAutomatedUnits(Player player) {
    	
    	//concerta o concurrentError que acontencida quando era usado iterators
        FreeMarsPlayer mplayer = (FreeMarsPlayer)player;
    	for(Map.Entry<Integer, Unit> entry : mplayer.getUnitManager().getUnits().entrySet()) {
    		Unit playerUnit = entry.getValue();
            if (playerUnit.getAutomater() != null) {
                playerUnit.getAutomater().automate();
            }
    	}

    }
}
