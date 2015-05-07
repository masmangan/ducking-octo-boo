package org.freemars.editor;

import org.freerealm.executor.command.SetTileTypeCommand;
import java.awt.event.MouseEvent;
import org.freerealm.map.Coordinate;
import java.awt.event.InputEvent;
import javax.swing.event.MouseInputAdapter;
import org.freerealm.executor.command.SetTileBonusResourceCommand;
import org.freerealm.executor.command.SetTileVegetationCommand;
import org.freerealm.vegetation.FreeRealmVegetation;
import org.freerealm.vegetation.Vegetation;

/**
 *
 * @author Deniz ARIKAN
 */
public class EditorMapPanelMouseListener extends MouseInputAdapter {

    private EditorModel editorModel;

    public EditorMapPanelMouseListener(EditorModel editorModel) {
        this.editorModel = editorModel;
    }

    private void handleLeftClick(MouseEvent mouseEvent) {
        EditorMapPanel editorMapPanel = (EditorMapPanel) mouseEvent.getSource();
        Coordinate coordinate = editorMapPanel.getCoordinateAt(mouseEvent.getX(), mouseEvent.getY());
        if (coordinate.getOrdinate() >= editorModel.getRealm().getMapHeight()) {
            return;
        }
        editorModel.setCenteredCoordinate(coordinate);
        editorModel.refresh();
    }

    private void handleRightClick(MouseEvent mouseEvent) {
        EditorMapPanel editorMapPanel = (EditorMapPanel) mouseEvent.getSource();
        Coordinate coordinate = editorMapPanel.getCoordinateAt(mouseEvent.getX(), mouseEvent.getY());
        editorModel.execute(new SetTileTypeCommand(editorModel.getRealm(), coordinate, editorModel.getCurrentTileType()));
        Vegetation vegetation = new FreeRealmVegetation();
        vegetation.setType(editorModel.getCurrentVegetation());
        editorModel.execute(new SetTileVegetationCommand(editorModel.getRealm(), coordinate, vegetation));
        editorModel.execute(new SetTileBonusResourceCommand(editorModel.getRealm(), coordinate, editorModel.getBonusResource()));
        editorModel.setFileDirty(true);
        editorModel.refresh();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if ((mouseEvent.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
            handleLeftClick(mouseEvent);
        } else if ((mouseEvent.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
            handleRightClick(mouseEvent);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
