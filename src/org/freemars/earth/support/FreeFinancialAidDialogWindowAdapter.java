package org.freemars.earth.support;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.freemars.controller.FreeMarsController;
import org.freemars.player.FreeMarsPlayer;
import org.freerealm.executor.command.WealthAddCommand;

/**
 *
 * @author Deniz ARIKAN
 */
public class FreeFinancialAidDialogWindowAdapter extends WindowAdapter {

    private FreeMarsController freeMarsController;

    public FreeFinancialAidDialogWindowAdapter(FreeMarsController freeMarsController) {
        this.freeMarsController = freeMarsController;
    }

    @Override
    public void windowClosed(WindowEvent windowEvent) {
        FreeMarsPlayer activePlayer = (FreeMarsPlayer) freeMarsController.getFreeMarsModel().getActivePlayer();
        if (activePlayer != null) {
            freeMarsController.execute(new WealthAddCommand(activePlayer, 20000));
            activePlayer.setReceivedFreeFinancialAid(true);
        }
    }
}
