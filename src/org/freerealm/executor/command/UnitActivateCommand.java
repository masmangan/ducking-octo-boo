package org.freerealm.executor.command;

import org.freerealm.Realm;
import org.freerealm.executor.CommandResult;
import org.freerealm.map.Coordinate;
import org.freerealm.unit.Unit;

/**
 * Command class to set a suspended unit's status to active. When executed
 * UnitSuspendCommand will set movement points of the unit to 0, add it to the
 * world map by setting its coordinate to new coordinate.
 *
 * @author Deniz ARIKAN
 */
public class UnitActivateCommand extends AbstractCommand {

    private Unit unit;
    private Coordinate coordinate;

    /**
     * Constructs a UnitActivateCommand using unit, coordinate.
     *
     * @param unit Suspended unit to activate, can not be null
     * @param coordinate Coordinate to put activated unit, can not be null
     */
    public UnitActivateCommand(Realm realm, Unit unit, Coordinate coordinate) {
        super(realm);
        this.unit = unit;
        this.coordinate = coordinate;
    }

    /**
     * Executes command to set status of a unit to active.
     *
     * @return CommandResult
     */
    public CommandResult execute() {
        getExecutor().execute(new UnitSetMovementPointsCommand(unit, 0));
        unit.setStatus(Unit.UNIT_ACTIVE);
        unit.setCoordinate(coordinate);
        for (int i = 0; i < unit.getType().getExplorationRadius() + 1; i++) {
            getExecutor().execute(new AddExploredCoordinatesToPlayerCommand(unit.getPlayer(), getRealm().getCircleCoordinates(coordinate, i)));
        }
        getRealm().getMap().addUnit(unit);
        CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.UNIT_STATUS_ACTIVATED_UPDATE);
        commandResult.putParameter("unit", unit);
        commandResult.putParameter("coordinate", unit.getCoordinate());
        return commandResult;
    }
}
