package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.unit.Unit;

/**
 * Command class to clear an order from a given unit. When called this command
 * will clear all orders of the given unit.
 *
 * @author Deniz ARIKAN
 */
public class UnitOrdersClearCommand extends AbstractCommand {

    private Unit unit;

    /**
     * Constructs a UnitOrdersClearCommand using unit.
     *
     * @param unit This unit's orders will be cleared.
     */
    public UnitOrdersClearCommand(Unit unit) {
        this.unit = unit;
    }

    /**
     * Executes command and clears orders of given unit.
     *
     * @param realm Realm to execute the command
     * @return CommandResult
     */
    public CommandResult execute() {
        if (unit != null) {
            unit.setCurrentOrder(null);
            unit.clearQueuedOrders();
            CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.UNIT_ORDERS_CLEARED_UPDATE);
            commandResult.putParameter("unit", unit);
            return commandResult;
        } else {
            return new CommandResult(CommandResult.RESULT_ERROR, "Unit can not be null");
        }
    }
}
