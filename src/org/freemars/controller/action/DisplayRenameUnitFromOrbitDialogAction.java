package org.freemars.controller.action;

import java.awt.Image;
import org.freemars.controller.FreeMarsController;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.earth.EarthFlightModel;
import org.freemars.earth.ui.OrbitDialog;
import org.freemars.ui.image.FreeMarsImageManager;
import org.freemars.ui.unit.RenameUnitDialog;
import org.freerealm.unit.Unit;

/**
 *
 * @author Deniz ARIKAN
 */
public class DisplayRenameUnitFromOrbitDialogAction extends AbstractAction {

    private FreeMarsController controller;
    private OrbitDialog orbitDialog;
    private Unit unit;

    public DisplayRenameUnitFromOrbitDialogAction(FreeMarsController controller, OrbitDialog orbitDialog, Unit unit) {
        super("Rename unit");
        this.controller = controller;
        this.orbitDialog = orbitDialog;
        this.unit = unit;
    }

    public void actionPerformed(ActionEvent e) {
        Unit unitToRename = unit != null ? unit : controller.getFreeMarsModel().getActivePlayer().getActiveUnit();
        if (unitToRename != null) {
            RenameUnitDialog renameUnitDialog = new RenameUnitDialog(controller.getCurrentFrame());
            renameUnitDialog.setCurrentUnitName(unitToRename.getName());
            Image unitImage = FreeMarsImageManager.getInstance().getImage(unitToRename);
            unitImage = FreeMarsImageManager.createResizedCopy(unitImage, 70, -1, false, renameUnitDialog);
            renameUnitDialog.setUnitImage(unitImage);
            renameUnitDialog.setConfirmButtonAction(new RenameUnitAction(controller, unitToRename, renameUnitDialog));
            renameUnitDialog.setNameTextFieldSelected();
            renameUnitDialog.display();
            orbitDialog.update();
        }
    }
}
