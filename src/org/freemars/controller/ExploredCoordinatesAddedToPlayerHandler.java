package org.freemars.controller;

import java.util.ArrayList;
import java.util.List;
import org.freemars.controller.viewcommand.UpdateCoordinatePaintModelCommand;
import org.freerealm.executor.CommandResult;
import org.freerealm.map.Coordinate;
import org.freerealm.player.Player;

/**
 * @author Deniz ARIKAN
 */
public class ExploredCoordinatesAddedToPlayerHandler implements ControllerUpdateHandler {

    public void handleUpdate(FreeMarsController freeMarsController, CommandResult commandResult) {
        Player player = (Player) commandResult.getParameter("player");
        if (player.equals(freeMarsController.getFreeMarsModel().getHumanPlayer())) {
            List<Coordinate> addedCoordinates = (List<Coordinate>) commandResult.getParameter("added_coordinates");
            List<Coordinate> modelUpdateCoordinates = new ArrayList<Coordinate>();
            modelUpdateCoordinates.addAll(addedCoordinates);
            for (Coordinate coordinate : addedCoordinates) {
                List<Coordinate> circleCoordinates = freeMarsController.getFreeMarsModel().getRealm().getCircleCoordinates(coordinate, 1);
                for (Coordinate circleCoordinate : circleCoordinates) {
                    if (!modelUpdateCoordinates.contains(circleCoordinate)) {
                        modelUpdateCoordinates.add(circleCoordinate);
                    }
                }
            }
            freeMarsController.executeViewCommand(new UpdateCoordinatePaintModelCommand(freeMarsController, modelUpdateCoordinates, false));
        }
    }
}
