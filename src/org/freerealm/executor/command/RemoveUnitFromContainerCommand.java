package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.unit.Unit;
import org.freerealm.unit.UnitContainer;

public class RemoveUnitFromContainerCommand extends AbstractCommand {

    private UnitContainer unitContainer;
    private Unit unit;

    /**
     * Constructs an RemoveUnitFromContainerCommand using unitContainer, unit.
     * @param unitContainer Unit container to remove unit from.
     * @param unit Unit to remove from container
     */
    public RemoveUnitFromContainerCommand(UnitContainer unitContainer, Unit unit) {
        this.unitContainer = unitContainer;
        this.unit = unit;
    }

    /**
     * Executes command and removes the given unit from container.
     * @param realm Realm to execute the command
     * @return CommandResult
     */
    public CommandResult execute() {
        unitContainer.removeUnit(unit.getId());
        return new CommandResult(CommandResult.RESULT_OK, "");
    }
}
