package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.unit.Unit;

/**
 * Command class to set a unit's movement points. When executed
 * UnitSuspendCommand will set movement points of the unit to given parameter.
 * @author Deniz ARIKAN
 */
public class UnitSetMovementPointsCommand extends AbstractCommand {

    private Unit unit;
    private int movementPoints;

    /**
     * Constructs a UnitSetMovementPointsCommand using unit, movementPoints.
     * @param unit Unit to suspend, can not be null.
     * @param movementPoints New movementPoints, can not be less than zero.
     */
    public UnitSetMovementPointsCommand(Unit unit, int movementPoints) {
        this.unit = unit;
        this.movementPoints = movementPoints;
    }

    /**
     * Executes command to set given unit's movement points.
     * @param realm Realm to execute the command
     * @return CommandResult
     */
    public CommandResult execute() {
        if (unit == null) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Unit is null", CommandResult.NO_UPDATE);
        }
        if (movementPoints < 0) {
            return new CommandResult(CommandResult.RESULT_ERROR, "movementPoints is less than zero", CommandResult.NO_UPDATE);
        }
        unit.setMovementPoints(movementPoints);
        return new CommandResult(CommandResult.RESULT_OK, "");
    }
}
