package org.freemars.editor.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freerealm.tile.TileType;

/**
 *
 * @author Deniz ARIKAN
 */
public class SetEditorTileTypeAction extends AbstractAction {

    private EditorController controller;
    private TileType tileType;

    public SetEditorTileTypeAction(EditorController controller, TileType tileType) {
        this.controller = controller;
        this.tileType = tileType;
    }

    public void actionPerformed(ActionEvent e) {
        controller.getEditorModel().setCurrentTileType(tileType);
    }
}
