package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.settlement.Settlement;

/**
 * Command class to set/unset a settlement's production mode continuous. Once in
 * continuous mode a settlement will continue to produce same unit until cancelled.
 * This value is not valid if settlement's current production is not a unit.
 * @author Deniz ARIKAN
 */
public class SetSettlementContinuousProductionCommand extends AbstractCommand {

    private Settlement settlement;
    private boolean contiuousProduction = false;

    /**
     * Constructs a SetCityContinuousProductionCommand using settlement,
     * contiuousProduction.
     * @param settlement Settlement to set new mode
     * @param contiuousProduction New value for continuous production mode
     */
    public SetSettlementContinuousProductionCommand(Settlement settlement, boolean contiuousProduction) {
        this.settlement = settlement;
        this.contiuousProduction = contiuousProduction;
    }

    /**
     * Executes command to set settlement's production mode to its new value.
     * @param realm Realm to execute the command
     * @return CommandResult
     */
    public CommandResult execute() {
        settlement.setContiuousProduction(contiuousProduction);
        return new CommandResult(CommandResult.RESULT_OK, "");
    }
}
