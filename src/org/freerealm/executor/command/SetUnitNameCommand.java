package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.unit.Unit;

/**
 *
 * @author Deniz ARIKAN
 */
public class SetUnitNameCommand extends AbstractCommand {

    private Unit unit;
    private String name;

    public SetUnitNameCommand(Unit unit, String name) {
        this.unit = unit;
        this.name = name;
    }

    public CommandResult execute() {
        if (unit == null) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Unit cannot be null");
        }
        if (name == null) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Name cannot be null");
        }
        name = name.trim();
        if (name.equals("")) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Name cannot be empty");
        }
        unit.setName(name);
        return new CommandResult(CommandResult.RESULT_OK, "");
    }
}
