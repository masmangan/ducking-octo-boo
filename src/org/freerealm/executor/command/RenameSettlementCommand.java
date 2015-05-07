package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.settlement.Settlement;

/**
 * Command class to rename a settlement. Command will return an error if new name is
 * null or empty.
 * @author Deniz ARIKAN
 */
public class RenameSettlementCommand extends AbstractCommand {

    private Settlement settlement;
    private String name;

    /**
     * Constructs a RenameCityCommand using settlement, name.
     * @param settlement Settlement to rename
     * @param name New name for the settlement
     */
    public RenameSettlementCommand(Settlement settlement, String name) {
        this.settlement = settlement;
        this.name = name;
    }

    /**
     * Executes command to rename given settlement.
     * @return CommandResult
     */
    public CommandResult execute() {
        if ((name == null) || (name.trim().equals(""))) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Settlement name cannot be empty");
        }
        settlement.setName(name);
        return new CommandResult(CommandResult.RESULT_OK, "");
    }
}
