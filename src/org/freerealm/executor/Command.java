package org.freerealm.executor;

/**
 * The interface which defines an execute method
 *
 * @author Deniz ARIKAN
 */
public interface Command {

    public Executor getExecutor();

    public void setExecutor(Executor executor);

    /**
     * Executes the command.
     *
     * @return CommandResult
     */
    public CommandResult execute();
}
