package org.freemars.earth.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.controller.FreeMarsController;
import org.freemars.earth.EarthFlightProperty;
import org.freerealm.settlement.Settlement;
import org.freerealm.executor.command.UnitOrderAddCommand;
import org.freerealm.unit.Unit;
import org.freemars.earth.Location;
import org.freemars.earth.order.RelocateUnitOrder;
import org.freemars.earth.ui.OrbitDialog;
import org.freerealm.settlement.improvement.SettlementImprovementType;

/**
 *
 * @author Deniz ARIKAN
 */
public class LandUnitInOrbitToMarsColonyAction extends AbstractAction {

    private FreeMarsController freeMarsController;
    private OrbitDialog orbitDialog;
    private Unit unit;
    private Settlement settlement;

    public LandUnitInOrbitToMarsColonyAction(FreeMarsController freeMarsController, OrbitDialog orbitDialog, Unit unit, Settlement settlement) {
        this.freeMarsController = freeMarsController;
        this.orbitDialog = orbitDialog;
        this.unit = unit;
        this.settlement = settlement;
    }

    public void actionPerformed(ActionEvent e) {
        EarthFlightProperty earthFlight = (EarthFlightProperty) unit.getType().getProperty("EarthFlight");
        SettlementImprovementType starPortImprovementType = freeMarsController.getFreeMarsModel().getRealm().getSettlementImprovementManager().getImprovement("Starport");
        if (settlement.hasImprovementType(starPortImprovementType)) {
            if (earthFlight != null) {
                int turnGiven = freeMarsController.getFreeMarsModel().getNumberOfTurns();
                RelocateUnitOrder relocateUnitOrder = new RelocateUnitOrder(freeMarsController.getFreeMarsModel().getRealm());
                relocateUnitOrder.setFreeMarsController(freeMarsController);
                relocateUnitOrder.setUnit(unit);
//                relocateUnitOrder.setTurnGiven(turnGiven);
                relocateUnitOrder.setSource(Location.MARS_ORBIT);
                relocateUnitOrder.setDestination(Location.MARS);
                relocateUnitOrder.setLandOnColony(settlement);
                freeMarsController.execute(new UnitOrderAddCommand(freeMarsController.getFreeMarsModel().getRealm(), unit, relocateUnitOrder));
                orbitDialog.update();
            }
        }
    }
}
