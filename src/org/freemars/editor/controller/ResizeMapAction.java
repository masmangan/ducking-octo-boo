package org.freemars.editor.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.freemars.editor.ResizeMapDialog;
import org.freerealm.executor.command.ResizeMapCommand;

/**
 *
 * @author Deniz ARIKAN
 */
public class ResizeMapAction extends AbstractAction {

    private EditorController controller;

    public ResizeMapAction(EditorController controller) {
        super("Resize map", null);
        putValue(SHORT_DESCRIPTION, "Resize map");
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        if (controller.getEditorModel().isFileDirty()) {
            int result = JOptionPane.showConfirmDialog(controller.getEditorFrame(), "Current map is not saved. Do you wish to save it?", "Save current map", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                controller.getAction(EditorController.SAVE_MAP_AS).actionPerformed(e);
            }
        }
        ResizeMapDialog dialog = new ResizeMapDialog(controller.getEditorFrame(), controller.getEditorModel());
        dialog.display();
        if (dialog.getReturnValue() == ResizeMapDialog.CONFIRM) {
            int mapWidth = dialog.getMapWidth();
            int mapHeight = dialog.getMapHeight();
            controller.getEditorModel().execute(new ResizeMapCommand(mapWidth, mapHeight));
            controller.getEditorModel().setFileDirty(true);
            controller.getEditorModel().refresh();
        }
    }
}
