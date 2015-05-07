package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.player.Player;

/**
 * Command class to remove given amount of wealth from a player.
 *
 * @author Deniz ARIKAN
 */
public class WealthRemoveCommand extends AbstractCommand {

    private Player player;
    private int amount = 0;

    /**
     * Constructs a WealthRemoveCommand using player, amount
     *
     * @param player Player to remove wealth
     * @param amount Amount to remove
     */
    public WealthRemoveCommand(Player player, int amount) {
        this.player = player;
        this.amount = amount;
    }

    /**
     * Executes command to remove given amount of wealth from player
     *
     * @return CommandResult
     */
    public CommandResult execute() {
        if (player.getWealth() < amount) {
            return new CommandResult(CommandResult.RESULT_ERROR, "");
        }
        player.setWealth(player.getWealth() - amount);
        return new CommandResult(CommandResult.RESULT_OK, "", CommandResult.PLAYER_WEALTH_REMOVED_UPDATE);
    }
}
