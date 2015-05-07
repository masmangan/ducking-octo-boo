package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.unit.Unit;

/**
 * Command class to skip a unit and (if possible) activate the next unit in
 * queue. When executed SkipUnitCommand will set movement points of the unit to
 * 0, get next unit of player and activate it. If next unit for the player is
 * null, then active unit will be set to null.
 *
 * @author Deniz ARIKAN
 */
public class SkipUnitCommand extends AbstractCommand {

    private Unit unit;

    /**
     * Constructs a SkipUnitCommand using unit.
     *
     * @param unit Unit to set active, can be null
     */
    public SkipUnitCommand(Unit unit) {
        this.unit = unit;
    }

    /**
     * Executes command to skip given unit for player.
     *
     * @return CommandResult
     */
    public CommandResult execute() {
        if (unit == null) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Unit is null");
        } else {
            getExecutor().execute(new UnitSetMovementPointsCommand(unit, 0));
            unit.setSkippedForCurrentTurn(true);
            CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.UNIT_SKIPPED_UPDATE);
            commandResult.putParameter("skippedUnit", unit);
            return commandResult;
        }
    }
}
