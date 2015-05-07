package org.freemars.unit.automater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.freemars.controller.FreeMarsController;
import org.freemars.tile.SpaceshipDebrisCollectable;
import org.freerealm.executor.command.UnitAdvanceToCoordinateCommand;
import org.freerealm.map.Coordinate;
import org.freerealm.map.Path;
import org.freerealm.tile.Tile;
import org.freerealm.unit.AbstractUnitAutomater;
import org.freerealm.unit.Unit;

/**
 *
 * @author Deniz ARIKAN
 */
public class ScoutAutomater extends AbstractUnitAutomater {

    private static final String NAME = "freeMarsScoutAutomater";
    private FreeMarsController freeMarsController;

    public String getName() {
        return NAME;
    }

    public void automate() {
        if (isScoutingNeeded(getUnit())) {
            Coordinate scoutingSite = findScoutingSite(getUnit());
            if (scoutingSite != null) {
                freeMarsController.execute(new UnitAdvanceToCoordinateCommand(freeMarsController.getFreeMarsModel().getRealm(), getUnit(), scoutingSite));
            } else {
                String log = "Scout automater for \"" + getUnit().getName() + "\" could not find any tiles to explore in turn " + freeMarsController.getFreeMarsModel().getNumberOfTurns() + ". Removing automater for \"" + getUnit().getName() + "\".";
                Logger.getLogger(ScoutAutomater.class).info(log);
                getUnit().setAutomater(null);
            }
        }
    }

    public void setFreeMarsController(FreeMarsController freeMarsController) {
        this.freeMarsController = freeMarsController;

    }

    private boolean isScoutingNeeded(Unit scout) {
        for (int i = 1; i < 10; i++) {
            List<Coordinate> circleCoordinates = freeMarsController.getFreeMarsModel().getRealm().getCircleCoordinates(scout.getCoordinate(), i);
            for (Coordinate coordinate : circleCoordinates) {
                if (coordinate != null && !getUnit().getPlayer().isCoordinateExplored(coordinate)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Coordinate findScoutingSite(Unit scout) {
        for (int i = 1; i < 10; i++) {
            List<Coordinate> circleCoordinates = freeMarsController.getFreeMarsModel().getRealm().getCircleCoordinates(scout.getCoordinate(), i);
            List<Coordinate> candidateCoordinates = new ArrayList<Coordinate>();
            for (Coordinate coordinate : circleCoordinates) {
                if (isCoordinateOkForScouting(scout, coordinate)) {
                    candidateCoordinates.add(coordinate);
                }
            }
            Collections.shuffle(candidateCoordinates);
            for (Coordinate candidateCoordinate : candidateCoordinates) {
                Tile tile = freeMarsController.getFreeMarsModel().getTile(candidateCoordinate);
                if (getUnit().getPlayer().getSettlementCount() > 0 && tile.getCollectable() != null && (tile.getCollectable() instanceof SpaceshipDebrisCollectable)) {
                    return candidateCoordinate;
                }
            }
        }
        for (int i = 1; i < 10; i++) {
            List<Coordinate> circleCoordinates = freeMarsController.getFreeMarsModel().getRealm().getCircleCoordinates(scout.getCoordinate(), i);
            List<Coordinate> candidateCoordinates = new ArrayList<Coordinate>();
            for (Coordinate coordinate : circleCoordinates) {
                if (isCoordinateOkForScouting(scout, coordinate)) {
                    candidateCoordinates.add(coordinate);
                }
            }
            Collections.shuffle(candidateCoordinates);
            for (int j = 4; j > 0; j--) {
                for (Coordinate candidateCoordinate : candidateCoordinates) {
                    if (getUnexploredNeighborCount(candidateCoordinate) >= j) {
                        return candidateCoordinate;
                    }
                }
                for (Coordinate candidateCoordinate : candidateCoordinates) {
                    if (getUnexploredNeighborCount(candidateCoordinate) >= j) {
                        return candidateCoordinate;
                    }
                }
            }
        }
        return null;
    }

    private boolean isCoordinateOkForScouting(Unit scout, Coordinate coordinate) {
        if (coordinate == null) {
            return false;
        }
        if (!getUnit().getPlayer().isCoordinateExplored(coordinate)) {
            return false;
        }
        Tile tile = freeMarsController.getFreeMarsModel().getTile(coordinate);
        if (tile == null) {
            return false;
        }
        Path path = freeMarsController.getFreeMarsModel().getRealm().getPathFinder().findPath(scout, coordinate, false);
        if (path == null) {
            return false;
        }
        return true;
    }

    private int getUnexploredNeighborCount(Coordinate coordinate) {
        int unexploredNeighborCount = 0;
        List<Coordinate> neighbors = freeMarsController.getFreeMarsModel().getRealm().getCircleCoordinates(coordinate, 1);
        for (Coordinate neighbor : neighbors) {
            if (!getUnit().getPlayer().isCoordinateExplored(neighbor)) {
                unexploredNeighborCount = unexploredNeighborCount + 1;
            }
        }
        return unexploredNeighborCount;
    }
}
