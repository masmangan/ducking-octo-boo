package org.freemars.earth.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.controller.FreeMarsController;
import org.freemars.earth.ui.IndependenceDeclaredDialog;

/**
 *
 * @author Deniz ARIKAN
 */
public class DisplayIndependenceDeclaredDialogAction extends AbstractAction {

    private FreeMarsController controller;

    public DisplayIndependenceDeclaredDialogAction(FreeMarsController controller) {
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        new IndependenceDeclaredDialog(controller.getCurrentFrame()).display();
    }
}
