package org.freemars.colonydialog;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JList;
import javax.swing.ListModel;
import org.freemars.controller.FreeMarsController;
import org.freerealm.executor.command.AddToSettlementProductionQueueCommand;
import org.freerealm.settlement.SettlementBuildable;

/**
 *
 * @author Deniz ARIKAN
 */
public class BuildableUnitsListMouseAdapter extends MouseAdapter {

    private FreeMarsController freeMarsController;
    private ColonyDialogModel colonyDialogModel;
    private ProductionQueueManagementDialog productionQueueManagementDialog;

    public BuildableUnitsListMouseAdapter(FreeMarsController freeMarsController, ColonyDialogModel model, ProductionQueueManagementDialog productionQueueManagementDialog) {
        this.freeMarsController = freeMarsController;
        this.colonyDialogModel = model;
        this.productionQueueManagementDialog = productionQueueManagementDialog;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            JList list = (JList) e.getSource();
            int index = list.locationToIndex(e.getPoint());
            ListModel dlm = list.getModel();
            Object item = dlm.getElementAt(index);
            list.ensureIndexIsVisible(index);
            productionQueueManagementDialog.addItemToCurrentQueue(item);
            freeMarsController.execute(new AddToSettlementProductionQueueCommand(colonyDialogModel.getColony(), (SettlementBuildable) item));
            productionQueueManagementDialog.update();
            colonyDialogModel.refresh(ColonyDialogModel.CURRENT_PRODUCTION_UPDATE);
        }
    }
}
