package org.freerealm.executor.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.freerealm.executor.CommandResult;
import org.freerealm.map.Coordinate;
import org.freerealm.Realm;
import org.freerealm.tile.Tile;
import org.freerealm.diplomacy.Diplomacy;
import org.freerealm.player.Player;
import org.freerealm.property.AllowMovementPropery;
import org.freerealm.unit.Unit;
import org.freerealm.property.MoveProperty;
import org.freerealm.tile.improvement.TileImprovementType;

/**
 * Command class to move a unit to one of its adjacent tiles.
 * <p>
 * Upon execution this command will return an error if:
 * <ul>
 * <li>Unit does not have "Move" property.</li>
 * <li>Unit's "Move" property does not have destination tile's type.</li>
 * <li>Given coordinate is not adjacent to unit's coordinate.</li>
 * <li>Unit does not have enough movement points, with one exception. If target
 * tile's movement cost and unit's movement points are both smaller than 1, then
 * unit can move to target tile even if its movement points are less than target
 * tile's movement cost.</li>
 * <li>Target tile is occupied by a unit that does not belong to moving unit's
 * player. If moving unit is going to capture target tile, an
 * <tt>AttackTileCommand</tt> must be called.</li>
 * <li>TODO : Invade settlement check</li>
 * </ul>
 *
 * @author Deniz ARIKAN
 */
public class MoveUnitCommand extends AbstractCommand {

    private Unit unit;
    private Coordinate coordinate;

    /**
     * Constructs a MoveUnitCommand using unit and coordinate.
     *
     * @param unit Unit to move, can not be null
     * @param coordinate Unit will be moved to given coordinate
     */
    public MoveUnitCommand(Realm realm, Unit unit, Coordinate coordinate) {
        super(realm);
        this.unit = unit;
        this.coordinate = coordinate;
    }

    /**
     * Executes command to move given unit.
     *
     * @param realm Realm to execute the command
     * @return CommandResult
     */
    public CommandResult execute() {
        boolean tileHasAllowMovement = false;
        MoveProperty moveAbility = (MoveProperty) unit.getType().getProperty(MoveProperty.NAME);
        if (moveAbility == null) {
            return new CommandResult(CommandResult.RESULT_ERROR, "");
        }
        Tile toTile = getRealm().getTile(coordinate);
        Iterator<TileImprovementType> iterator = toTile.getImprovementsIterator();
        while (iterator.hasNext()) {
            TileImprovementType tileImprovement = iterator.next();
            if (tileImprovement.hasPropertyNamed(AllowMovementPropery.NAME)) {
                tileHasAllowMovement = true;
                break;
            }
        }
        if (!moveAbility.hasTileType(toTile.getType()) && !tileHasAllowMovement) {
            return new CommandResult(CommandResult.RESULT_ERROR, "");
        }
        List<Coordinate> adjacentCoordinates = getRealm().getCircleCoordinates(unit.getCoordinate(), 1);
        if (!adjacentCoordinates.contains(coordinate)) {
            return new CommandResult(CommandResult.RESULT_ERROR, "");
        }
        if (unit.getMovementPoints() <= 0) {
            return new CommandResult(CommandResult.RESULT_ERROR, "");
        }
        if (unit.getMovementPoints() > 0 && unit.getMovementPoints() < 1 && toTile.getMovementCost() >= 1) {
            return new CommandResult(CommandResult.RESULT_ERROR, "");
        }
        if (toTile.getNumberOfUnits() > 0) {
            Unit defendingUnit = toTile.getUnits().get(toTile.getUnits().firstKey());
            if (!unit.getPlayer().equals(defendingUnit.getPlayer())) {
                return new CommandResult(CommandResult.RESULT_ERROR, "Tile contains a unit that does not belong to player. Use AttackTileCommand.");
            }
        }

        if ((toTile.getSettlement() != null) && (!unit.getPlayer().equals((toTile.getSettlement().getPlayer())))) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Tile contains a settlement that does not belong to player. Use InvadeCityCommand.");
        }

        Coordinate previousCoordinate = unit.getCoordinate();
        getRealm().getMap().removeUnit(unit);
        unit.setCoordinate(coordinate);
        getRealm().getMap().addUnit(unit);
        float movementCost = toTile.getMovementCost();
        float newMovementPoints = (unit.getMovementPoints() >= movementCost ? unit.getMovementPoints() - movementCost : 0);
        unit.setMovementPoints(newMovementPoints);
        manageExploration();
        manageCollectable();
        manageDiplomacy();
        CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.UNIT_MOVEMENT_UPDATE);
        commandResult.putParameter("unit", unit);
        commandResult.putParameter("previousCoordinate", previousCoordinate);
        commandResult.putParameter("newCoordinate", coordinate);
        return commandResult;
    }

    private void manageExploration() {
        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        coordinates.add(unit.getCoordinate());
        for (int i = 1; i < unit.getType().getExplorationRadius() + 1; i++) {
            coordinates.addAll(getRealm().getCircleCoordinates(coordinate, i));
        }
        getExecutor().execute(new AddExploredCoordinatesToPlayerCommand(unit.getPlayer(), coordinates));
    }

    private void manageCollectable() {
        Tile tile = getRealm().getTile(coordinate);
        if (tile != null && tile.getCollectable() != null) {
            getExecutor().execute(new ProcessCollectableCommand(getRealm(), unit, tile.getCollectable()));
        }
    }

    private void manageDiplomacy() {
        List<Coordinate> circleCoordinates = getRealm().getCircleCoordinates(coordinate, 1);
        for (Coordinate checkCoordinate : circleCoordinates) {
            Tile checkTile = getRealm().getTile(checkCoordinate);
            if (checkTile.getSettlement() != null) {
                Player movingUnitsPlayer = unit.getPlayer();
                Player neighborTileCityPlayer = checkTile.getSettlement().getPlayer();
                setDiplomaticStatusToPeaceIfNeeded(movingUnitsPlayer, neighborTileCityPlayer);
            }
            Iterator<Unit> unitIterator = checkTile.getUnitsIterator();
            while (unitIterator.hasNext()) {
                Unit checkUnit = unitIterator.next();
                Player movingUnitsPlayer = unit.getPlayer();
                Player neighborTileUnitPlayer = checkUnit.getPlayer();
                setDiplomaticStatusToPeaceIfNeeded(movingUnitsPlayer, neighborTileUnitPlayer);
            }
        }
    }

    private void setDiplomaticStatusToPeaceIfNeeded(Player player1, Player player2) {
        if (!player1.equals(player2)) {
            int diplomaticStatus = getRealm().getDiplomacy().getPlayerRelationStatus(player1, player2);
            if (diplomaticStatus == Diplomacy.NO_CONTACT) {
                getRealm().getDiplomacy().addPlayerRelation(player1, player2, Diplomacy.AT_PEACE);
            }
        }
    }
}
