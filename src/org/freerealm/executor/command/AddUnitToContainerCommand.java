package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.unit.Unit;
import org.freerealm.unit.UnitContainer;

/**
 *
 * @author Deniz ARIKAN
 */
public class AddUnitToContainerCommand extends AbstractCommand {

    private UnitContainer unitContainer;
    private Unit unit;

    /**
     * Constructs an AddUnitToContainerCommand using unitContainer, unit.
     *
     * @param unitContainer Unit container to hold unit
     * @param unit Unit to add to container
     */
    public AddUnitToContainerCommand(UnitContainer unitContainer, Unit unit) {
        this.unitContainer = unitContainer;
        this.unit = unit;
    }

    /**
     * Executes command and adds the given unit to container.
     *
     * @return CommandResult
     */
    public CommandResult execute() {
        if (unitContainer.getTotalCapacity() >= (unit.getType().getWeightForContainer() + unitContainer.getTotalContainedWeight())) {
            unit.setStatus(Unit.UNIT_SUSPENDED);
            unitContainer.addUnit(unit.getId());
            return new CommandResult(CommandResult.RESULT_OK, "");
        } else {
            return new CommandResult(CommandResult.RESULT_ERROR, "Not enough capacity");
        }
    }
}
