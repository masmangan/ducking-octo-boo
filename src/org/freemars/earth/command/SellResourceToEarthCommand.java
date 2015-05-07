package org.freemars.earth.command;

import org.freemars.controller.FreeMarsController;
import org.freemars.earth.ModifyResourceEarthSellPrice;
import org.freemars.player.FreeMarsPlayer;
import org.freemars.player.ResourceTradeData;
import org.freerealm.Utility;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.AbstractCommand;
import org.freerealm.executor.command.ResourceRemoveCommand;
import org.freerealm.executor.command.WealthAddCommand;
import org.freerealm.resource.Resource;
import org.freerealm.unit.Unit;

/**
 * Command class to buy resources from Earth.
 *
 * @author Deniz ARIKAN
 */
public class SellResourceToEarthCommand extends AbstractCommand {

    private FreeMarsController freeMarsController;
    private Unit spaceShip;
    private Resource resource;
    private int amount = 0;

    /**
     * Constructs a SellResourceToEarthCommand using amount
     *
     * @param amount Amount to remove
     */
    public SellResourceToEarthCommand(FreeMarsController freeMarsController, Unit spaceShip, Resource resource, int amount) {
        this.freeMarsController = freeMarsController;
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
        CommandResult result = freeMarsController.execute(new ResourceRemoveCommand(spaceShip, resource, amount));
        if (result.getCode() == CommandResult.RESULT_OK) {
            FreeMarsPlayer player = (FreeMarsPlayer) spaceShip.getPlayer();
            int earthBuysAtPrice = freeMarsController.getFreeMarsModel().getEarthFlightModel().getEarthBuysAtPrice(resource);
            int totalPrice = earthBuysAtPrice * amount;
            int earthTaxRate = player.getEarthTaxRate();
            ModifyResourceEarthSellPrice modifyResourceEarthSellPrice = (ModifyResourceEarthSellPrice) player.getProperty("ModifyResourceEarthSellPrice");
            if (modifyResourceEarthSellPrice != null) {
                String priceModifiedResource = modifyResourceEarthSellPrice.getResource();
                int modifier = modifyResourceEarthSellPrice.getModifier();
                if (priceModifiedResource.equals(ModifyResourceEarthSellPrice.ALL)) {
                    totalPrice = (int) Utility.modifyByPercent(totalPrice, modifier);
                } else {
                    int modifiedResourceId = Integer.parseInt(modifyResourceEarthSellPrice.getResource());
                    if (resource.getId() == modifiedResourceId) {
                        totalPrice = (int) Utility.modifyByPercent(totalPrice, modifier);
                    }
                }
            }
            int taxAmount = totalPrice * earthTaxRate / 100;
            int netIncome = totalPrice - taxAmount;
            getExecutor().execute(new WealthAddCommand(player, netIncome));
            freeMarsController.getFreeMarsModel().getEarthFlightModel().addResource(resource, amount);

            ResourceTradeData resourceTradeData = player.getResourceTradeData(resource.getId());
            resourceTradeData.setQuantityExported(resourceTradeData.getQuantityExported() + amount);
            resourceTradeData.setIncomeBeforeTaxes(resourceTradeData.getIncomeBeforeTaxes() + totalPrice);
            resourceTradeData.setTax(resourceTradeData.getTax() + taxAmount);
            resourceTradeData.setNetIncome(resourceTradeData.getNetIncome() + netIncome);
            freeMarsController.execute(new SetFreeMarsPlayerTotalTaxPaidCommand(player, player.getTotalTaxPaid() + taxAmount));
            CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "");
            commandResult.putParameter("total_price", totalPrice);
            commandResult.putParameter("tax_rate", earthTaxRate);
            commandResult.putParameter("tax_amount", taxAmount);
            commandResult.putParameter("net_income", netIncome);
            return commandResult;
        }
        return new CommandResult(CommandResult.RESULT_ERROR, "");
    }
}
