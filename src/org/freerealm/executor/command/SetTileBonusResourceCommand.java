package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.map.Coordinate;
import org.freerealm.Realm;
import org.freerealm.resource.bonus.BonusResource;
import org.freerealm.tile.Tile;

/**
 * Command class to set a tile's bonus resource. If bonus resource is null
 * current bonus resource on given tile is removed.
 *
 * @author Deniz ARIKAN
 */
public class SetTileBonusResourceCommand extends AbstractCommand {

    private Tile tile;
    private Coordinate coordinate;
    private BonusResource bonusResource;

    /**
     * Constructs a SetTileBonusResourceCommand using tile, bonusResource
     *
     * @param tile Tile for setting vegetation
     * @param bonusResource New value for bonusResource
     */
    public SetTileBonusResourceCommand(Realm realm, Tile tile, BonusResource bonusResource) {
        super(realm);
        this.tile = tile;
        this.bonusResource = bonusResource;
    }

    /**
     * Constructs a SetTileBonusResourceCommand using coordinate, bonusResource
     *
     * @param coordinate Tile coordinate for setting vegetation
     * @param bonusResource New value for bonusResource
     */
    public SetTileBonusResourceCommand(Realm realm, Coordinate coordinate, BonusResource bonusResource) {
        super(realm);
        this.coordinate = coordinate;
        this.bonusResource = bonusResource;
    }

    /**
     * Executes command to set new bonus resource to given tile or coordinate.
     *
     * @param realm Realm to execute the command
     * @return CommandResult
     */
    public CommandResult execute() {
        if (tile == null) {
            if (coordinate == null) {
                return new CommandResult(CommandResult.RESULT_ERROR, "Coordinate cannot be null.");
            }
            tile = getRealm().getMap().getTile(coordinate);
        }
        if (tile == null) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Tile cannot be null.");
        }
        if (bonusResource != null && !bonusResource.canExistOnTileType(tile.getType())) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Given bonus resource cannot exist on this type of tile.");
        }
        tile.setBonusResource(bonusResource);
        CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "");
        commandResult.putParameter("coordinate", coordinate);
        return commandResult;
    }
}
