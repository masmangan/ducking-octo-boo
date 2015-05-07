package org.freemars.earth.action;

import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import org.freemars.controller.FreeMarsController;
import org.freemars.earth.ui.SpaceshipsSeizedDialog;
import org.freerealm.unit.Unit;

/**
 *
 * @author Deniz ARIKAN
 */
public class DisplaySpaceshipsSeizedDialogAction extends AbstractAction {

    private FreeMarsController controller;
    private Vector<Unit> seizedUnits;

    public DisplaySpaceshipsSeizedDialogAction(FreeMarsController controller) {
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        SpaceshipsSeizedDialog spaceShipsSeizedDialog = new SpaceshipsSeizedDialog(controller.getCurrentFrame(), seizedUnits);
        spaceShipsSeizedDialog.display();
    }

    public void setSeizedUnits(Vector<Unit> seizedUnits) {
        this.seizedUnits = seizedUnits;
    }
}
