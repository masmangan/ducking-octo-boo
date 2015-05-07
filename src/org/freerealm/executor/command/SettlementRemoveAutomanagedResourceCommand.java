package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.resource.Resource;
import org.freerealm.settlement.Settlement;

/**
 *
 * @author Deniz ARIKAN
 */
public class SettlementRemoveAutomanagedResourceCommand extends AbstractCommand {

    private Settlement settlement;
    private Resource resource;

    public SettlementRemoveAutomanagedResourceCommand(Settlement settlement, Resource resource) {
        this.settlement = settlement;
        this.resource = resource;
    }

    public CommandResult execute() {
        settlement.removeAutomanagedResource(resource);
        CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.NO_UPDATE);
        return commandResult;
    }
}
