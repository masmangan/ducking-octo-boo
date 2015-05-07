package org.freerealm.executor.command;

import org.freerealm.Realm;
import org.freerealm.executor.CommandResult;
import org.freerealm.settlement.Settlement;

/**
 * Command class to buy production points for a settlement. Cost per production
 * point is defined by <tt>production_point_cost</tt> property of the realm. If
 * player's wealth is not enough to buy the given production points
 * BuyCityProductionCommand will return an error.
 *
 * @author Deniz ARIKAN
 */
public class BuySettlementProductionCommand extends AbstractCommand {
    
    private Settlement settlement;
    private int amount = 0;

    /**
     * Constructs a BuyCityProductionCommand using settlement, amount.
     *
     * @param settlement Settlement to add production points
     * @param amount Production points to be bought
     */
    public BuySettlementProductionCommand(Realm realm, Settlement settlement, int amount) {
        super(realm);
        this.settlement = settlement;
        this.amount = amount;
    }

    /**
     * Executes command to buy production points in the given settlement.
     *
     * @return CommandResult
     */
    public CommandResult execute() {
        int currentWealth = settlement.getPlayer().getWealth();
        int productionPointCost = Integer.parseInt(getRealm().getProperty("production_point_cost"));
        if (currentWealth >= amount * productionPointCost) {
            settlement.getPlayer().setWealth(currentWealth - amount * productionPointCost);
            settlement.setProductionPoints(settlement.getProductionPoints() + amount);
        } else {
            return new CommandResult(CommandResult.RESULT_ERROR, "Not enough wealth");
        }
        return new CommandResult(CommandResult.RESULT_OK, "");
    }
}
