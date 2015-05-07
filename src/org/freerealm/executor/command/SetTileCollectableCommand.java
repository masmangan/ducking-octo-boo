package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.tile.Collectable;
import org.freerealm.tile.Tile;

/**
 * @author Deniz ARIKAN
 */
public class SetTileCollectableCommand extends AbstractCommand {

    private Tile tile;
    private Collectable collectable;

    public SetTileCollectableCommand(Tile tile, Collectable collectable) {
        this.tile = tile;
        this.collectable = collectable;
    }

    public CommandResult execute() {
        if (tile == null) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Tile cannot be null", CommandResult.NO_UPDATE);
        }
        tile.setCollectable(collectable);
        CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.NO_UPDATE);
        commandResult.putParameter("tile", tile);
        commandResult.putParameter("collectable", collectable);
        return commandResult;
    }
}
