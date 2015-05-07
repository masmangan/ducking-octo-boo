package org.freemars.earth.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.freemars.controller.FreeMarsController;
import org.freemars.model.FreeMarsModel;
import org.freemars.ai.ExpeditionaryForcePlayer;
import org.freerealm.Utility;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.AbstractCommand;
import org.freerealm.executor.command.AddExploredCoordinatesToPlayerCommand;
import org.freerealm.executor.command.UnitActivateCommand;
import org.freerealm.map.Coordinate;
import org.freerealm.map.Path;
import org.freerealm.map.PathFinder;
import org.freerealm.player.Player;
import org.freerealm.settlement.Settlement;
import org.freerealm.tile.Tile;
import org.freerealm.unit.Unit;
import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public class LandExpeditionaryForceWaveCommand extends AbstractCommand {

    private final FreeMarsController freeMarsController;
    private final ExpeditionaryForcePlayer expeditionaryForcePlayer;
    private Random randomGenerator;

    public LandExpeditionaryForceWaveCommand(FreeMarsController freeMarsController, ExpeditionaryForcePlayer expeditionaryForcePlayer) {
        this.freeMarsController = freeMarsController;
        this.expeditionaryForcePlayer = expeditionaryForcePlayer;
    }

    public CommandResult execute() {
        Player targetPlayer = freeMarsController.getFreeMarsModel().getRealm().getPlayerManager().getPlayer(expeditionaryForcePlayer.getTargetPlayerId());
        addExploredTilesToExpeditionaryForcePlayer(expeditionaryForcePlayer, targetPlayer);
        ArrayList<Unit> unlandedUnits = new ArrayList<Unit>();
        Iterator<Unit> iterator = expeditionaryForcePlayer.getUnitsIterator();
        while (iterator.hasNext()) {
            Unit unit = iterator.next();
            if (unit.getStatus() == Unit.UNIT_SUSPENDED) {
                unlandedUnits.add(unit);
            }
        }
        HashMap<UnitType, Integer> landedUnits = new HashMap<UnitType, Integer>();
        Coordinate centerCoordinate = findCenterCoordinate(targetPlayer);
        int i = 0;
        for (Unit unlandedUnit : unlandedUnits) {
            if (i % expeditionaryForcePlayer.getRemainingAttackWaves() == 0) {
                landUnit(unlandedUnit, centerCoordinate);
                if (landedUnits.containsKey(unlandedUnit.getType())) {
                    landedUnits.put(unlandedUnit.getType(), landedUnits.get(unlandedUnit.getType()) + 1);
                } else {
                    landedUnits.put(unlandedUnit.getType(), 1);
                }
            }
            i++;
        }
        expeditionaryForcePlayer.setRemainingAttackWaves(expeditionaryForcePlayer.getRemainingAttackWaves() - 1);
        int attackWave = expeditionaryForcePlayer.getMaximumAttackWaves() - expeditionaryForcePlayer.getRemainingAttackWaves();
        CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "", FreeMarsModel.EXPEDITIONARY_FORCE_LANDED_UPDATE);
        commandResult.putParameter("targetPlayer", targetPlayer);
        commandResult.putParameter("attackWave", attackWave);
        commandResult.putParameter("landedUnits", landedUnits);
        return commandResult;
    }

    private void addExploredTilesToExpeditionaryForcePlayer(Player expeditionaryForcePlayer, Player targetPlayer) {
        Iterator<Settlement> iterator = targetPlayer.getSettlementsIterator();
        while (iterator.hasNext()) {
            Settlement settlement = iterator.next();
            expeditionaryForcePlayer.addExploredCoordinate(settlement.getCoordinate());
            for (int i = 1; i < 5; i++) {
                List<Coordinate> circleCoordinates = freeMarsController.getFreeMarsModel().getRealm().getCircleCoordinates(settlement.getCoordinate(), i);
                getExecutor().equals(new AddExploredCoordinatesToPlayerCommand(expeditionaryForcePlayer, circleCoordinates));
            }
        }
    }

    private Coordinate findCenterCoordinate(Player targetPlayer) {
        Iterator<Settlement> iterator = targetPlayer.getSettlementsIterator();
        int abscissaTotal = 0;
        int ordinateTotal = 0;
        while (iterator.hasNext()) {
            Settlement settlement = iterator.next();
            abscissaTotal = abscissaTotal + settlement.getCoordinate().getAbscissa();
            ordinateTotal = ordinateTotal + settlement.getCoordinate().getOrdinate();
        }
        int abscissa = Math.round(abscissaTotal / targetPlayer.getSettlementCount());
        int ordinate = Math.round(ordinateTotal / targetPlayer.getSettlementCount());
        return new Coordinate(abscissa, ordinate);
    }

    private void landUnit(Unit unit, Coordinate centerCoordinate) {
        Coordinate landingCoordinate = searchAroundCoordinate(centerCoordinate, unit);
        freeMarsController.execute(new UnitActivateCommand(freeMarsController.getFreeMarsModel().getRealm(), unit, landingCoordinate));
    }

    private Coordinate searchAroundCoordinate(Coordinate centerCoordinate, Unit unit) {
        randomGenerator = new Random();
        if (randomGenerator.nextInt(6) == 0) {
            Iterator<Coordinate> iterator = expeditionaryForcePlayer.getExploredCoordinatesIterator();
            ArrayList<Coordinate> exploredCoordinates = new ArrayList<Coordinate>();
            while (iterator.hasNext()) {
                Coordinate coordinate = iterator.next();
                exploredCoordinates.add(coordinate);
            }
            for (int i = 0; i < expeditionaryForcePlayer.getExploredCoordinateCount(); i++) {
                int exploredCoordinateIndex = randomGenerator.nextInt(expeditionaryForcePlayer.getExploredCoordinateCount());
                Coordinate checkCoordinate = exploredCoordinates.get(exploredCoordinateIndex);
                unit.setCoordinate(checkCoordinate);
                if (isValidLandingCoordinate(freeMarsController.getFreeMarsModel(), unit, centerCoordinate)) {
                    return checkCoordinate;
                }
            }
        }
        for (int i = 4 + randomGenerator.nextInt(6); i < 12; i++) {
            List<Coordinate> checkCoordinates = freeMarsController.getFreeMarsModel().getRealm().getCircleCoordinates(centerCoordinate, i);
            if (Math.random() >= 0.5) {
                Collections.reverse(checkCoordinates);
            }
            for (Coordinate checkCoordinate : checkCoordinates) {
                unit.setCoordinate(checkCoordinate);
                if (isValidLandingCoordinate(freeMarsController.getFreeMarsModel(), unit, centerCoordinate)) {
                    return checkCoordinate;
                }
            }
        }
        return centerCoordinate;
    }

    private boolean isValidLandingCoordinate(FreeMarsModel freeMarsModel, Unit unit, Coordinate centerCoordinate) {
        if (unit.getCoordinate() == null) {
            return false;
        }
        if (!freeMarsModel.getRealm().isValidCoordinate(unit.getCoordinate())) {
            return false;
        }
        Tile tile = freeMarsModel.getRealm().getTile(unit.getCoordinate());
        if (tile == null) {
            return false;
        }
        if (tile.getSettlement() != null) {
            return false;
        }
        if (tile.getOccupiedByPlayer() != null && !tile.getOccupiedByPlayer().equals(unit.getPlayer())) {
            return false;
        }
        if (tile.getUnits().size() >= 4) {
            return false;
        }
        if (!tile.getType().getName().equals("Plains") && !tile.getType().getName().equals("Wasteland") && !tile.getType().getName().equals("Desert")) {
            return false;
        }
        PathFinder pathFinder = freeMarsModel.getRealm().getPathFinder();
        Path path = pathFinder.findPath(unit, centerCoordinate, false);
        return path != null;
    }
}
