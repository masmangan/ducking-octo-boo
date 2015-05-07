package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.settlement.Settlement;
import org.freerealm.settlement.improvement.SettlementImprovement;
import org.freerealm.settlement.improvement.SettlementImprovementType;

/**
 * Command class to add a new improvement to given settlement.
 *
 * @author Deniz ARIKAN
 */
public class AddSettlementImprovementCommand extends AbstractCommand {

    private Settlement settlement;
    private SettlementImprovementType improvementType;

    /**
     * Constructs an AddSettlementImprovementCommand using settlement and
     * improvement.
     *
     * @param settlement Settlement in which the new improvement will be built
     * @param improvementType Type of new improvement to add to settlement
     */
    public AddSettlementImprovementCommand(Settlement settlement, SettlementImprovementType improvementType) {
        this.settlement = settlement;
        this.improvementType = improvementType;
    }

    /**
     * Executes command and adds the improvement to settlement.
     *
     * @return CommandResult
     */
    public CommandResult execute() {
        SettlementImprovement newImprovement = new SettlementImprovement();
        newImprovement.setType(improvementType);
        newImprovement.setEnabled(true);
        settlement.addImprovement(newImprovement);
        return new CommandResult(CommandResult.RESULT_OK, "");
    }
}
