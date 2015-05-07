package org.freemars.editor.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.freemars.editor.NewEmptyMapDialog;
import org.freerealm.executor.command.CreateMapCommand;
import org.freerealm.map.Coordinate;
import org.freerealm.tile.TileType;

/**
 *
 * @author Deniz ARIKAN
 */
public class NewEmptyMapAction extends AbstractAction {

    private EditorController controller;

    public NewEmptyMapAction(EditorController controller) {
        super("New empty map", null);
        putValue(SHORT_DESCRIPTION, "New empty map");
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        if (controller.getEditorModel().isFileDirty()) {
            int result = JOptionPane.showConfirmDialog(controller.getEditorFrame(), "Current map is not saved. Do you wish to save it?", "Save current map", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                controller.getAction(EditorController.SAVE_MAP_AS).actionPerformed(e);
            }
        }
        NewEmptyMapDialog dialog = new NewEmptyMapDialog(controller.getEditorFrame(), controller.getEditorModel());
        dialog.display();
        if (dialog.getReturnValue() == NewEmptyMapDialog.CONFIRM) {
            int mapWidth = dialog.getMapWidth();
            int mapHeight = dialog.getMapHeight();
            TileType tileType = dialog.getSelectedTileType();
            controller.getEditorModel().execute(new CreateMapCommand(controller.getEditorModel().getRealm(), tileType, mapWidth, mapHeight));
            controller.getEditorModel().setEditorMapZoomLevel(3);
            controller.getEditorModel().setCenteredCoordinate(new Coordinate(0, 0));
            controller.getEditorModel().setFileName(null);
            controller.getEditorModel().setFileDirty(false);
            controller.getEditorModel().refresh();
        }
    }
}
