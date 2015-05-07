package org.freerealm.executor.command;

import org.freerealm.Realm;
import org.freerealm.executor.CommandResult;
import org.freerealm.settlement.Settlement;
import org.freerealm.settlement.workforce.WorkForce;
import org.freerealm.map.Coordinate;
import org.freerealm.Utility;

/**
 * Command class to assign workforce to a coordinate for a given settlement.
 * <p>
 * Upon execution this command will return an error if:
 * <ul>
 * <li>Given coordinate already has a workforce from another settlement and is
 * not available for workforce assignment.</li>
 * <li>Number of workers in workforce exceeds settlement's number of maximum
 * workers per tile.</li>
 * <li>City does not have enough free workers for the new workforce.</li>
 * </ul>
 * <p>
 * If no error is fired after checking these conditions WorkforceAssignCommand
 * will add given workforce to settlement's workforce manager. Even if number of
 * workers in given workforce is 0 command will add a new workforce.
 *
 * @author Deniz ARIKAN
 */
public class WorkforceAssignCommand extends AbstractCommand {

    private Settlement settlement;
    private WorkForce workforce;
    private Coordinate coordinate;

    /**
     * Constructs an WorkforceAssignCommand using settlement, workforce,
     * coordinate.
     *
     * @param settlement Settlement in which the workforce will be assigned
     * @param workforce New workforce which will be assigned to coordinate
     * @param coordinate Target coordinate
     */
    public WorkforceAssignCommand(Realm realm, Settlement settlement, WorkForce workforce, Coordinate coordinate) {
        super(realm);
        this.settlement = settlement;
        this.workforce = workforce;
        this.coordinate = coordinate;
    }

    /**
     * Executes command to add a new workforce to given coordinate for the
     * settlement.
     *
     * @param realm Realm to execute the command
     * @return CommandResult
     */
    public CommandResult execute() {
        if (!Utility.isTileAvailableForSettlement(getRealm(), settlement, coordinate)) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Tile is not available for settlement");
        }
        if (workforce.getNumberOfWorkers() > settlement.getMaxWorkersPerTile()) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Number of workers can not exceed " + settlement.getMaxWorkersPerTile());
        }
        int numberOfWorkersForTile = 0;
        if (settlement.getWorkForceManager().getAssignedWorkforceForTile(coordinate) != null) {
            numberOfWorkersForTile = settlement.getWorkForceManager().getAssignedWorkforceForTile(coordinate).getNumberOfWorkers();
        }
        if (workforce.getNumberOfWorkers() - numberOfWorkersForTile <= settlement.getProductionWorkforce()) {
            settlement.getWorkForceManager().addWorkForce(coordinate, workforce);
            return new CommandResult(CommandResult.RESULT_OK, "");
        } else {
            return new CommandResult(CommandResult.RESULT_ERROR, "Not enough free workforce");
        }
    }
}
