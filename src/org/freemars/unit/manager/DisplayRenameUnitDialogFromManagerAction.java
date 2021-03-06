package org.freemars.unit.manager;

import org.freemars.controller.FreeMarsController;
import java.awt.event.ActionEvent;
import org.freemars.controller.action.DisplayRenameUnitDialogAction;
import org.freemars.unit.UnitsTableHelper;
import org.freerealm.unit.Unit;

/**
 *
 * @author Deniz ARIKAN
 */
public class DisplayRenameUnitDialogFromManagerAction extends DisplayRenameUnitDialogAction {

    private FreeMarsController freeMarsController;
    private UnitManagerDialog unitManagerDialog;

    public DisplayRenameUnitDialogFromManagerAction(FreeMarsController freeMarsController, UnitManagerDialog unitManagerDialog, Unit unit) {
        super(freeMarsController, unit);
        this.freeMarsController = freeMarsController;
        this.unitManagerDialog = unitManagerDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        new UnitsTableHelper(freeMarsController, unitManagerDialog).updateUnitsTable();
    }
}
