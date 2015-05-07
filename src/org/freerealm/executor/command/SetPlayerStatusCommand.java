package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.player.Player;

/**
 * Command class to set a player's status.
 * 
 * @author Deniz ARIKAN
 */
public class SetPlayerStatusCommand extends AbstractCommand {

    private Player player;
    private int status;

    /**
     * Constructs an SetPlayerStatusCommand using player, status.
     * @param player Player to set status
     * @param status New status
     */
    public SetPlayerStatusCommand(Player player, int status) {
        this.player = player;
        this.status = status;
    }

    /**
     * Executes command and set the given status to player.
     * @param realm Realm to execute the command
     * @return CommandResult
     */
    public CommandResult execute() {
        player.setStatus(status);
        return new CommandResult(CommandResult.RESULT_OK, "");
    }
}
