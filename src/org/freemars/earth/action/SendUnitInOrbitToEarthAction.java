package org.freemars.earth.action;

import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.controller.FreeMarsController;
import org.freemars.earth.EarthFlightProperty;
import org.freerealm.executor.command.UnitOrderAddCommand;
import org.freerealm.unit.Unit;
import org.freemars.earth.Location;
import org.freemars.ui.util.FreeMarsOptionPane;
import org.freemars.earth.order.RelocateUnitOrder;
import org.freemars.earth.ui.OrbitDialog;
import org.freemars.player.FreeMarsPlayer;

/**
 *
 * @author Deniz ARIKAN
 */
public class SendUnitInOrbitToEarthAction extends AbstractAction {

    private FreeMarsController freeMarsController;
    private OrbitDialog orbitDialog;
    private Unit unit;

    public SendUnitInOrbitToEarthAction(FreeMarsController freeMarsController, OrbitDialog orbitDialog, Unit unit) {
        this.freeMarsController = freeMarsController;
        this.orbitDialog = orbitDialog;
        this.unit = unit;
    }

    public void actionPerformed(ActionEvent e) {
        FreeMarsPlayer player = (FreeMarsPlayer) unit.getPlayer();
        if (player.hasDeclaredIndependence()) {
            FreeMarsOptionPane.showMessageDialog(freeMarsController.getCurrentFrame(), "We can not send spaceships to Earth after declaration of independence", "Rejected");
        } else {
            EarthFlightProperty earthFlight = (EarthFlightProperty) unit.getType().getProperty("EarthFlight");
            if (earthFlight != null) {
                int turnGiven = freeMarsController.getFreeMarsModel().getRealm().getNumberOfTurns();
                RelocateUnitOrder relocateUnitOrder = new RelocateUnitOrder(freeMarsController.getFreeMarsModel().getRealm());
                relocateUnitOrder.setFreeMarsController(freeMarsController);
                relocateUnitOrder.setUnit(unit);
                relocateUnitOrder.setTurnGiven(turnGiven);
                relocateUnitOrder.setSource(Location.MARS_ORBIT);
                relocateUnitOrder.setDestination(Location.EARTH);
                freeMarsController.execute(new UnitOrderAddCommand(freeMarsController.getFreeMarsModel().getRealm(), unit, relocateUnitOrder));
                freeMarsController.getFreeMarsModel().getEarthFlightModel().addUnitLocation(unit, Location.TRAVELING_TO_EARTH);
                orbitDialog.update();
                Window windowFocusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
                FreeMarsOptionPane.showMessageDialog(windowFocusOwner, unit.getType().getName() + " is sent to Earth");
            }
        }
    }
}
