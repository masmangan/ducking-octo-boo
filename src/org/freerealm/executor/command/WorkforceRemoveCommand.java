package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.settlement.Settlement;
import org.freerealm.map.Coordinate;

/**
 * Command class to remove workforce from a coordinate for a given settlement.
 * @author Deniz ARIKAN
 */
public class WorkforceRemoveCommand extends AbstractCommand {

    private Settlement settlement;
    private Coordinate coordinate;

    /**
     * Constructs an WorkforceRemoveCommand using settlement, coordinate.
     * @param settlement Settlement in which the workforce will be removed
     * @param coordinate Target coordinate
     */
    public WorkforceRemoveCommand(Settlement settlement, Coordinate coordinate) {
        this.settlement = settlement;
        this.coordinate = coordinate;
    }

    /**
     * Executes command to remove a new workforce from given coordinate for the
     * settlement.
     * @param realm Realm to execute the command
     * @return CommandResult
     */
    public CommandResult execute() {
        settlement.getWorkForceManager().removeWorkForce(coordinate);
        return new CommandResult(CommandResult.RESULT_OK, "");
    }
}
