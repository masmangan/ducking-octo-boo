package org.freemars.command;

import org.apache.log4j.Logger;
import org.freemars.earth.EarthFlightModel;
import org.freemars.model.FreeMarsModel;
import org.freemars.model.FreeMarsViewModel;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.AbstractCommand;
import org.freerealm.executor.command.ResetRealmCommand;

/**
 * Command class to reset the given realm.
 *
 * @author Deniz ARIKAN
 */
public class ResetFreeMarsModelCommand extends AbstractCommand {

    private final FreeMarsModel freeMarsModel;

    /**
     * Constructs a ResetFreeMarsModelCommand.
     */
    public ResetFreeMarsModelCommand(FreeMarsModel freeMarsModel) {
        this.freeMarsModel = freeMarsModel;
    }

    /**
     * Executes command to reset the given Free Mars model.
     *
     * @return CommandResult
     */
    public CommandResult execute() {
        getExecutor().execute(new ResetRealmCommand(freeMarsModel.getRealm()));
        freeMarsModel.setEarthFlightModel(new EarthFlightModel(freeMarsModel.getRealm()));
        freeMarsModel.getFreeMarsViewModel().setMapPanelDisplayingGrid(false);
        freeMarsModel.getFreeMarsViewModel().setMapPanelDisplayingCoordinates(false);
        freeMarsModel.getFreeMarsViewModel().setMapPanelDisplayingTileTypes(false);
        freeMarsModel.getFreeMarsViewModel().setMapPanelDisplayingUnitPath(false);
        freeMarsModel.getFreeMarsViewModel().setMapPanelZoomLevel(FreeMarsViewModel.MAIN_MAP_DEFAULT_ZOOM_LEVEL);
        freeMarsModel.getFreeMarsViewModel().setMiniMapPanelZoomLevel(FreeMarsViewModel.MINI_MAP_DEFAULT_ZOOM_LEVEL);
        freeMarsModel.getFreeMarsViewModel().setCenteredCoordinate(FreeMarsViewModel.DEFAULT_CENTERED_COORDINATE);
        freeMarsModel.getFreeMarsViewModel().setMapPanelUnitPath(null);
        freeMarsModel.setHumanPlayer(null);
        freeMarsModel.setHumanPlayerDefeated(false);
        freeMarsModel.clearObjectives();
        Logger.getLogger(ResetFreeMarsModelCommand.class).info("Free Mars model reset successfully.");
        return new CommandResult(CommandResult.NO_UPDATE, "");
    }
}
