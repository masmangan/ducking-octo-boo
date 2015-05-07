package org.freerealm.executor.command;

import org.freerealm.settlement.Settlement;
import org.freerealm.settlement.improvement.SettlementImprovement;
import org.freerealm.executor.CommandResult;

/**
 *
 * @author Deniz ARIKAN
 */
public class SettlementImprovementSetWorkersCommand extends AbstractCommand {

    private Settlement settlement;
    private SettlementImprovement settlementImprovement;
    private int workers;

    public SettlementImprovementSetWorkersCommand(Settlement settlement, SettlementImprovement settlementImprovement, int workers) {
        this.settlement = settlement;
        this.settlementImprovement = settlementImprovement;
        this.workers = workers;
    }

    public CommandResult execute() {
        int availableWorkers = settlement.getProductionWorkforce() + settlementImprovement.getNumberOfWorkers();
        int workersToAssign = (availableWorkers > workers ? workers : availableWorkers);
        workersToAssign = (settlementImprovement.getType().getMaximumWorkers() > workersToAssign ? workersToAssign : settlementImprovement.getType().getMaximumWorkers());
        settlementImprovement.setNumberOfWorkers(workersToAssign);
        if (workersToAssign > 0) {
            getExecutor().execute(new SettlementImprovementSetEnabledCommand(settlement, settlementImprovement, true));
        }
        return new CommandResult(CommandResult.RESULT_OK, "");
    }
}
