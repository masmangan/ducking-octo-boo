package org.freemars.earth.support;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.controller.FreeMarsController;
import org.freemars.player.FreeMarsPlayer;
import org.freemars.ui.util.FreeMarsOptionPane;
import org.freerealm.executor.command.AddUnitCommand;
import org.freerealm.executor.command.WealthAddCommand;
import org.freerealm.settlement.Settlement;
import org.freerealm.unit.Unit;
import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public class AddTransporterToColonyAction extends AbstractAction {

    private FreeMarsController freeMarsController;
    private FreeTransporterDialog freeTransporterDialog;
    private Settlement colony;

    public AddTransporterToColonyAction(FreeMarsController freeMarsController, FreeTransporterDialog freeTransporterDialog, Settlement colony) {
        this.freeMarsController = freeMarsController;
        this.freeTransporterDialog = freeTransporterDialog;
        this.colony = colony;
    }

    public void actionPerformed(ActionEvent e) {
        FreeMarsPlayer activePlayer = (FreeMarsPlayer) freeMarsController.getFreeMarsModel().getActivePlayer();
        UnitType unitType = freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Transporter");
        Unit unit = new Unit(freeMarsController.getFreeMarsModel().getRealm(), unitType, colony.getCoordinate(), activePlayer);
        freeMarsController.execute(new AddUnitCommand(freeMarsController.getFreeMarsModel().getRealm(), activePlayer, unit));
        freeMarsController.execute(new WealthAddCommand(activePlayer, 720));
        activePlayer.setReceivedFreeTransporter(true);
        FreeMarsOptionPane.showMessageDialog(freeTransporterDialog, "Your new transporter has been transferred to " + colony.getName(), "Transporter added");
        freeTransporterDialog.dispose();
    }
}
