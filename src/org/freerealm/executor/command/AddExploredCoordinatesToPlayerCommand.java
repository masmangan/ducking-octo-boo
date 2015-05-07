package org.freerealm.executor.command;

import java.util.List;
import org.freerealm.executor.CommandResult;
import org.freerealm.map.Coordinate;
import org.freerealm.player.Player;

/**
 * @author Deniz ARIKAN
 */
public class AddExploredCoordinatesToPlayerCommand extends AbstractCommand {

    private final Player player;
    private final List<Coordinate> coordinates;

    public AddExploredCoordinatesToPlayerCommand(Player player, List<Coordinate> coordinates) {
        this.player = player;
        this.coordinates = coordinates;
    }

    public CommandResult execute() {
        CommandResult commandResult;
        if (player != null) {
            List<Coordinate> addedCoordinates = player.addExploredCoordinates(coordinates);
            if (!addedCoordinates.isEmpty()) {
                commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.EXPLORED_COORDINATES_ADDED_TO_PLAYER_UPDATE);
            } else {
                commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.NO_UPDATE);
            }
            commandResult.putParameter("player", player);
            commandResult.putParameter("added_coordinates", addedCoordinates);
        } else {
            commandResult = new CommandResult(CommandResult.RESULT_ERROR, "Player cannot be null");
        }
        return commandResult;
    }

}
