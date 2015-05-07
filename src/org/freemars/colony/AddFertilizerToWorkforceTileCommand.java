package org.freemars.colony;

import org.freerealm.Realm;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.AbstractCommand;
import org.freerealm.property.ModifyResourceProduction;
import org.freerealm.resource.Resource;
import org.freerealm.settlement.workforce.WorkForce;
import org.freerealm.tile.FreeRealmTileModifier;
import org.freerealm.tile.Tile;

/**
 *
 * @author Deniz ARIKAN
 */
public class AddFertilizerToWorkforceTileCommand extends AbstractCommand {

    private FreeMarsColony freeMarsColony;
    private WorkForce workForce;

    public AddFertilizerToWorkforceTileCommand(Realm realm, FreeMarsColony freeMarsColony, WorkForce workForce) {
        super(realm);
        this.freeMarsColony = freeMarsColony;
        this.workForce = workForce;
    }

    public CommandResult execute() {
        if (!freeMarsColony.isCoordinateFertilized(workForce.getCoordinate())) {
            Resource fertilizerResource = getRealm().getResourceManager().getResource("Fertilizer");
            int fertilizerResourceQuantity = freeMarsColony.getResourceQuantity(fertilizerResource);
            int fertilizerQuantityPerTile = Integer.parseInt(getRealm().getProperty("fertilizer_quantity_per_tile"));
            if (fertilizerResourceQuantity >= fertilizerQuantityPerTile) {
                Resource foodResource = getRealm().getResourceManager().getResource(Resource.FOOD);
                FreeRealmTileModifier freeRealmTileModifier = new FreeRealmTileModifier();
                ModifyResourceProduction modifyResourceProduction = new ModifyResourceProduction();
                modifyResourceProduction.setResource(foodResource);
                modifyResourceProduction.setModifier(2);
                modifyResourceProduction.setModifyingOnlyIfResourceExists(true);
                freeRealmTileModifier.addProperty(modifyResourceProduction);
                Tile tile = getRealm().getTile(workForce.getCoordinate());
                tile.addCustomModifier("Fertilizer", freeRealmTileModifier);
                freeMarsColony.setResourceQuantity(fertilizerResource, fertilizerResourceQuantity - fertilizerQuantityPerTile);
                freeMarsColony.addFertilizedCoordinate(workForce.getCoordinate());
                return new CommandResult(CommandResult.RESULT_OK, "");
            } else {
                return new CommandResult(CommandResult.RESULT_ERROR, "");
            }
        }
        return new CommandResult(CommandResult.RESULT_OK, "");
    }
}
