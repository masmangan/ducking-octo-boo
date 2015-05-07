package org.freemars.controller;

import java.util.Iterator;
import org.freemars.controller.action.DisplayVictoryDialogAction;
import org.freemars.player.FreeMarsPlayer;
import org.freerealm.executor.CommandResult;
import org.freerealm.player.Player;

/**
 * @author Deniz ARIKAN
 */
public class NewTurnHandler implements ControllerUpdateHandler {

    public void handleUpdate(FreeMarsController freeMarsController, CommandResult commandResult) {
        freeMarsController.getAutosaveManager().manageAutosave();
        Iterator<Player> iterator = freeMarsController.getFreeMarsModel().getPlayersIterator();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            FreeMarsPlayer freeMarsPlayer = (FreeMarsPlayer) player;
            if (!freeMarsPlayer.isContinuingGameAfterVictory() && freeMarsController.getFreeMarsModel().hasCompletedObjectives(freeMarsPlayer)) {
                new DisplayVictoryDialogAction(freeMarsController).actionPerformed(null);
            }
        }
    }
}
