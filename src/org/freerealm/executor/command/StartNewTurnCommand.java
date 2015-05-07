package org.freerealm.executor.command;

import java.util.Iterator;
import org.freerealm.Realm;
import org.freerealm.executor.CommandResult;
import org.freerealm.player.Player;

/**
 * Command class to start a new turn in realm, it increases number of turns by
 * one and sets first player active.
 *
 * @author Deniz ARIKAN
 */
public class StartNewTurnCommand extends AbstractCommand {

    public StartNewTurnCommand(Realm realm) {
        super(realm);
    }

    /**
     * Executes command to start new turn.
     *
     * @return CommandResult
     */
    public CommandResult execute() {
        getRealm().setNumberOfTurns(getRealm().getNumberOfTurns() + 1);
        Iterator<Player> playerIterator = getRealm().getPlayerManager().getPlayersIterator();
        while (playerIterator.hasNext()) {
            getExecutor().execute(new StartPlayerTurnCommand(getRealm(), playerIterator.next()));
        }
        Player firstPlayer = getRealm().getPlayerManager().getFirstPlayer();
        getExecutor().execute(new SetActivePlayerCommand(getRealm(), firstPlayer));
        return new CommandResult(CommandResult.RESULT_OK, "", CommandResult.NEW_TURN_UPDATE);
    }
}
