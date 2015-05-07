package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.resource.Resource;
import org.freerealm.resource.ResourceStorer;

/**
 * Command class to remove given amount of resource from a resource storer. If
 * storer does not have given amount for that type of resource then command
 * return an error.
 * @author Deniz ARIKAN
 */
public class ResourceRemoveCommand extends AbstractCommand {

    private ResourceStorer storer;
    private Resource resource;
    private int amount = 0;

    /**
     * Constructs a ResourceRemoveCommand using storer, resource, amount
     * @param storer Resource storer to remove resource
     * @param resource Resource type
     * @param amount Amount to transfer
     */
    public ResourceRemoveCommand(ResourceStorer storer, Resource resource, int amount) {
        this.storer = storer;
        this.resource = resource;
        this.amount = amount;
    }

    /**
     * Executes command to remove given amount of resource from resource storer
     * @param realm Realm to execute the command
     * @return CommandResult
     */
    public CommandResult execute() {
        if (storer.getResourceQuantity(resource) < amount) {
            return new CommandResult(CommandResult.RESULT_ERROR, "");
        }
        storer.setResourceQuantity(resource, storer.getResourceQuantity(resource) - amount);
        return new CommandResult(CommandResult.RESULT_OK, "");
    }
}
