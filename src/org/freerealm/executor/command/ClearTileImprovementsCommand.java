package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.map.Coordinate;
import org.freerealm.Realm;

/**
 * Command class to remove all improvements of a tile
 *
 * @author Deniz ARIKAN
 */
public class ClearTileImprovementsCommand extends AbstractCommand {

    private Coordinate coordinate;

    /**
     * Constructs a ClearTileImprovementsCommand using coordinate
     *
     * @param coordinate Tile coordinate to clear all improvements
     */
    public ClearTileImprovementsCommand(Realm realm, Coordinate coordinate) {
        super(realm);
        this.coordinate = coordinate;
    }

    /**
     * Executes command to remove all improvements of a tile
     *
     * @return CommandResult
     */
    public CommandResult execute() {
        getRealm().getMap().getTile(coordinate).clearImprovements();
        return new CommandResult(CommandResult.RESULT_OK, "");
    }
}
