package org.freemars.editor.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.editor.MapInformationDialog;

/**
 *
 * @author Deniz ARIKAN
 */
public class DisplayMapInformationAction extends AbstractAction {

    private EditorController controller;

    public DisplayMapInformationAction(EditorController controller) {
        super("Display map information", null);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        MapInformationDialog dialog = new MapInformationDialog(controller.getEditorFrame(), controller.getEditorModel().getRealm().getMap());
        dialog.display();
    }
}
