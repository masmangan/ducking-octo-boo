package org.freemars.colony;

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
public class RemoveFertilizerFromTileCommand extends AbstractCommand {

    private FreeMarsColony freeMarsColony;
    private Coordinate coordinate;
    private boolean addFertilizerToColonyStorage;

    public RemoveFertilizerFromTileCommand(Realm realm, FreeMarsColony freeMarsColony, Coordinate coordinate, boolean addFertilizerToColonyStorage) {
        super(realm);
        this.freeMarsColony = freeMarsColony;
        this.coordinate = coordinate;
        this.addFertilizerToColonyStorage = addFertilizerToColonyStorage;
    }

    public CommandResult execute() {
        getRealm().getTile(coordinate).removeCustomModifier("Fertilizer");
        freeMarsColony.removeFertilizedCoordinate(coordinate);
        if (addFertilizerToColonyStorage) {
            int fertilizerQuantityPerTile = Integer.parseInt(getRealm().getProperty("fertilizer_quantity_per_tile"));
            Resource fertilizerResource = getRealm().getResourceManager().getResource("Fertilizer");
            getExecutor().execute(new ResourceAddCommand(freeMarsColony, fertilizerResource, fertilizerQuantityPerTile));
        }
        return new CommandResult(CommandResult.RESULT_OK, "");
    }
}
