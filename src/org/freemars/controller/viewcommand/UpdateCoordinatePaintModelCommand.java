package org.freemars.controller.viewcommand;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.freemars.controller.FreeMarsController;
import org.freemars.ui.map.TilePaintModel;
import org.freemars.ui.map.TilePaintModelBuilder;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.AbstractCommand;
import org.freerealm.map.Coordinate;
import org.freerealm.tile.Tile;

/**
 *
 * @author Deniz ARIKAN
 */
public class UpdateCoordinatePaintModelCommand extends AbstractCommand implements ViewCommand {

    private static final Logger logger = Logger.getLogger(UpdateCoordinatePaintModelCommand.class);

    private final FreeMarsController freeMarsController;
    private final List<Coordinate> coordinates;
    private boolean paintUnits = true;

    public UpdateCoordinatePaintModelCommand(FreeMarsController freeMarsController, Coordinate coordinate) {
        this.freeMarsController = freeMarsController;
        coordinates = new ArrayList<Coordinate>();
        coordinates.add(coordinate);
    }

    public UpdateCoordinatePaintModelCommand(FreeMarsController freeMarsController, List<Coordinate> coordinates) {
        this.freeMarsController = freeMarsController;
        this.coordinates = coordinates;
    }

    public UpdateCoordinatePaintModelCommand(FreeMarsController freeMarsController, List<Coordinate> coordinates, boolean paintUnits) {
        this.freeMarsController = freeMarsController;
        this.coordinates = coordinates;
        this.paintUnits = paintUnits;
    }

    @Override
    public String toString() {
        return "UpdateCoordinatePaintModel";
    }

    public CommandResult execute() {
        long startTime = System.currentTimeMillis();
        for (Coordinate coordinate : coordinates) {
            TilePaintModel tilePaintModel = freeMarsController.getFreeMarsModel().getFreeMarsViewModel().getTilePaintModel(coordinate);
            Graphics graphics = freeMarsController.getGameFrame().getMapPanel().getGraphics();
            if (tilePaintModel == null) {
                tilePaintModel = TilePaintModelBuilder.buildTilePaintModel(graphics, freeMarsController.getFreeMarsModel(), coordinate, paintUnits);
            } else {
                Tile tile = freeMarsController.getFreeMarsModel().getTile(coordinate);
                TilePaintModelBuilder.updateTilePaintModel(graphics, tilePaintModel, freeMarsController.getFreeMarsModel(), tile, paintUnits);
            }
            if (tilePaintModel != null) {
                freeMarsController.getFreeMarsModel().getFreeMarsViewModel().putTilePaintModel(coordinate, tilePaintModel);
            }
        }
        freeMarsController.executeViewCommand(new RepaintMapPanelCommand(freeMarsController));
        long executionTime = System.currentTimeMillis() - startTime;
        if (executionTime > 10) {
            logger.info("UpdateCoordinatePaintModelCommand executed in " + executionTime + " miliseconds for " + coordinates.size() + " tiles.");
        }
        synchronized (ViewCommandExecutionThread.WAIT_ON) {
            ViewCommandExecutionThread.WAIT_ON.notify();
        }
        return new CommandResult(CommandResult.RESULT_OK, "");
    }

    public boolean isBlockingViewExecutionThread() {
        return true;
    }

}
