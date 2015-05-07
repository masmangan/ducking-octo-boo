package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.player.Player;

/**
 *
 * @author Deniz ARIKAN
 */
public class SignalPlayerEndTurnCommand extends AbstractCommand {

    private Player player;

    public SignalPlayerEndTurnCommand(Player player) {
        this.player = player;
    }

    public CommandResult execute() {
        if (player != null) {
            player.setTurnEnded(true);
            return new CommandResult(CommandResult.RESULT_OK, "", CommandResult.PLAYER_END_TURN_UPDATE);
        } else {
            return new CommandResult(CommandResult.RESULT_ERROR, "Player can not be null");
        }
    }
}
