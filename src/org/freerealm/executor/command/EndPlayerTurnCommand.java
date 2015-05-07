package org.freerealm.executor.command;

import java.util.ArrayList;
import org.freerealm.executor.CommandResult;
import org.freerealm.settlement.Settlement;
import org.freerealm.settlement.improvement.SettlementImprovementType;
import org.freerealm.Realm;
import java.util.Iterator;
import org.freerealm.modifier.GeneralModifier;
import org.freerealm.player.NotEnoughInputResourceForSettlementImprovementMessage;
import org.freerealm.settlement.SettlementBuildable;
import org.freerealm.settlement.improvement.SettlementImprovement;
import org.freerealm.resource.Resource;
import org.freerealm.player.NotEnoughPopulationForProductionMessage;
import org.freerealm.player.NotEnoughResourceForProductionMessage;
import org.freerealm.player.Player;
import org.freerealm.player.ResourceWasteMessage;
import org.freerealm.player.SettlementImprovementCompletedMessage;
import org.freerealm.player.UnitCompletedMessage;
import org.freerealm.property.BuildableProperty;
import org.freerealm.property.RemoveSettlementImprovementProperty;
import org.freerealm.settlement.RequiredPopulationResourceAmountCalculator;
import org.freerealm.settlement.SettlementBuildableCostCalculator;
import org.freerealm.settlement.SettlementModifier;
import org.freerealm.settlement.improvement.SettlementImprovementResourceProductionModel;
import org.freerealm.unit.Unit;
import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public class EndPlayerTurnCommand extends AbstractCommand {

    private Player player;

    public EndPlayerTurnCommand(Realm realm, Player player) {
        super(realm);
        this.player = player;
    }

    public CommandResult execute() {
        manageWealth();
        manageSettlements();
        manageUnits();
        return new CommandResult(CommandResult.RESULT_OK, "");
    }

    private void manageWealth() {
        player.setWealth(player.getWealth() + player.getTotalIncome() - player.getTotalExpenses());
    }

    private void manageSettlements() {
        Iterator<Settlement> settlementIterator = player.getSettlementsIterator();
        while (settlementIterator.hasNext()) {
            endTurnSettlement(settlementIterator.next());
        }
    }

    private void manageUnits() {
        ArrayList<Unit> temporaryUnitList = new ArrayList<Unit>();
        Iterator<Unit> unitIterator = player.getUnitsIterator();
        while (unitIterator.hasNext()) {
            temporaryUnitList.add(unitIterator.next());
        }
        for (Unit unit : temporaryUnitList) {
            endTurnUnit(unit);
        }
    }

    private void endTurnUnit(Unit unit) {
        if (unit.getCurrentOrder() != null || unit.getNextOrder() != null) {
            getExecutor().execute(new UnitOrderExecuteCommand(getRealm(), unit));
        }
    }

    private void endTurnSettlement(Settlement settlement) {
        manageSettlementTerrainResourceProduction(settlement);
        manageSettlementImprovementsResourceProduction(settlement);
        manageSettlementProductionPoints(settlement);
        manageSettlementPopulation(settlement);
        manageSettlementProduction(settlement);
        manageSettlementStorage(settlement);
        manageSettlementAutomanagedResources(settlement);
    }

    private void manageSettlementTerrainResourceProduction(Settlement settlement) {
        Iterator resourceIterator = getRealm().getResourceManager().getResourcesIterator();
        while (resourceIterator.hasNext()) {
            Resource resource = (Resource) resourceIterator.next();
            settlement.setResourceQuantity(resource, settlement.getResourceQuantity(resource) + settlement.getResourceProductionFromTerrain(resource));
        }
    }

    /**
     * Manage resources produced in colony improvements like factories, plants
     * or solar arrays
     *
     * @param settlement
     */
    private void manageSettlementImprovementsResourceProduction(Settlement settlement) {
        Iterator<SettlementImprovement> iterator = settlement.getImprovementsIterator();
        while (iterator.hasNext()) {
            SettlementImprovement settlementImprovement = iterator.next();
            if (settlementImprovement.isEnabled()) {
                manageProducerImprovement(settlement, settlementImprovement);
            }
        }
    }

    private void manageProducerImprovement(Settlement settlement, SettlementImprovement settlementImprovement) {
        if (settlementImprovement.getType().getInputResourceCount() > 0 || settlementImprovement.getType().getOutputResourceCount() > 0) {
            SettlementImprovementResourceProductionModel settlementImprovementResourceProductionModel = new SettlementImprovementResourceProductionModel(settlement, settlementImprovement);
            Iterator<Resource> inputResourcesIterator = settlementImprovement.getType().getInputResourcesIterator();
            if (inputResourcesIterator != null) {
                while (inputResourcesIterator.hasNext()) {
                    Resource resource = inputResourcesIterator.next();
                    if (settlement.getResourceQuantity(resource) < settlementImprovement.getType().getInputQuantity(resource) && settlementImprovement.getNumberOfWorkers() > 0) {
                        NotEnoughInputResourceForSettlementImprovementMessage notEnoughInputResourceForSettlementImprovementMessage = new NotEnoughInputResourceForSettlementImprovementMessage();
                        notEnoughInputResourceForSettlementImprovementMessage.setSubject("Not enough input resource");
                        StringBuffer messageText = new StringBuffer("Settlement of " + settlement.getName() + " does not have enough " + resource.getName().toLowerCase() + " to use in " + settlementImprovement.getType().getName() + ".");
                        notEnoughInputResourceForSettlementImprovementMessage.setText(messageText);
                        notEnoughInputResourceForSettlementImprovementMessage.setTurnSent(getRealm().getNumberOfTurns());
                        notEnoughInputResourceForSettlementImprovementMessage.setSettlement(settlement);
                        notEnoughInputResourceForSettlementImprovementMessage.setSettlementImprovement(settlementImprovement);
                        notEnoughInputResourceForSettlementImprovementMessage.setResource(resource);
                        settlement.getPlayer().addMessage(notEnoughInputResourceForSettlementImprovementMessage);
                    } else {
                        getExecutor().execute(new ResourceRemoveCommand(settlement, resource, settlementImprovementResourceProductionModel.getInputAmount(resource)));
                    }
                }
            }
            Iterator<Resource> outputResourcesIterator = settlementImprovement.getType().getOutputResourcesIterator();
            if (outputResourcesIterator != null) {
                while (outputResourcesIterator.hasNext()) {
                    Resource resource = outputResourcesIterator.next();
                    getExecutor().execute(new ResourceAddCommand(settlement, resource, settlementImprovementResourceProductionModel.getOutputAmount(resource)));
                }
            }
        }
    }

    private void manageSettlementProductionPoints(Settlement settlement) {
        settlement.setProductionPoints(settlement.getProductionPoints() + settlement.getProductionPointsPerTurn());
    }

    private void manageSettlementPopulation(Settlement settlement) {
        boolean isSettlementPopulationIncreasing = true;
        Iterator<Integer> iterator = getRealm().getRequiredPopulationResourcesIterator();
        while (iterator.hasNext()) {
            Integer resourceId = iterator.next();
            Resource resource = getRealm().getResourceManager().getResource(resourceId);
            GeneralModifier[] modifiers = new GeneralModifier[]{player};
            RequiredPopulationResourceAmountCalculator requiredPopulationResourceAmountCalculator = new RequiredPopulationResourceAmountCalculator(getRealm(), resource, modifiers);
            int requiredPopulationResourceAmount = requiredPopulationResourceAmountCalculator.getRequiredPopulationResourceAmount();
            int requiredAmount = requiredPopulationResourceAmount * settlement.getPopulation();
            if (requiredAmount > settlement.getResourceQuantity(resource)) {
                isSettlementPopulationIncreasing = false;
                settlement.setResourceQuantity(resource, 0);
            } else {
                settlement.setResourceQuantity(resource, settlement.getResourceQuantity(resource) - requiredAmount);
            }
        }
        if (isSettlementPopulationIncreasing) {
            double populationChange = (settlement.getPopulation() * settlement.getPopulationIncreasePercent()) / 100;
            if (populationChange > 0 && populationChange < 1) {
                populationChange = 1;
            } else {
                populationChange = Math.floor(populationChange);
            }
            settlement.setPopulation(settlement.getPopulation() + (int) populationChange);
        } else {
            int populationChange = (int) Math.ceil(settlement.getPopulation() * settlement.getPopulationDecreasePercent() / 100);
            settlement.setPopulation(settlement.getPopulation() - populationChange);
        }
    }

    private void manageSettlementProduction(Settlement settlement) {
        int productionPoints = settlement.getProductionPoints();
        SettlementBuildable currentProduction = settlement.getCurrentProduction();
        if (currentProduction != null) {
            int currentProductionCost = new SettlementBuildableCostCalculator(currentProduction, new SettlementModifier[]{settlement.getPlayer()}).getCost();
            if (productionPoints >= currentProductionCost && settlement.canBuild(currentProduction)) {
                if (currentProduction instanceof SettlementImprovementType) {
                    SettlementImprovementType settlementImprovementType = (SettlementImprovementType) currentProduction;
                    settlement.setProductionPoints(productionPoints - currentProductionCost);
                    getExecutor().execute(new AddSettlementImprovementCommand(settlement, settlementImprovementType));
                    settlement.removeFromProductionQueue(0);
                    RemoveSettlementImprovementProperty removeSettlementImprovement = (RemoveSettlementImprovementProperty) settlementImprovementType.getProperty(RemoveSettlementImprovementProperty.NAME);
                    if (removeSettlementImprovement != null) {
                        getExecutor().execute(new RemoveSettlementImprovementCommand(settlement, removeSettlementImprovement.getSettlementImprovementId()));
                    }
                    StringBuffer messageText = new StringBuffer("Settlement of " + settlement.getName() + " has completed " + currentProduction);
                    SettlementImprovementCompletedMessage message = new SettlementImprovementCompletedMessage();
                    message.setSubject("Building complete");
                    message.setText(messageText);
                    message.setTurnSent(getRealm().getNumberOfTurns());
                    message.setSettlement(settlement);
                    message.setSettlementImprovementType((SettlementImprovementType) currentProduction);
                    message.setNextProduction(settlement.getCurrentProduction());
                    settlement.getPlayer().addMessage(message);
                } else if (currentProduction instanceof UnitType) {
                    UnitType unitType = (UnitType) currentProduction;
                    BuildableProperty buildableProperty = (BuildableProperty) unitType.getProperty("buildable_property");
                    if (buildableProperty != null) {
                        int populationCost = buildableProperty.getPopulationCost();
                        if (settlement.getPopulation() < populationCost) {
                            StringBuffer messageText = new StringBuffer("Not enough population for " + unitType.getName().toLowerCase() + " production in " + settlement.getName());
                            NotEnoughPopulationForProductionMessage notEnoughPopulationForProductionMessage = new NotEnoughPopulationForProductionMessage();
                            notEnoughPopulationForProductionMessage.setSettlement(settlement);
                            notEnoughPopulationForProductionMessage.setText(messageText);
                            notEnoughPopulationForProductionMessage.setTurnSent(getRealm().getNumberOfTurns());
                            settlement.getPlayer().addMessage(notEnoughPopulationForProductionMessage);
                        } else if (enoughResourcesForProduction(settlement, currentProduction, buildableProperty)) {
                            Iterator<Integer> resourceIdsIterator = buildableProperty.getBuildCostResourceIdsIterator();
                            while (resourceIdsIterator.hasNext()) {
                                Integer resourceId = resourceIdsIterator.next();
                                Resource resource = getRealm().getResourceManager().getResource(resourceId);
                                settlement.setResourceQuantity(resource, settlement.getResourceQuantity(resource) - buildableProperty.getBuildCostResourceQuantity(resourceId));
                            }
                            settlement.setProductionPoints(productionPoints - currentProductionCost);
                            SetSettlementPopulationCommand setSettlementPopulationCommand = new SetSettlementPopulationCommand(settlement, settlement.getPopulation() - populationCost);
                            getExecutor().execute(setSettlementPopulationCommand);
                            Unit newUnit = new Unit(getRealm());
                            newUnit.setType(unitType);
                            newUnit.setCoordinate(settlement.getCoordinate());
                            newUnit.setPlayer(settlement.getPlayer());
                            getExecutor().execute(new AddUnitCommand(getRealm(), settlement.getPlayer(), newUnit));
                            if ((settlement.getPlayer().getActiveUnit() == null)) {
                                getExecutor().execute(new SetActiveUnitCommand(settlement.getPlayer(), newUnit));
                            }
                            if (!settlement.isContiuousProduction()) {
                                settlement.removeFromProductionQueue(0);
                            }
                            UnitCompletedMessage unitCompletedMessage = new UnitCompletedMessage();
                            unitCompletedMessage.setSubject("Unit complete");
                            StringBuffer messageText = new StringBuffer("Settlement of " + settlement.getName() + " has completed " + currentProduction);
                            unitCompletedMessage.setText(messageText);
                            unitCompletedMessage.setTurnSent(getRealm().getNumberOfTurns());
                            unitCompletedMessage.setSettlement(settlement);
                            unitCompletedMessage.setUnit(newUnit);
                            unitCompletedMessage.setNextProduction(settlement.getCurrentProduction());
                            unitCompletedMessage.setContiuousProduction(settlement.isContiuousProduction());
                            settlement.getPlayer().addMessage(unitCompletedMessage);
                        }
                    }
                }
            }
        }
    }

    private boolean enoughResourcesForProduction(Settlement settlement, SettlementBuildable settlementBuildable, BuildableProperty buildableProperty) {
        Iterator<Integer> resourceIdsIterator = buildableProperty.getBuildCostResourceIdsIterator();
        while (resourceIdsIterator.hasNext()) {
            Integer resourceId = resourceIdsIterator.next();
            Resource resource = getRealm().getResourceManager().getResource(resourceId);
            if (settlement.getResourceQuantity(resource) < buildableProperty.getBuildCostResourceQuantity(resourceId)) {
                StringBuffer messageText = new StringBuffer("Not enough " + resource.getName().toLowerCase() + " for " + settlementBuildable.getName().toLowerCase() + " production in " + settlement.getName());
                NotEnoughResourceForProductionMessage notEnoughResourceForProductionMessage = new NotEnoughResourceForProductionMessage();
                notEnoughResourceForProductionMessage.setText(messageText);
                notEnoughResourceForProductionMessage.setTurnSent(getRealm().getNumberOfTurns());
                notEnoughResourceForProductionMessage.setSettlement(settlement);
                notEnoughResourceForProductionMessage.setResource(resource);
                settlement.getPlayer().addMessage(notEnoughResourceForProductionMessage);
                return false;
            }
        }
        return true;
    }

    private void manageSettlementStorage(Settlement settlement) {
        Iterator resourceIterator = getRealm().getResourceManager().getResourcesIterator();
        while (resourceIterator.hasNext()) {
            Resource resource = (Resource) resourceIterator.next();
            int storage = settlement.getTotalCapacity(resource);
            if (settlement.getResourceQuantity(resource) > storage) {
                int wastedAmount = settlement.getResourceQuantity(resource) - storage;
                StringBuffer messageText = new StringBuffer(wastedAmount + " units of " + resource + " is wasted in " + settlement.getName());
                ResourceWasteMessage message = new ResourceWasteMessage();
                message.setText(messageText);
                message.setTurnSent(getRealm().getNumberOfTurns());
                message.setSettlement(settlement);
                message.setResource(resource);
                player.addMessage(message);
                settlement.setResourceQuantity(resource, storage);
            } else {
                int resourceProduction = settlement.getResourceProductionFromTerrain(resource);
                int nextTurnResourceAmount = settlement.getResourceQuantity(resource) + resourceProduction;
                if (nextTurnResourceAmount > storage) {
                    int willBeWastedAmount = settlement.getResourceQuantity(resource) + resourceProduction - storage;
                    StringBuffer messageText = new StringBuffer("With current production " + willBeWastedAmount + " units of " + resource + " will be wasted in " + settlement.getName() + " next turn");
                    ResourceWasteMessage message = new ResourceWasteMessage();
                    message.setText(messageText);
                    message.setTurnSent(getRealm().getNumberOfTurns());
                    message.setSettlement(settlement);
                    message.setResource(resource);
                    player.addMessage(message);

                }
            }
        }
    }

    private void manageSettlementAutomanagedResources(Settlement settlement) {
        Iterator<Resource> iterator = settlement.getAutomanagedResourcesIterator();
        while (iterator.hasNext()) {
            Resource resource = iterator.next();
            getExecutor().execute(new SettlementAutomanageResourceCommand(getRealm(), settlement, resource));
        }
    }
}
