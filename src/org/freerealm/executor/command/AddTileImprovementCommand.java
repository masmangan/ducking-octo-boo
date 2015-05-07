package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.tile.Tile;
import org.freerealm.tile.improvement.TileImprovementType;

/**
 * Command class to add a tile improvement to given tile. Since some
 * improvements cannot be applied to some tile types this command can return an
 * error if given improvement is not available for tile's type.
 *
 * @author Deniz ARIKAN
 */
public class AddTileImprovementCommand extends AbstractCommand {

    private TileImprovementType improvement;
    private Tile tile;

    /**
     * Constructs an AddTileImprovementCommand using tile, improvement.
     *
     * @param tile Tile to add improvement
     * @param improvement New improvement
     */
    public AddTileImprovementCommand(Tile tile, TileImprovementType improvement) {
        this.improvement = improvement;
        this.tile = tile;
    }

    /**
     * Executes command to add a new improvement to tile. If given tile
     * improvement can not be build on this tile's type command returns an
     * error.
     *
     * @return CommandResult
     */
    public CommandResult execute() {
        if (improvement.canBeBuiltOnTileType(tile.getType())) {
            tile.addImprovement(improvement);
            CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.TILE_IMPROVEMENT_ADDED_UPDATE);
            commandResult.putParameter("tile", tile);
            commandResult.putParameter("improvement", improvement);
            return commandResult;
        }
        return new CommandResult(CommandResult.RESULT_ERROR, "");
    }
}
