package org.freemars.controller.action;

import org.freemars.controller.FreeMarsController;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freerealm.executor.command.SetActiveUnitCommand;
import org.freerealm.unit.Unit;

/**
 *
 * @author Deniz ARIKAN
 */
public class ActivateUnitAction extends AbstractAction {

    private FreeMarsController controller;
    private Unit unit;

    public ActivateUnitAction(FreeMarsController controller, Unit unit) {
        super("Activate unit");
        this.controller = controller;
        this.unit = unit;
    }

    public void actionPerformed(ActionEvent e) {
        controller.execute(new SetActiveUnitCommand(controller.getFreeMarsModel().getActivePlayer(), unit));
    }
}
