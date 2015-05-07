package org.freemars.earth.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.controller.FreeMarsController;
import org.freemars.earth.EarthFlightModel;
import org.freemars.earth.ui.EarthDialog;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.AddUnitToContainerCommand;
import org.freerealm.unit.Unit;
import org.freerealm.unit.UnitContainer;

/**
 *
 * @author Deniz ARIKAN
 */
public class AddUnitToContainerAction extends AbstractAction {

    private FreeMarsController freeMarsController;
    private EarthDialog earthDialog;
    private UnitContainer unitContainer;
    private Unit unit;

    public AddUnitToContainerAction(FreeMarsController freeMarsController, EarthDialog earthDialog, UnitContainer unitContainer, Unit unit) {
        this.earthDialog = earthDialog;
        this.freeMarsController = freeMarsController;
        this.unitContainer = unitContainer;
        this.unit = unit;
    }

    public void actionPerformed(ActionEvent e) {
        CommandResult commandResult = freeMarsController.execute(new AddUnitToContainerCommand(unitContainer, unit));
        if (commandResult.getCode() == CommandResult.RESULT_OK) {
            freeMarsController.getFreeMarsModel().getEarthFlightModel().removeUnitLocation(unit);
            earthDialog.addUnitInfoText("\"" + unit.getName() + "\" has boarded \"" + unitContainer.getName() + "\"\n\n");
        } else {
            earthDialog.addUnitInfoText(commandResult.getText() + "\n\n");
        }
        earthDialog.update(EarthFlightModel.UNIT_RELOCATION_UPDATE);
    }
}
