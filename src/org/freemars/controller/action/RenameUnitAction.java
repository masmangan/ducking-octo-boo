package org.freemars.controller.action;

import org.freemars.controller.FreeMarsController;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.ui.unit.RenameUnitDialog;
import org.freemars.ui.util.FreeMarsOptionPane;
import org.freerealm.unit.Unit;
import org.freerealm.executor.command.SetUnitNameCommand;

/**
 *
 * @author Deniz ARIKAN
 */
public class RenameUnitAction extends AbstractAction {

    private FreeMarsController controller;
    private RenameUnitDialog renameUnitDialog;
    private Unit unit;

    public RenameUnitAction(FreeMarsController controller, Unit unit, RenameUnitDialog renameUnitDialog) {
        this.controller = controller;
        this.unit = unit;
        this.renameUnitDialog = renameUnitDialog;
    }

    public void actionPerformed(ActionEvent e) {
        String name = renameUnitDialog.getNameTextFieldValue();
        if (!name.trim().equals("")) {
            controller.execute(new SetUnitNameCommand(unit, name));
            renameUnitDialog.dispose();
        } else {
            FreeMarsOptionPane.showMessageDialog(renameUnitDialog, "Unit name cannot be empty", "Error");
            renameUnitDialog.setNameTextFieldValue(unit.getName());
            renameUnitDialog.setNameTextFieldFocused();
            renameUnitDialog.setNameTextFieldSelected();
        }
    }
}
