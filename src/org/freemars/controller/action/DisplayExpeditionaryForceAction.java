package org.freemars.controller.action;

import org.freemars.controller.FreeMarsController;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.earth.action.DisplayExpeditionaryForceDefeatedDialogAction;
import org.freemars.earth.ui.ExpeditionaryForceDialog;
import org.freemars.player.FreeMarsPlayer;
import org.freemars.ai.ExpeditionaryForcePlayer;
import org.freerealm.player.Player;

/**
 *
 * @author Deniz ARIKAN
 */
public class DisplayExpeditionaryForceAction extends AbstractAction {

    private FreeMarsController controller;

    public DisplayExpeditionaryForceAction(FreeMarsController controller) {
        super("Expeditionary force");
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        FreeMarsPlayer activePlayer = (FreeMarsPlayer) controller.getFreeMarsModel().getActivePlayer();
        ExpeditionaryForcePlayer expeditionaryForcePlayer = controller.getFreeMarsModel().getRelatedExpeditionaryForcePlayer(activePlayer);
        if (expeditionaryForcePlayer.getStatus() == Player.STATUS_REMOVED) {
            new DisplayExpeditionaryForceDefeatedDialogAction(controller).actionPerformed(e);
        } else {
            ExpeditionaryForceDialog expeditionaryForceDialog = new ExpeditionaryForceDialog(controller.getCurrentFrame(), activePlayer, expeditionaryForcePlayer);
            expeditionaryForceDialog.setCloseAction(new CloseFreeMarsDialogAction(expeditionaryForceDialog));
            expeditionaryForceDialog.display();
        }
    }
}
