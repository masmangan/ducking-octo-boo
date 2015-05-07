package org.freerealm.executor.command;

import org.freerealm.Realm;
import org.freerealm.executor.CommandResult;
import org.freerealm.unit.Order;
import org.freerealm.unit.Unit;

/**
 * Command class to assign an order to a given unit. When called, this command
 * will immediately execute the given order and if the order is completed with
 * this execution, unit's order will be set to null. If order is not completed
 * it will be set as unit's order. Passing a null order to
 * UnitOrderAssignCommand will cause a runtime exception.
 * <p>
 * If ordered unit was the player's active unit this command will not find and
 * activate player's next unit. If needed, the command caller must make next
 * unit active.
 *
 * @author Deniz ARIKAN
 */
public class UnitOrderAddCommand extends AbstractCommand {

    private Unit unit;
    private Order order;

    /**
     * Constructs a UnitOrderAddCommand using unit and order.
     *
     * @param unit New order will be assigned to this unit
     * @param order Order to assign
     */
    public UnitOrderAddCommand(Realm realm, Unit unit, Order order) {
        super(realm);
        this.unit = unit;
        this.order = order;
    }

    /**
     * Executes command and assigns new order to given unit.
     *
     * @param realm Realm to execute the command
     * @return CommandResult
     */
    public CommandResult execute() {
        if (unit != null) {
            unit.addOrder(order);
            getExecutor().execute(new UnitOrderExecuteCommand(getRealm(), unit));
            CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.UNIT_ORDER_ASSIGNED_UPDATE);
            commandResult.putParameter("unit", unit);
            commandResult.putParameter("order", order);
            return commandResult;
        } else {
            return new CommandResult(CommandResult.RESULT_ERROR, "Unit can not be null");
        }
    }
}
