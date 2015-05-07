package org.freemars.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import org.freemars.controller.FreeMarsController;

/**
 *
 * @author Deniz ARIKAN
 */
public class DisplayMapEditorAction extends AbstractAction {

    private FreeMarsController controller;

    public DisplayMapEditorAction(FreeMarsController controller) {
        super("Map editor", null);
        putValue(SHORT_DESCRIPTION, "Map editor");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_E));
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        controller.displayEditorFrame();
    }
}
