package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.settlement.Settlement;
import org.freerealm.settlement.improvement.SettlementImprovement;

/**
 *
 * @author Deniz ARIKAN
 */
public class SettlementImprovementSetEnabledCommand extends AbstractCommand {

    private Settlement settlement;
    private SettlementImprovement settlementImprovement;
    private boolean enabled;

    public SettlementImprovementSetEnabledCommand(Settlement settlement, SettlementImprovement settlementImprovement, boolean enabled) {
        this.settlement = settlement;
        this.settlementImprovement = settlementImprovement;
        this.enabled = enabled;
    }

    public CommandResult execute() {
        settlementImprovement.setEnabled(enabled);
        if (!enabled) {
            getExecutor().execute(new SettlementImprovementSetWorkersCommand(settlement, settlementImprovement, 0));
        }
        return new CommandResult(CommandResult.RESULT_OK, "");
    }
}
