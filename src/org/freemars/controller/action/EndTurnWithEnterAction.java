package org.freemars.controller.action;

import org.freemars.controller.FreeMarsController;
import org.freerealm.executor.command.SignalPlayerEndTurnCommand;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freerealm.player.Player;

/**
 *
 * @author Deniz ARIKAN
 */
public class EndTurnWithEnterAction extends AbstractAction {

    private FreeMarsController controller;

    public EndTurnWithEnterAction(FreeMarsController controller) {
        super("End turn");
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        Player activePlayer = controller.getFreeMarsModel().getActivePlayer();
        if (activePlayer.getActiveUnit() == null) {
            controller.execute(new SignalPlayerEndTurnCommand(controller.getFreeMarsModel().getActivePlayer()));
        }
    }
}
