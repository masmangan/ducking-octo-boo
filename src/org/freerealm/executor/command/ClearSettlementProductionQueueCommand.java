package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.settlement.Settlement;

/**
 *
 * @author Deniz ARIKAN
 */
public class ClearSettlementProductionQueueCommand extends AbstractCommand {

    private final Settlement settlement;

    public ClearSettlementProductionQueueCommand(Settlement settlement) {
        this.settlement = settlement;
    }

    /**
     * Executes command to assign new production and mode to given settlement.
     *
     * @return CommandResult
     */
    public CommandResult execute() {
        settlement.clearProductionQueue();
        return new CommandResult(CommandResult.RESULT_OK, "");
    }
}
