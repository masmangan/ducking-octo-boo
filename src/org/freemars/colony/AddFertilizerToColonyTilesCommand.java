package org.freemars.colony;

import java.util.Iterator;
import org.freerealm.Realm;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.AbstractCommand;
import org.freerealm.resource.Resource;
import org.freerealm.settlement.workforce.WorkForce;

/**
 *
 * @author Deniz ARIKAN
 */
public class AddFertilizerToColonyTilesCommand extends AbstractCommand {

    private FreeMarsColony freeMarsColony;

    public AddFertilizerToColonyTilesCommand(Realm realm, FreeMarsColony freeMarsColony) {
        super(realm);
        this.freeMarsColony = freeMarsColony;
    }

    public CommandResult execute() {
        boolean fertilizerQuantityEnough = true;
        Resource foodResource = getRealm().getResourceManager().getResource(Resource.FOOD);
        Iterator<WorkForce> iterator = freeMarsColony.getWorkForceManager().getWorkForceIterator();
        while (iterator.hasNext() && fertilizerQuantityEnough) {
            WorkForce workForce = iterator.next();
            if (workForce.getResource().equals(foodResource)) {
                CommandResult commandResult = getExecutor().execute(new AddFertilizerToWorkforceTileCommand(getRealm(), freeMarsColony, workForce));
                if (commandResult.getCode() == CommandResult.RESULT_ERROR) {
                    fertilizerQuantityEnough = false;
                }
            }
        }
        if (fertilizerQuantityEnough) {
            return new CommandResult(CommandResult.RESULT_OK, "");
        } else {
            return new CommandResult(CommandResult.RESULT_ERROR, "");
        }
    }
}
