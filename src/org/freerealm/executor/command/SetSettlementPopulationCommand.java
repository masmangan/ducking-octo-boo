package org.freerealm.executor.command;

import org.freerealm.settlement.Settlement;
import org.freerealm.executor.CommandResult;

/**
 *
 * @author Deniz ARIKAN
 */
public class SetSettlementPopulationCommand extends AbstractCommand {

    private final Settlement settlement;
    private final int population;

    public SetSettlementPopulationCommand(Settlement settlement, int population) {
        this.settlement = settlement;
        this.population = population;
    }

    public CommandResult execute() {
        int currentPopulation = settlement.getPopulation();
        CommandResult commandResult;
        if (population != currentPopulation) {
            settlement.setPopulation(population);
            commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.SETTLEMENT_POPULATION_UPDATED);
            commandResult.putParameter("settlement", settlement);
            commandResult.putParameter("population", population);
        } else {
            commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.NO_UPDATE);
        }
        return commandResult;
    }
}
