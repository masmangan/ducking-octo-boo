package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.Realm;
import org.freerealm.player.Player;
import org.freerealm.property.ModifyStartingWealth;

/**
 * Command class to add a new player to realm.
 *
 * @author Deniz ARIKAN
 */
public class AddPlayerCommand extends AbstractCommand {

    private Player player;

    /**
     * Constructs an AddPlayerCommand using player.
     *
     * @param realm Realm to add player.
     * @param player New player to add to realm.
     */
    public AddPlayerCommand(Realm realm, Player player) {
        super(realm);
        this.player = player;
    }

    /**
     * Executes command and adds the given player to realm.
     *
     * @return CommandResult
     */
    public CommandResult execute() {
        int newPlayerId = player.getId();
        if (getRealm().getPlayerManager().getPlayer(newPlayerId) != null) {
            return new CommandResult(CommandResult.RESULT_ERROR, "A player with given id already exists");
        }
        getRealm().getPlayerManager().addPlayer(player);
        ModifyStartingWealth modifyStartingWealth = (ModifyStartingWealth) player.getProperty("ModifyStartingWealth");
        if (modifyStartingWealth != null) {
            player.setWealth(player.getWealth() + modifyStartingWealth.getModifier());
        }
        return new CommandResult(CommandResult.RESULT_OK, "");
    }
}
