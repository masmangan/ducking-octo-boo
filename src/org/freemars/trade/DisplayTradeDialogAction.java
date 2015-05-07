package org.freemars.trade;

import org.freemars.controller.FreeMarsController;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author Deniz ARIKAN
 */
public class DisplayTradeDialogAction extends AbstractAction {

    private FreeMarsController controller;

    public DisplayTradeDialogAction(FreeMarsController controller) {
        super("Trade");
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        TradeDialog dialog = new TradeDialog(controller.getCurrentFrame(), controller.getFreeMarsModel());
        dialog.setCloseAction(new CloseTradeDialogAction(dialog));
        dialog.display();
    }
}
