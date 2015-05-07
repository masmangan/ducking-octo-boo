package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.resource.Resource;
import org.freerealm.resource.ResourceStorer;

/**
 * Command class to transfer given amount of resource from a source resource
 * storer to destination. Upon execution this command will return an error if
 * Source does not have enough resource for transfer. If capacity of destination
 * is not enough for transfer then amount of resource for available space will
 * be transferred.
 * @author Deniz ARIKAN
 */
public class TransferResourceCommand extends AbstractCommand {

    private ResourceStorer source;
    private ResourceStorer destination;
    private Resource resource;
    private int amount = 0;

    /**
     * Constructs a TransferResourceCommand using source, destination, resource,
     * amount
     * @param source Source for resource transfer
     * @param destination Destination for resource transfer
     * @param resource Resource type
     * @param amount Amount to transfer
     */
    public TransferResourceCommand(ResourceStorer source, ResourceStorer destination, Resource resource, int amount) {
        this.source = source;
        this.destination = destination;
        this.resource = resource;
        this.amount = amount;
    }

    /**
     * Executes command to transfer given amount of resource from source to
     * destination.
     * @param realm Realm to execute the command
     * @return CommandResult
     */
    public CommandResult execute() {
        if (amount > source.getResourceQuantity(resource)) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Not enough resource");
        }
        if (destination.getRemainingCapacity(resource) < amount) {
            amount = destination.getRemainingCapacity(resource);
        }
        source.setResourceQuantity(resource, source.getResourceQuantity(resource) - amount);
        destination.setResourceQuantity(resource, destination.getResourceQuantity(resource) + amount);
        return new CommandResult(CommandResult.RESULT_OK, "", CommandResult.RESOURCE_TRANSFERRED_UPDATE);
    }
}
