package org.freemars.colony;

import java.util.Iterator;
import java.util.Vector;
import org.freerealm.Realm;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.AbstractCommand;
import org.freerealm.executor.command.ResourceAddCommand;
import org.freerealm.map.Coordinate;
import org.freerealm.resource.Resource;

/**
 *
 * @author Deniz ARIKAN
 */
public class RemoveFertilizerFromColonyTilesCommand extends AbstractCommand {

    private FreeMarsColony freeMarsColony;
    private boolean addFertilizerToColonyStorage;

    public RemoveFertilizerFromColonyTilesCommand(Realm realm, FreeMarsColony freeMarsColony, boolean addFertilizerToColonyStorage) {
        super(realm);
        this.freeMarsColony = freeMarsColony;
        this.addFertilizerToColonyStorage = addFertilizerToColonyStorage;
    }

    public CommandResult execute() {
        Vector<Coordinate> fertilizedCoordinates = new Vector<Coordinate>();
        Iterator<Coordinate> iterator = freeMarsColony.getFertilizedCoordinatesIterator();
        while (iterator.hasNext()) {
            fertilizedCoordinates.add(iterator.next());
        }
        iterator = fertilizedCoordinates.iterator();
        while (iterator.hasNext()) {
            Coordinate coordinate = iterator.next();
            getExecutor().execute(new RemoveFertilizerFromTileCommand(getRealm(), freeMarsColony, coordinate, addFertilizerToColonyStorage));
        }
        return new CommandResult(CommandResult.RESULT_OK, "");
    }
}
