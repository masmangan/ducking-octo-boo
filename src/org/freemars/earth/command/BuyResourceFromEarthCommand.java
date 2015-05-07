package org.freemars.earth.command;

import org.freemars.earth.EarthFlightModel;
import org.freemars.player.FreeMarsPlayer;
import org.freemars.player.ResourceTradeData;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.AbstractCommand;
import org.freerealm.executor.command.ResourceAddCommand;
import org.freerealm.executor.command.ResourceRemoveCommand;
import org.freerealm.executor.command.WealthRemoveCommand;
import org.freerealm.resource.Resource;
import org.freerealm.unit.Unit;

/**
 * Command class to buy resources from Earth.
 *
 * @author Deniz ARIKAN
 */
public class BuyResourceFromEarthCommand extends AbstractCommand {

    private EarthFlightModel earthFlightModel;
    private Unit spaceShip;
    private Resource resource;
    private int amount = 0;

    /**
     * Constructs a BuyResourceFromEarthCommand using player, amount
     *
     * @param player Player to remove wealth
     * @param amount Amount to remove
     */
    public BuyResourceFromEarthCommand(EarthFlightModel earthFlightModel, Unit spaceShip, Resource resource, int amount) {
        this.earthFlightModel = earthFlightModel;
        this.spaceShip = spaceShip;
        this.resource = resource;
        this.amount = amount;
    }

    /**
     * Executes command to ...
     *
     * @param realm Realm to execute the command
     * @return CommandResult
     */
    public CommandResult execute() {
        CommandResult result = getExecutor().execute(new ResourceAddCommand(spaceShip, resource, amount));
        if (result.getCode() == CommandResult.RESULT_OK) {
            FreeMarsPlayer player = (FreeMarsPlayer) spaceShip.getPlayer();
            int totalPrice = earthFlightModel.getEarthSellsAtPrice(resource) * amount;
            result = getExecutor().execute(new WealthRemoveCommand(player, totalPrice));
            if (result.getCode() == CommandResult.RESULT_OK) {
                earthFlightModel.removeResource(resource, amount);
                ResourceTradeData resourceTradeData = player.getResourceTradeData(resource.getId());
                resourceTradeData.setQuantityImported(resourceTradeData.getQuantityImported() + amount);
                resourceTradeData.setExpenditure(resourceTradeData.getExpenditure() + totalPrice);
                return new CommandResult(CommandResult.RESULT_OK, "");
            } else {
                getExecutor().execute(new ResourceRemoveCommand(spaceShip, resource, amount));
                return new CommandResult(CommandResult.RESULT_ERROR, "");
            }
        }
        return new CommandResult(CommandResult.RESULT_ERROR, "");
    }
}
