package org.freemars.player.tax;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freerealm.executor.command.SetTaxRateCommand;
import org.freemars.controller.FreeMarsController;
import org.freerealm.player.Player;

/**
 *
 * @author Deniz ARIKAN
 */
public class AcceptTaxRateAction extends AbstractAction {

    private FreeMarsController controller;
    private Player player;
    private TaxRateDialog taxRateDialog;

    public AcceptTaxRateAction(FreeMarsController controller, Player player, TaxRateDialog taxRateDialog) {
        super("Accept");
        this.controller = controller;
        this.player = player;
        this.taxRateDialog = taxRateDialog;
    }

    public void actionPerformed(ActionEvent e) {
        controller.execute(new SetTaxRateCommand(player, taxRateDialog.getTaxRateSliderValue()));
        taxRateDialog.dispose();
    }
}
