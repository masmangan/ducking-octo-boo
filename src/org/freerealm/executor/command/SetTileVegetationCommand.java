package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.map.Coordinate;
import org.freerealm.Realm;
import org.freerealm.tile.Tile;
import org.freerealm.vegetation.Vegetation;

/**
 * Command class to set a tile's vegetation. If vegetation is null current
 * vegetation on given tile is removed.
 *
 * @author Deniz ARIKAN
 */
public class SetTileVegetationCommand extends AbstractCommand {

    private Coordinate coordinate;
    private Vegetation vegetation;

    /**
     * Constructs a SetTileVegetationCommand using coordinate, vegetation
     *
     * @param coordinate Tile coordinate for setting vegetation
     * @param vegetation New value for vegetation
     */
    public SetTileVegetationCommand(Realm realm, Coordinate coordinate, Vegetation vegetation) {
        super(realm);
        this.coordinate = coordinate;
        this.vegetation = vegetation;
    }

    /**
     * Executes command to set new vegetation to given tile or coordinate.
     *
     * @param realm Realm to execute the command
     * @return CommandResult
     */
    public CommandResult execute() {
        if (coordinate == null) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Coordinate cannot be null.");
        }
        Tile tile = getRealm().getMap().getTile(coordinate);
        if (tile == null) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Tile cannot be null.");
        }
        if (vegetation != null && !vegetation.getType().canGrowOnTileType(tile.getType())) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Given vegetation  cannot grow on this type of tile.");
        }
        tile.setVegetation(vegetation);
        CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.VEGETATION_CHANGED_UPDATE);
        commandResult.putParameter("coordinate", coordinate);
        return commandResult;
    }
}
