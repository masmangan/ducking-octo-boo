package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.resource.Resource;
import org.freerealm.settlement.Settlement;

/**
 *
 * @author Deniz ARIKAN
 */
public class SettlementAddAutomanagedResourceCommand extends AbstractCommand {

    private Settlement settlement;
    private Resource resource;

    public SettlementAddAutomanagedResourceCommand(Settlement settlement, Resource resource) {
        this.settlement = settlement;
        this.resource = resource;
    }

    public CommandResult execute() {
        settlement.addAutomanagedResource(resource);
        CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.NO_UPDATE);
        return commandResult;
    }
}
