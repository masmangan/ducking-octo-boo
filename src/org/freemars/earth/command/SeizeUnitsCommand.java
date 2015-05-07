package org.freemars.earth.command;

import java.util.Iterator;
import java.util.Vector;
import org.freemars.controller.FreeMarsController;
import org.freemars.earth.Location;
import org.freemars.model.FreeMarsModel;
import org.freemars.player.FreeMarsPlayer;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.AbstractCommand;
import org.freerealm.executor.command.RemoveUnitCommand;
import org.freerealm.unit.Unit;

/**
 *
 * @author Deniz ARIKAN
 */
public class SeizeUnitsCommand extends AbstractCommand {

    private FreeMarsController freeMarsController;
    private FreeMarsPlayer freeMarsPlayer;
    private String[] seizedUnitTypeNames = {"Colonizer", "Transporter", "Mech"};

    public SeizeUnitsCommand(FreeMarsController freeMarsController, FreeMarsPlayer freeMarsPlayer) {
        this.freeMarsController = freeMarsController;
        this.freeMarsPlayer = freeMarsPlayer;
    }

    public CommandResult execute() {
        Vector<Unit> seizedUnits = new Vector<Unit>();
        Iterator<Unit> iterator = freeMarsPlayer.getUnitsIterator();
        while (iterator.hasNext()) {
            Unit unit = iterator.next();
            if (willUnitBeSeized(unit)) {
                seizedUnits.add(unit);
            }
        }
        for (Unit seizedUnit : seizedUnits) {
            freeMarsController.getFreeMarsModel().getEarthFlightModel().removeUnitLocation(seizedUnit);
            RemoveUnitCommand removeUnitCommand = new RemoveUnitCommand(freeMarsController.getFreeMarsModel().getRealm(), freeMarsPlayer, seizedUnit);
            freeMarsController.execute(removeUnitCommand);
        }
        CommandResult commandResult;
        if (seizedUnits.size() > 0) {
            commandResult = new CommandResult(CommandResult.RESULT_OK, "", FreeMarsModel.UNITS_SEIZED_UPDATE);
            commandResult.putParameter("player", freeMarsPlayer);
            commandResult.putParameter("seized_units", seizedUnits);
        } else {
            commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.NO_UPDATE);
        }
        return commandResult;
    }

    private boolean willUnitBeSeized(Unit unit) {
        String unitTypeName = unit.getType().getName();
        for (int i = 0; i < seizedUnitTypeNames.length; i++) {
            String seizedUnitTypeName = seizedUnitTypeNames[i];
            if (unitTypeName.equals(seizedUnitTypeName)) {
                Location unitLocation = freeMarsController.getFreeMarsModel().getEarthFlightModel().getUnitLocation(unit);
                if (unitLocation != null && (unitLocation.equals(Location.EARTH) || unitLocation.equals(Location.TRAVELING_TO_EARTH))) {
                    return true;
                }
            }
        }
        return false;
    }
}
