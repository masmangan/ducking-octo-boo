package org.freemars.controller.action;

import org.freemars.controller.FreeMarsController;
import org.freerealm.executor.command.SignalPlayerEndTurnCommand;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author Deniz ARIKAN
 */
public class EndTurnAction extends AbstractAction {

    private final FreeMarsController freeMarsController;

    public EndTurnAction(FreeMarsController controller) {
        super("End turn");
        putValue(MNEMONIC_KEY, KeyEvent.VK_T);
        this.freeMarsController = controller;
    }

    public void actionPerformed(ActionEvent e) {
        freeMarsController.execute(new SignalPlayerEndTurnCommand(freeMarsController.getFreeMarsModel().getActivePlayer()));
    }
}
