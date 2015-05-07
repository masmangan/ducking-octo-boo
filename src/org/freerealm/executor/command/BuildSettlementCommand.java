package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.settlement.Settlement;
import java.util.Iterator;
import org.freerealm.Realm;
import org.freerealm.property.BuildSettlementProperty;
import org.freerealm.property.Property;
import org.freerealm.settlement.FreeRealmSettlement;
import org.freerealm.tile.Collectable;
import org.freerealm.unit.Unit;

/**
 * Command class to build a new settlement using the given unit.
 *
 * Upon execution this command will return an error if:
 * <ul>
 * <li>Settlement name is null or an empty string.</li>
 * <li>Tile already has a settlement.</li>
 * <li>Given unit does not have "BuildSettlement" property.</li>
 * </ul>
 *
 * If no error is fired after checking these conditions BuildSettlementCommand
 * will add a new settlement to unit's coordinate and remove the unit. Initial
 * population of settlement will be <tt>settlement_founding_population</tt>
 * property of the realm.
 * <p>
 * If settlement building unit was the player's active unit this command will
 * not find and activate player's next unit, normally active unit will be null.
 * If needed, the command caller must make next unit active.
 *
 * @author Deniz ARIKAN
 */
public class BuildSettlementCommand extends AbstractCommand {

    private String settlementName;
    private Unit unit;
    private Settlement settlement;

    /**
     * Constructs a BuildSettlementCommand using unit and settlementName.
     *
     * @param unit Unit to build the settlement, must have "BuildSettlement"
     * property
     * @param settlementName Name of new settlement
     */
    public BuildSettlementCommand(Realm realm, Unit unit, String settlementName, Settlement settlement) {
        super(realm);
        this.unit = unit;
        this.settlementName = settlementName;
        this.settlement = settlement;
    }

    /**
     * Executes command and adds a new settlement to realm belonging to unit's
     * player.
     *
     * @return CommandResult
     */
    public CommandResult execute() {
        if ((settlementName == null) || (settlementName.trim().equals(""))) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Settlement name cannot be empty", CommandResult.NO_UPDATE);
        }
        if (getBuildSettlementProperty(unit) == null) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Unit is not able to found new settlements", CommandResult.NO_UPDATE);
        }
        if (getRealm().getTile(unit.getCoordinate()).getSettlement() != null) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Tile alredy contains a settlement", CommandResult.NO_UPDATE);
        }
        if (settlement == null) {
            settlement = new FreeRealmSettlement(getRealm());
        }
        Collectable collectable = getRealm().getTile(unit.getCoordinate()).getCollectable();
        if (collectable != null) {
            getExecutor().execute(new ProcessCollectableCommand(getRealm(), unit, collectable));
        }
        settlement.setCoordinate(unit.getCoordinate());
        settlement.setPlayer(unit.getPlayer());
        settlement.setName(settlementName);
        int settlementFoundingPopulation = Integer.parseInt(getRealm().getProperty("settlement_founding_population"));
        settlement.setPopulation(settlementFoundingPopulation);
        unit.getPlayer().addSettlement(settlement);
        getExecutor().execute(new RemoveUnitCommand(getRealm(), unit.getPlayer(), unit));
        getRealm().getMap().addSettlement(settlement);
        CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.NEW_SETTLEMENT_UPDATE);
        commandResult.putParameter("settlement", settlement);
        commandResult.putParameter("properties", getProperties());
        return commandResult;
    }

    private BuildSettlementProperty getBuildSettlementProperty(Unit unit) {
        Iterator<Property> propertyIterator = unit.getType().getPropertiesIterator();
        while (propertyIterator.hasNext()) {
            Property property = propertyIterator.next();
            if (property instanceof BuildSettlementProperty) {
                return (BuildSettlementProperty) property;
            }
        }
        return null;
    }
}
