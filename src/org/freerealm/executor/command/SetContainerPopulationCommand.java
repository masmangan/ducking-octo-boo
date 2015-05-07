package org.freerealm.executor.command;

import org.freerealm.PopulationContainer;
import org.freerealm.executor.CommandResult;

/**
 *
 * @author arikande
 */
public class SetContainerPopulationCommand extends AbstractCommand {

    private PopulationContainer populationContainer;
    private int population;
    private int weightPerCitizen;

    public SetContainerPopulationCommand(PopulationContainer populationContainer, int population, int weightPerCitizen) {
        this.populationContainer = populationContainer;
        this.population = population;
        this.weightPerCitizen = weightPerCitizen;
    }

    public CommandResult execute() {
        if (populationContainer.canContainPopulation()) {
            if (populationContainer.getRemainingCapacity() >= (population - populationContainer.getContainedPopulation()) * weightPerCitizen) {
                populationContainer.setContainedPopulation(population);
                return new CommandResult(CommandResult.RESULT_OK, "");
            } else {
                return new CommandResult(CommandResult.RESULT_ERROR, "Not enough capacity");
            }
        } else {
            return new CommandResult(CommandResult.RESULT_ERROR, "Container can not carry population");
        }
    }
}
