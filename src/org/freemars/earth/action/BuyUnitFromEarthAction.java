package org.freemars.earth.action;

import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.freemars.controller.FreeMarsController;
import org.freemars.earth.EarthFlightModel;
import org.freemars.earth.Location;
import org.freemars.earth.command.BuyUnitFromEarthCommand;
import org.freemars.earth.ui.EarthDialog;
import org.freemars.model.FreeMarsModel;
import org.freemars.player.FreeMarsPlayer;
import org.freemars.ui.util.FreeMarsOptionPane;
import org.freerealm.executor.DefaultExecutor;
import org.freerealm.executor.command.AddUnitCommand;
import org.freerealm.executor.command.WealthRemoveCommand;
import org.freerealm.map.Coordinate;
import org.freerealm.unit.Unit;
import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public class BuyUnitFromEarthAction extends AbstractAction {

    private FreeMarsController freeMarsController;
    private EarthDialog earthDialog;
    private UnitType unitType;

    public BuyUnitFromEarthAction(FreeMarsController freeMarsController, EarthDialog earthDialog, UnitType unitType) {
        this.freeMarsController = freeMarsController;
        this.earthDialog = earthDialog;
        this.unitType = unitType;
    }

    public void actionPerformed(ActionEvent e) {
        FreeMarsModel freeMarsModel = freeMarsController.getFreeMarsModel();
        FreeMarsPlayer player = (FreeMarsPlayer) freeMarsModel.getRealm().getPlayerManager().getActivePlayer();
        if (player.hasDeclaredIndependence()) {
            FreeMarsOptionPane.showMessageDialog(earthDialog, "We do not sell to rebels!", "Rejected");
        } else {
            Object[] options = {"Yes", "No, thanks"};
            int price = freeMarsModel.getEarthFlightModel().getEarthSellsAtPrice(unitType);
            String unitPrice = new DecimalFormat().format(price);
            int value = JOptionPane.showOptionDialog(earthDialog,
                    "Purchase a new " + unitType.getName() + " for " + unitPrice + "?",
                    "Purchase unit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
            if (value == JOptionPane.YES_OPTION) {
                freeMarsController.execute(new BuyUnitFromEarthCommand(freeMarsModel, player, unitType));
                earthDialog.addUnitInfoText(unitType.getName() + " has been purchased for " + unitPrice + "\n\n");
                earthDialog.update(EarthFlightModel.PURCHASE_EARTH_UNIT_UPDATE);
            }
        }
    }
}
