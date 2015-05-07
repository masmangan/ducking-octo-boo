package org.freemars.colonydialog.workforce;

import org.freerealm.settlement.workforce.WorkForce;
import org.freerealm.executor.command.WorkforceAssignCommand;
import org.freerealm.executor.command.WorkforceRemoveCommand;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.freemars.colony.RemoveFertilizerFromTileCommand;
import org.freemars.colonydialog.ColonyDialogModel;
import org.freemars.controller.FreeMarsController;
import org.freerealm.Realm;

/**
 *
 * @author Deniz ARIKAN
 */
public class WorkForceManagementPopupListener implements PopupMenuListener {

    private FreeMarsController freeMarsController;
    private ColonyDialogModel colonyDialogModel;
    private WorkForceManagementPopupModel workForceManagementPopupModel;

    public WorkForceManagementPopupListener(FreeMarsController freeMarsController, ColonyDialogModel colonyDialogModel, WorkForceManagementPopupModel workForceManagementPopupModel) {
        this.freeMarsController = freeMarsController;
        this.colonyDialogModel = colonyDialogModel;
        this.workForceManagementPopupModel = workForceManagementPopupModel;
    }

    public void popupMenuCanceled(PopupMenuEvent e) {
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        WorkForceManagementPopup popup = (WorkForceManagementPopup) e.getSource();
        if (workForceManagementPopupModel.getSelectedResource() != null) {
            if (workForceManagementPopupModel.getSelectedNumberOfWorkers() > 0) {
                WorkForce workForce = new WorkForce();
                workForce.setCoordinate(workForceManagementPopupModel.getCoordinate());
                workForce.setNumberOfWorkers(workForceManagementPopupModel.getSelectedNumberOfWorkers());
                workForce.setResource(workForceManagementPopupModel.getSelectedResource());
                Realm realm = freeMarsController.getFreeMarsModel().getRealm();
                freeMarsController.execute(new WorkforceAssignCommand(realm, workForceManagementPopupModel.getColony(), workForce, workForceManagementPopupModel.getCoordinate()));
                colonyDialogModel.refresh(ColonyDialogModel.WORKFORCE_UPDATE);
                colonyDialogModel.refresh(ColonyDialogModel.COLONY_RESOURCES_UPDATE);
            } else {
                freeMarsController.execute(new WorkforceRemoveCommand(workForceManagementPopupModel.getColony(), workForceManagementPopupModel.getCoordinate()));
                freeMarsController.execute(new RemoveFertilizerFromTileCommand(freeMarsController.getFreeMarsModel().getRealm(), workForceManagementPopupModel.getColony(), workForceManagementPopupModel.getCoordinate(), true));
                colonyDialogModel.refresh(ColonyDialogModel.WORKFORCE_UPDATE);
                colonyDialogModel.refresh(ColonyDialogModel.COLONY_RESOURCES_UPDATE);
            }
        }
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
    }
}
