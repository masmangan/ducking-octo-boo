package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.settlement.Settlement;
import org.freerealm.settlement.SettlementBuildable;

/**
 * Command class to assign a new production to a settlement. If new production
 * is a settlement improvement that the settlement already has, an error will be
 * returned. New value for production can be null. Continuous production can
 * also be set/unset with this command.
 *
 * @author Deniz ARIKAN
 */
public class AddToSettlementProductionQueueCommand extends AbstractCommand {

    private final Settlement settlement;
    private final SettlementBuildable buildable;

    /**
     * Constructs a AddToSettlementProductionQueueCommand using settlement,
     * buildable, contiuousProduction.
     *
     * @param settlement Settlement to assign new production
     * @param buildable New production assignment
     */
    public AddToSettlementProductionQueueCommand(Settlement settlement, SettlementBuildable buildable) {
        this.settlement = settlement;
        this.buildable = buildable;
    }

    /**
     * Executes command to assign new production and mode to given settlement.
     *
     * @return CommandResult
     */
    public CommandResult execute() {
        if (!settlement.canBuild(buildable)) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Settlement cannot build " + buildable);
        } else {
            settlement.addToProductionQueue(buildable);
            return new CommandResult(CommandResult.RESULT_OK, "");
        }
    }
}
