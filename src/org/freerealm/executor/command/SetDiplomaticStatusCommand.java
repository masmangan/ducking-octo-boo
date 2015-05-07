package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.Realm;
import org.freerealm.player.Player;

/**
 * Command class to set diplomatic status between two players
 * @author Deniz ARIKAN
 */
public class SetDiplomaticStatusCommand extends AbstractCommand {

    private Player player1;
    private Player player2;
    private int status;

    /**
     * Constructs a SetDiplomaticStatusCommand using two players and
     * new diplomatic status.
     * @param player1 First player
     * @param player2 Second player
     * @param status New diplomatic status
     */
    public SetDiplomaticStatusCommand(Realm realm, Player player1, Player player2, int status) {
        super(realm);
        this.player1 = player1;
        this.player2 = player2;
        this.status = status;
    }

    /**
     * Executes command to set new diplomatic status between given players.
     * @param realm Realm to execute the command
     * @return CommandResult
     */
    public CommandResult execute() {
        if (player1 == null || player2 == null) {
            return new CommandResult(CommandResult.RESULT_ERROR, "", CommandResult.NO_UPDATE);
        } else {
            getRealm().getDiplomacy().addPlayerRelation(player1, player2, status);
            return new CommandResult(CommandResult.RESULT_OK, "Players must not be null", CommandResult.DIPLOMATIC_STATUS_UPDATE);
        }
    }
}
