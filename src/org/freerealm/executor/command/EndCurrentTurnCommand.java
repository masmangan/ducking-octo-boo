package org.freerealm.executor.command;

import java.util.Iterator;
import org.freerealm.Realm;
import org.freerealm.executor.CommandResult;
import org.freerealm.player.Player;

/**
 *
 * @author Deniz ARIKAN
 */
public class EndCurrentTurnCommand extends AbstractCommand {

    public EndCurrentTurnCommand(Realm realm) {
        super(realm);
    }

    public CommandResult execute() {
        Iterator<Player> playerIterator = getRealm().getPlayerManager().getPlayersIterator();
        while (playerIterator.hasNext()) {
            getExecutor().execute(new EndPlayerTurnCommand(getRealm(), playerIterator.next()));
        }
        return new CommandResult(CommandResult.RESULT_OK, "", CommandResult.TURN_ENDED_UPDATE);
    }
}
