package org.freemars.colonydialog.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.colonydialog.ColonyDialogModel;
import org.freemars.controller.FreeMarsController;
import org.freerealm.Realm;
import org.freerealm.executor.command.RemoveUnitFromContainerCommand;
import org.freerealm.executor.command.UnitActivateCommand;
import org.freerealm.unit.Unit;

/**
 *
 * @author arikande
 */
public class UnloadUnitToColonyAction extends AbstractAction {

    private FreeMarsController freeMarsController;
    private ColonyDialogModel colonyDialogModel;
    private Unit containerUnit;
    private Unit unit;

    public UnloadUnitToColonyAction(FreeMarsController freeMarsController, ColonyDialogModel colonyDialogModel, Unit containerUnit, Unit unit) {
        this.freeMarsController = freeMarsController;
        this.colonyDialogModel = colonyDialogModel;
        this.containerUnit = containerUnit;
        this.unit = unit;
    }

    public void actionPerformed(ActionEvent e) {
        freeMarsController.execute(new RemoveUnitFromContainerCommand(containerUnit, unit));
        Realm realm = freeMarsController.getFreeMarsModel().getRealm();
        freeMarsController.execute(new UnitActivateCommand(realm, unit, containerUnit.getCoordinate()));
        colonyDialogModel.refresh(ColonyDialogModel.COLONY_UNITS_UPDATE);
    }
}
