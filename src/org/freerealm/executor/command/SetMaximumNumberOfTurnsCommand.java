package org.freerealm.executor.command;

import org.freerealm.Realm;
import org.freerealm.executor.CommandResult;

/**
 *
 * @author Deniz ARIKAN
 */
public class SetMaximumNumberOfTurnsCommand extends AbstractCommand {

    private final int maximumNumberOfTurns;

    public SetMaximumNumberOfTurnsCommand(Realm realm, int maximumNumberOfTurns) {
        super(realm);
        this.maximumNumberOfTurns = maximumNumberOfTurns;
    }

    public CommandResult execute() {
        getRealm().setMaximumNumberOfTurns(maximumNumberOfTurns);
        return new CommandResult(CommandResult.RESULT_OK, "", CommandResult.NO_UPDATE);
    }

}
