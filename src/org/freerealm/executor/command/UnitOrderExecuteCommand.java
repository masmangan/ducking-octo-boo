package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.Realm;
import org.freerealm.unit.Order;
import org.freerealm.unit.Unit;

/**
 * Command class to execute next available order of a given unit. When called,
 * this command will get first order from order queue of unit and execute it. If
 * order is complete after execution, the command will remove it from order
 * queue.<br>
 * If queue is already empty when command is executed it will return RESULT_OK.
 *
 * @author Deniz ARIKAN
 */
public class UnitOrderExecuteCommand extends AbstractCommand {

    private final Unit unit;

    /**
     * Constructs a UnitOrderExecuteCommand using unit.
     *
     * @param unit New order will be assigned to this unit
     */
    public UnitOrderExecuteCommand(Realm realm, Unit unit) {
        super(realm);
        this.unit = unit;
    }

    /**
     * Runs command and executes next available order of given unit.
     *
     * @return CommandResult
     */
    public CommandResult execute() {
        if (unit != null) {
            boolean continueExecution = true;
            while (continueExecution) {
                if (unit.getCurrentOrder() == null || unit.getCurrentOrder().isComplete()) {
                    Order nextOrder = unit.getNextOrder();
                    if (nextOrder != null && nextOrder.isExecutable()) {
                        nextOrder.setTurnGiven(getRealm().getNumberOfTurns());
                        unit.setCurrentOrder(nextOrder);
                        unit.removeOrder(nextOrder);
                    } else {
                        unit.setCurrentOrder(null);
                    }
                }
                if (unit.getCurrentOrder() != null) {
                    unit.getCurrentOrder().setRealm(getRealm());
                    unit.getCurrentOrder().setExecutor(getExecutor());
                    unit.getCurrentOrder().execute();
                    if (unit.getCurrentOrder() == null || !unit.getCurrentOrder().isComplete()) {
                        continueExecution = false;
                    }
                } else {
                    continueExecution = false;
                }
            }
            CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.UNIT_ORDER_EXECUTED_UPDATE);
            commandResult.putParameter("unit", unit);
            commandResult.putParameter("order", unit.getCurrentOrder());
            return commandResult;
        } else {
            return new CommandResult(CommandResult.RESULT_ERROR, "Unit can not be null");
        }
    }
}
