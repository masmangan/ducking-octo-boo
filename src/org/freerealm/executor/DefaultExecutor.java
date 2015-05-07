package org.freerealm.executor;

/**
 *
 * @author Deniz ARIKAN
 */
public class DefaultExecutor implements Executor {

    public CommandResult execute(Command command) {
        CommandResult commandResult = command.execute();
        return commandResult;
    }

}
