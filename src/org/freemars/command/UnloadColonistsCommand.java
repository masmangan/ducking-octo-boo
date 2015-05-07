package org.freemars.command;

import org.freemars.colony.FreeMarsColony;
import org.freemars.controller.FreeMarsController;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.AbstractCommand;
import org.freerealm.executor.command.SetContainerPopulationCommand;
import org.freerealm.executor.command.SetSettlementPopulationCommand;
import org.freerealm.settlement.Settlement;
import org.freerealm.unit.Unit;

/**
 *
 * @author Deniz ARIKAN
 */
public class UnloadColonistsCommand extends AbstractCommand {

    private final FreeMarsController freeMarsController;
    private final Settlement freeMarsColony;
    private final Unit unit;
    int numberOfColonists;

    public UnloadColonistsCommand(FreeMarsController freeMarsController, Settlement freeMarsColony, Unit unit, int numberOfColonists) {
        this.freeMarsController = freeMarsController;
        this.freeMarsColony = freeMarsColony;
        this.unit = unit;
        this.numberOfColonists = numberOfColonists;
    }

    public CommandResult execute() {
        int weightPerCitizen = Integer.parseInt(freeMarsController.getFreeMarsModel().getRealm().getProperty("weight_per_citizen"));
        freeMarsController.execute(new SetContainerPopulationCommand(unit, unit.getContainedPopulation() - numberOfColonists, weightPerCitizen));
        freeMarsController.execute(new SetSettlementPopulationCommand(freeMarsColony, freeMarsColony.getPopulation() + numberOfColonists));
        return new CommandResult(CommandResult.NO_UPDATE, "");
    }

}
