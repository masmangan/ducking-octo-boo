package org.freemars.earth.command;

import org.freemars.earth.EarthFlightModel;
import org.freemars.earth.Location;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.AbstractCommand;
import org.freerealm.unit.Unit;

/**
 *
 * @author Deniz ARIKAN
 */
public class RelocateUnitCommand extends AbstractCommand {

    private EarthFlightModel earthFlightModel;
    private Unit unit;
    private Location location;

    public RelocateUnitCommand(EarthFlightModel earthFlightModel, Unit unit, Location location) {
        this.earthFlightModel = earthFlightModel;
        this.unit = unit;
        this.location = location;
    }

    public CommandResult execute() {
        earthFlightModel.addUnitLocation(unit, location);
        CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "");
        commandResult.putParameter("unit", unit);
        commandResult.putParameter("location", location);
        return commandResult;
    }
}
