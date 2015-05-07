package org.freemars.earth.action;

import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import org.freemars.controller.FreeMarsController;
import org.freemars.earth.ui.UnitsSeizedDialog;
import org.freerealm.unit.Unit;

/**
 *
 * @author Deniz ARIKAN
 */
public class DisplayUnitsSeizedDialogAction extends AbstractAction {

    private FreeMarsController controller;
    private Vector<Unit> seizedUnits;

    public DisplayUnitsSeizedDialogAction(FreeMarsController controller) {
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        UnitsSeizedDialog unitsSeizedDialog = new UnitsSeizedDialog(controller.getCurrentFrame(), seizedUnits);
        unitsSeizedDialog.display();
    }

    public void setSeizedUnits(Vector<Unit> seizedUnits) {
        this.seizedUnits = seizedUnits;
    }
}
