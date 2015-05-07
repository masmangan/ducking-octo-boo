package org.freemars.controller.listener;

import org.freemars.controller.FreeMarsController;
import java.awt.event.MouseEvent;
import org.freerealm.map.Coordinate;
import java.awt.event.InputEvent;
import javax.swing.event.MouseInputAdapter;
import org.freemars.controller.viewcommand.SetCenteredCoordinateCommand;
import org.freemars.ui.map.MiniMapPanel;

/**
 *
 * @author Deniz ARIKAN
 */
public class MiniMapPanelMouseListener extends MouseInputAdapter {

    private FreeMarsController freeMarsController;
    private MiniMapPanel miniMapPanel;

    public MiniMapPanelMouseListener(FreeMarsController freeMarsController, MiniMapPanel miniMapPanel) {
        this.freeMarsController = freeMarsController;
        this.miniMapPanel = miniMapPanel;
    }

    private void handleLeftClick(MouseEvent mouseEvent) {
        Coordinate coordinate = miniMapPanel.getCoordinateAt(mouseEvent.getX(), mouseEvent.getY());
        if (coordinate.getOrdinate() >= freeMarsController.getFreeMarsModel().getRealm().getMapHeight()) {
            return;
        }
        freeMarsController.executeViewCommand(new SetCenteredCoordinateCommand(freeMarsController, coordinate));
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        miniMapPanel.requestFocus();
        if ((mouseEvent.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
            handleLeftClick(mouseEvent);
        }
    }
}
