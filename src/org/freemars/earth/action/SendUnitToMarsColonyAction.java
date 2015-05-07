package org.freemars.earth.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.controller.FreeMarsController;
import org.freemars.earth.EarthFlightProperty;
import org.freerealm.settlement.Settlement;
import org.freerealm.executor.command.UnitOrderAddCommand;
import org.freerealm.unit.Unit;
import org.freemars.earth.Location;
import org.freemars.earth.EarthFlightModel;
import org.freemars.earth.order.RelocateUnitOrder;
import org.freemars.earth.ui.EarthDialog;

/**
 *
 * @author Deniz ARIKAN
 */
public class SendUnitToMarsColonyAction extends AbstractAction {

    private FreeMarsController freeMarsController;
    private EarthDialog earthDialog;
    private Settlement settlement;

    public SendUnitToMarsColonyAction(FreeMarsController freeMarsController, EarthDialog earthDialog, Settlement settlement) {
        this.freeMarsController = freeMarsController;
        this.earthDialog = earthDialog;
        this.settlement = settlement;
    }

    public void actionPerformed(ActionEvent e) {
        Unit unit = freeMarsController.getFreeMarsModel().getEarthFlightModel().getEarthDialogSelectedUnit();
        EarthFlightProperty earthFlight = (EarthFlightProperty) unit.getType().getProperty("EarthFlight");
        if (earthFlight != null) {
            int turnGiven = 0;
            RelocateUnitOrder relocateUnitOrder = new RelocateUnitOrder(freeMarsController.getFreeMarsModel().getRealm());
            relocateUnitOrder.setFreeMarsController(freeMarsController);
            relocateUnitOrder.setUnit(unit);
            relocateUnitOrder.setTurnGiven(turnGiven);
            relocateUnitOrder.setSource(Location.EARTH);
            relocateUnitOrder.setDestination(Location.MARS);
            relocateUnitOrder.setLandOnColony(settlement);
            freeMarsController.execute(new UnitOrderAddCommand(freeMarsController.getFreeMarsModel().getRealm(), unit, relocateUnitOrder));
            freeMarsController.getFreeMarsModel().getEarthFlightModel().addUnitLocation(unit, Location.TRAVELING_TO_MARS);
            earthDialog.addUnitInfoText(unit.getName() + " is sent to Martian colony of " + settlement.getName() + "\n\n");
            earthDialog.update(EarthFlightModel.UNIT_RELOCATION_UPDATE);
        }
    }
}
