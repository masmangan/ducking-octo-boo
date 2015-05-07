package org.freemars.earth.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.controller.FreeMarsController;
import org.freemars.earth.EarthFlightModel;
import org.freemars.earth.Location;
import org.freemars.earth.ui.EarthDialog;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.RemoveUnitFromContainerCommand;
import org.freerealm.unit.Unit;
import org.freerealm.unit.UnitContainer;

/**
 *
 * @author Deniz ARIKAN
 */
public class RemoveUnitFromContainerAction extends AbstractAction {

    private FreeMarsController freeMarsController;
    private EarthDialog earthDialog;
    private UnitContainer unitContainer;
    private Unit unit;

    public RemoveUnitFromContainerAction(FreeMarsController freeMarsController, EarthDialog earthDialog, UnitContainer unitContainer, Unit unit) {
        this.earthDialog = earthDialog;
        this.freeMarsController = freeMarsController;
        this.unitContainer = unitContainer;
        this.unit = unit;
    }

    public void actionPerformed(ActionEvent e) {
        CommandResult commandResult = freeMarsController.execute(new RemoveUnitFromContainerCommand(unitContainer, unit));
        if (commandResult.getCode() == CommandResult.RESULT_OK) {
            freeMarsController.getFreeMarsModel().getEarthFlightModel().addUnitLocation(unit, Location.EARTH);
            earthDialog.addUnitInfoText("\"" + unitContainer.getName() + "\" has unloaded \"" + unit.getName() + "\"\n\n");
        }
        earthDialog.update(EarthFlightModel.UNIT_RELOCATION_UPDATE);
    }
}
