package org.freemars.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.freemars.colony.FreeMarsColony;
import org.freemars.controller.FreeMarsController;
import org.freerealm.settlement.Settlement;
import org.freerealm.executor.command.BuildSettlementCommand;
import org.freerealm.map.Coordinate;
import org.freerealm.player.Player;
import org.freerealm.executor.command.UnitAdvanceToCoordinateCommand;
import org.freerealm.map.Path;
import org.freerealm.resource.Resource;
import org.freerealm.tile.Tile;
import org.freerealm.tile.TileType;
import org.freerealm.unit.Unit;
import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public class ColonizerManager {

    private FreeMarsController freeMarsController;
    private AIPlayer freeMarsAIPlayer;
    private ArrayList<Coordinate> colonizerDestinations;
    private TileType foodTileType;

    public ColonizerManager(FreeMarsController freeMarsController, AIPlayer freeMarsAIPlayer) {
        this.freeMarsController = freeMarsController;
        this.freeMarsAIPlayer = freeMarsAIPlayer;
        colonizerDestinations = new ArrayList<Coordinate>();
        Resource foodResource = freeMarsController.getFreeMarsModel().getRealm().getResourceManager().getResource(Resource.FOOD);
        foodTileType = freeMarsController.getFreeMarsModel().getRealm().getTileTypeManager().getMaxProducingTileType(foodResource);
    }

    public void manage() {
        colonizerDestinations.clear();
        UnitType colonizerUnitType = freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Colonizer");
        List<Unit> colonizers = freeMarsAIPlayer.getUnitsOfType(colonizerUnitType);
        for (Unit colonizer : colonizers) {
            if (colonizer.getStatus() == Unit.UNIT_ACTIVE) {
                Coordinate colonySite = findColonySite(colonizer);
                if (colonySite != null) {
                    freeMarsController.execute(new UnitAdvanceToCoordinateCommand(freeMarsController.getFreeMarsModel().getRealm(), colonizer, colonySite));
                    if (colonizer.getCoordinate().equals(colonySite)) {
                        FreeMarsColony freeMarsColony = new FreeMarsColony(freeMarsController.getFreeMarsModel().getRealm());
                        freeMarsController.execute(new BuildSettlementCommand(freeMarsController.getFreeMarsModel().getRealm(), colonizer, freeMarsAIPlayer.getNextSettlementName(), freeMarsColony));
                    } else {
                        colonizerDestinations.add(colonySite);
                    }
                }
            }
        }
    }

    private Coordinate findColonySite(Unit colonizer) {
        if (colonizer.getStatus() == Unit.UNIT_ACTIVE && colonizer.getCoordinate() != null) {
            if (isOkForColony(colonizer, colonizer.getCoordinate())) {
                return colonizer.getCoordinate();
            }
            for (int i = 1; i < 10; i++) {
                List<Coordinate> coordinates = freeMarsController.getFreeMarsModel().getRealm().getCircleCoordinates(colonizer.getCoordinate(), i);
                for (Coordinate coordinate : coordinates) {
                    if (isOkForColony(colonizer, coordinate)) {
                        return coordinate;
                    }
                }
            }
            Iterator<Coordinate> exploredCoordinatesIterator = freeMarsAIPlayer.getExploredCoordinatesIterator();
            while (exploredCoordinatesIterator.hasNext()) {
                Coordinate coordinate = exploredCoordinatesIterator.next();
                if (isOkForColony(colonizer, coordinate)) {
                    return coordinate;
                }
            }
        }
        return null;
    }

    private boolean isOkForColony(Unit colonizer, Coordinate coordinate) {
        if (coordinate == null) {
            return false;
        }
        if (!freeMarsController.getFreeMarsModel().getRealm().isValidCoordinate(coordinate)) {
            return false;
        }
        if (colonizerDestinations.contains(coordinate)) {
            return false;
        }
        Tile tile = freeMarsController.getFreeMarsModel().getRealm().getTile(coordinate);
        if (!tile.getType().equals(foodTileType)) {
            return false;
        }
        if (tile.getSettlement() != null) {
            return false;
        }
        if (tile.getVegetation() != null) {
            return false;
        }
        Path path = freeMarsController.getFreeMarsModel().getRealm().getPathFinder().findPath(colonizer, coordinate, false);
        if (path == null) {
            return false;
        }
        boolean hillsFound = false;
        List<Coordinate> neighborCoordinates = freeMarsController.getFreeMarsModel().getRealm().getCircleCoordinates(coordinate, 1);
        for (Coordinate neighborCoordinate : neighborCoordinates) {
            if (neighborCoordinate == null) {
                return false;
            }
            Tile neighborTile = freeMarsController.getFreeMarsModel().getRealm().getTile(neighborCoordinate);
            if (neighborTile.getSettlement() != null) {
                return false;
            }
            if (neighborTile.getType().getName().equals("Hills")) {
                hillsFound = true;
            }
            Iterator<Player> playerIterator = freeMarsController.getFreeMarsModel().getRealm().getPlayerManager().getPlayersIterator();
            while (playerIterator.hasNext()) {
                Player player = playerIterator.next();
                Iterator<Settlement> settlementsIterator = player.getSettlementsIterator();
                while (settlementsIterator.hasNext()) {
                    Settlement otherColony = settlementsIterator.next();
                    if (otherColony.getValidWorkForceCoordinates().contains(neighborCoordinate)) {
                        return false;
                    }
                }
            }
        }
        if (!hillsFound) {
            return false;
        }
        return true;
    }
}
