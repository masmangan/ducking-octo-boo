package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.unit.Unit;
import org.freerealm.unit.UnitAutomater;

/**
 * @author Deniz ARIKAN
 */
public class UnitSetAutomaterCommand extends AbstractCommand {

    private Unit unit;
    private UnitAutomater unitAutomater;

    public UnitSetAutomaterCommand(Unit unit, UnitAutomater unitAutomater) {
        this.unit = unit;
        this.unitAutomater = unitAutomater;
    }

    public CommandResult execute() {
        getExecutor().execute(new UnitOrdersClearCommand(unit));
        if (unit != null) {
            unit.setAutomater(unitAutomater);
            if (unitAutomater != null) {
                unitAutomater.setUnit(unit);
                unitAutomater.automate();
            }
            return new CommandResult(CommandResult.RESULT_OK, "");
        } else {
            return new CommandResult(CommandResult.RESULT_ERROR, "Unit can not be null");
        }
    }
}
