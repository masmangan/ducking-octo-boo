package org.freemars.controller.action;

import org.freemars.controller.FreeMarsController;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.unit.automater.EngineerAutomater;
import org.freemars.unit.automater.ScoutAutomater;
import org.freerealm.executor.command.SkipUnitCommand;
import org.freerealm.executor.command.UnitSetAutomaterCommand;
import org.freerealm.player.Player;
import org.freerealm.unit.Unit;
import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public class AutomateUnitAction extends AbstractAction {

    private FreeMarsController freeMarsController;
    private Unit unit;

    public AutomateUnitAction(FreeMarsController freeMarsController, Unit unit) {
        super("Automate", null);
        this.freeMarsController = freeMarsController;
        this.unit = unit;
    }

    public void actionPerformed(ActionEvent e) {
        Unit unitToOrder = unit != null ? unit : freeMarsController.getFreeMarsModel().getActivePlayer().getActiveUnit();
        if (unitToOrder != null) {
            if (unitToOrder.getAutomater() == null) {
                UnitType scoutType = freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Scout");
                UnitType engineerType = freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Engineer");
                UnitSetAutomaterCommand unitSetAutomaterCommand = null;
                if (unitToOrder.getType().equals(engineerType)) {
                    EngineerAutomater engineerAutomater = new EngineerAutomater();
                    engineerAutomater.setFreeMarsController(freeMarsController);
                    unitSetAutomaterCommand = new UnitSetAutomaterCommand(unitToOrder, engineerAutomater);
                } else if (unitToOrder.getType().equals(scoutType)) {
                    ScoutAutomater scoutAutomater = new ScoutAutomater();
                    scoutAutomater.setFreeMarsController(freeMarsController);
                    unitSetAutomaterCommand = new UnitSetAutomaterCommand(unitToOrder, scoutAutomater);
                }
                if (unitSetAutomaterCommand != null) {
                    freeMarsController.execute(unitSetAutomaterCommand);
                    freeMarsController.execute(new SkipUnitCommand(unitToOrder));
                }
            } else {
                UnitSetAutomaterCommand unitSetAutomaterCommand = new UnitSetAutomaterCommand(unitToOrder, null);
                freeMarsController.execute(unitSetAutomaterCommand);
            }
        }
    }

    public boolean checkEnabled() {
        Player activePlayer = freeMarsController.getFreeMarsModel().getActivePlayer();
        if (activePlayer == null) {
            return false;
        }
        Unit unitToOrder = unit != null ? unit : freeMarsController.getFreeMarsModel().getActivePlayer().getActiveUnit();
        if (unitToOrder == null) {
            return false;
        }
        UnitType engineerType = freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Engineer");
        UnitType scoutType = freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Scout");
        return unitToOrder.getType().equals(engineerType) || unitToOrder.getType().equals(scoutType);
    }
}
