package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.player.Player;

/**
 * Command class to add given amount of wealth to a player.
 *
 * @author Deniz ARIKAN
 */
public class WealthAddCommand extends AbstractCommand {

    private Player player;
    private int amount = 0;

    /**
     * Constructs a WealthAddCommand using player, amount
     *
     * @param player Player to add wealth
     * @param amount Amount of wealth to add
     */
    public WealthAddCommand(Player player, int amount) {
        this.player = player;
        this.amount = amount;
    }

    /**
     * Executes command to add given amount of wealth to player
     *
     * @param realm Realm to execute the command
     * @return CommandResult
     */
    public CommandResult execute() {
        if (player != null) {
            player.setWealth(player.getWealth() + amount);
            return new CommandResult(CommandResult.RESULT_OK, "", CommandResult.PLAYER_WEALTH_ADDED_UPDATE);
        } else {
            return new CommandResult(CommandResult.RESULT_ERROR, "Player can not be null.");
        }
    }
}
