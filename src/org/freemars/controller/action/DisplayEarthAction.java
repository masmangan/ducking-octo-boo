package org.freemars.controller.action;

import org.freemars.controller.FreeMarsController;
import org.freemars.earth.ui.EarthDialog;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author Deniz ARIKAN
 */
public class DisplayEarthAction extends AbstractAction {

    private FreeMarsController controller;

    public DisplayEarthAction(FreeMarsController controller) {
        super("Earth");
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        EarthDialog earthDialog = new EarthDialog(controller);
        earthDialog.display();
//        controller.refresh();
    }
}
