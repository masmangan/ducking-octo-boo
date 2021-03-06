package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.resource.Resource;
import org.freerealm.resource.ResourceStorer;

/**
 * Command class to add given amount of resource to a resource storer. If
 * capacity of destination is not enough then command will return an error.
 * @author Deniz ARIKAN
 */
public class ResourceAddCommand extends AbstractCommand {

    private ResourceStorer storer;
    private Resource resource;
    private int amount = 0;

    /**
     * Constructs a ResourceAddCommand using storer, resource, amount
     * @param storer Resource storer to add new resource
     * @param resource Resource type
     * @param amount Amount to transfer
     */
    public ResourceAddCommand(ResourceStorer storer, Resource resource, int amount) {
        this.storer = storer;
        this.resource = resource;
        this.amount = amount;
    }

    /**
     * Executes command to add given amount of resource to resource storer
     * @param realm Realm to execute the command
     * @return CommandResult
     */
    public CommandResult execute() {
        if (storer.getRemainingCapacity(resource) < amount) {
            return new CommandResult(CommandResult.RESULT_ERROR, "");
        }
        storer.setResourceQuantity(resource, storer.getResourceQuantity(resource) + amount);
        return new CommandResult(CommandResult.RESULT_OK, "");
    }
}
