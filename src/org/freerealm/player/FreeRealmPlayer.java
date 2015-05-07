package org.freerealm.player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.freerealm.map.Coordinate;
import org.freerealm.Realm;
import org.freerealm.nation.Nation;
import org.freerealm.property.Property;
import org.freerealm.resource.Resource;
import org.freerealm.unit.Unit;
import org.freerealm.player.mission.Mission;
import org.freerealm.property.ModifyProduction;
import org.freerealm.property.ModifyRequiredPopulationResourceAmount;
import org.freerealm.property.ModifySettlementImprovementCost;
import org.freerealm.property.ModifySettlementImprovementUpkeepCost;
import org.freerealm.property.ModifyUnitCost;
import org.freerealm.property.ModifyUnitUpkeepCost;
import org.freerealm.settlement.Settlement;
import org.freerealm.settlement.improvement.SettlementImprovementType;
import org.freerealm.unit.DefaultUnitGroupDefinition;
import org.freerealm.unit.UnitGroupDefinition;
import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public class FreeRealmPlayer implements Player, Comparable<FreeRealmPlayer> {

    private final Realm realm;
    private int id;
    private int status;
    private String name;
    private Color primaryColor;
    private Color secondaryColor;
    private boolean turnEnded;
    private int wealth;
    private int taxRate;
    private Nation nation;
    private SettlementManager settlementManager;
    private UnitManager unitManager;
    private ArrayList<Coordinate> exploredCoordinates;
    private ArrayList<Mission> missions;
    private MessageManager messageManager;
    private HashMap<Integer, Integer> builtImprovementsCount;
    private ArrayList<Property> properties;

    public FreeRealmPlayer(Realm realm) {
        this.realm = realm;
        setTaxRate(0);
        settlementManager = new SettlementManager(this);
        unitManager = new UnitManager(this);
        if (realm.getProperty("starting_wealth") != null) {
            setWealth(Integer.parseInt(realm.getProperty("starting_wealth")));
        }
        exploredCoordinates = new ArrayList<Coordinate>();
        missions = new ArrayList<Mission>();
        messageManager = new MessageManager();
        builtImprovementsCount = new HashMap<Integer, Integer>();
        properties = new ArrayList<Property>();
    }

    @Override
    public String toString() {
        return getName();
    }

    public int compareTo(FreeRealmPlayer player) {
        if (getId() < player.getId()) {
            return -1;
        } else if (getId() > player.getId()) {
            return 1;
        } else {
            return 0;
        }
    }

    public Realm getRealm() {
        return realm;
    }

    public UnitGroupDefinition getUnitGroupDefinition() {
        DefaultUnitGroupDefinition unitGroupDefinition = new DefaultUnitGroupDefinition();
        Iterator<Unit> iterator = getUnitsIterator();
        while (iterator.hasNext()) {
            Unit unit = iterator.next();
            unitGroupDefinition.setQuantityForUnitType(unit.getType(), unitGroupDefinition.getQuantityForUnitType(unit.getType()) + 1);
        }
        return unitGroupDefinition;
    }

    public int getTotalIncome() {
        int totalIncome = 0;
        Iterator<Settlement> settlementIterator = getSettlementManager().getSettlementsIterator();
        while (settlementIterator.hasNext()) {
            totalIncome = totalIncome + settlementIterator.next().getWealthCollectedByTax();
        }
        /*
         Iterator<Unit> unitIterator = getUnitsIterator();
         while (unitIterator.hasNext()) {
         unitIterator.next();
         }
         */
        return totalIncome;
    }

    public int getTotalIncomeIf(int tax) {
        int totalIncome = 0;
        Iterator<Settlement> settlementIterator = getSettlementManager().getSettlementsIterator();
        while (settlementIterator.hasNext()) {
            totalIncome = totalIncome + settlementIterator.next().getWealthCollectedByTaxIf(tax);
        }
        /*
         Iterator<Unit> unitIterator = getUnitsIterator();
         while (unitIterator.hasNext()) {
         unitIterator.next();
         }
         */
        return totalIncome;
    }

    public int getTotalExpenses() {
        int totalExpenses = 0;
        totalExpenses = totalExpenses + getSettlementManager().getSettlementUpkeep();
        totalExpenses = totalExpenses + getUnitManager().getUnitUpkeep();
        return totalExpenses;
    }

    public boolean isMissionAssigned(int missionId) {
        Iterator<Mission> iterator = getMissionsIterator();
        while (iterator.hasNext()) {
            Mission mission = iterator.next();
            if (mission.getId() == missionId) {
                return true;
            }
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPopulation() {
        int population = 0;
        Iterator<Settlement> settlementIterator = getSettlementManager().getSettlementsIterator();
        while (settlementIterator.hasNext()) {
            population = population + settlementIterator.next().getPopulation();
        }
        return population;
    }

    public boolean isTurnEnded() {
        return turnEnded;
    }

    public void setTurnEnded(boolean turnEnded) {
        this.turnEnded = turnEnded;
    }

    public Color getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(Color primaryColor) {
        this.primaryColor = primaryColor;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(Color secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public int getWealth() {
        return wealth;
    }

    public void setWealth(int wealth) {
        this.wealth = wealth;
    }

    public int getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Nation getNation() {
        return nation;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
    }

    public String getNextSettlementName() {
        String settlementName = "";
        Iterator<String> iterator = getNation().getSettlementNamesIterator();
        while (iterator.hasNext()) {
            boolean settlementNameExists = false;
            String trysettlementName = iterator.next();
            Iterator<Settlement> settlementsIterator = getSettlementManager().getSettlementsIterator();
            while (settlementsIterator.hasNext()) {
                Settlement settlement = settlementsIterator.next();
                if (settlement.getName().equals(trysettlementName)) {
                    settlementNameExists = true;
                }
            }
            if (!settlementNameExists) {
                return trysettlementName;
            }
        }
        return settlementName;
    }

    public UnitManager getUnitManager() {
        return unitManager;
    }

    public int getUnitCount() {
        return unitManager.getUnitCount();
    }

    public Iterator<Unit> getUnitsIterator() {
        return getUnitManager().getUnitsIterator();
    }

    public Unit getUnit(int unitId) {
        return getUnitManager().getUnit(unitId);
    }

    public Unit getActiveUnit() {
        return getUnitManager().getActiveUnit();
    }

    public void setActiveUnit(Unit unit) {
        getUnitManager().setActiveUnit(unit);
    }

    public Unit getNextUnit(Unit unit) {
        return getUnitManager().getNextUnit(unit);
    }

    public boolean hasUnit(Unit unit) {
        return getUnitManager().getUnits().containsValue(unit);
    }

    public void addUnit(Unit unit) {
        getUnitManager().addUnit(unit);
    }

    public void removeUnit(Unit unit) {
        getUnitManager().removeUnit(unit);
    }

    public List<Unit> getUnitsOfType(UnitType unitType) {
        return getUnitManager().getUnitsOfType(unitType);
    }

    public SettlementManager getSettlementManager() {
        return settlementManager;
    }

    public void setSettlementManager(SettlementManager settlementManager) {
        this.settlementManager = settlementManager;
    }

    public Iterator<Settlement> getSettlementsIterator() {
        return getSettlementManager().getSettlementsIterator();
    }

    public int getSettlementCount() {
        return settlementManager.getSettlementCount();
    }

    public void addSettlement(Settlement settlement) {
        getSettlementManager().addSettlement(settlement);
    }

    public void removeSettlement(Settlement settlement) {
        getSettlementManager().removeSettlement(settlement);
    }

    public boolean addExploredCoordinate(Coordinate coordinate) {
        if (coordinate != null && (!exploredCoordinates.contains(coordinate))) {
            exploredCoordinates.add(coordinate);
            return true;
        } else {
            return false;
        }
    }

    public List<Coordinate> addExploredCoordinates(List<Coordinate> coordinates) {
        List<Coordinate> addedCoordinates = new ArrayList<Coordinate>();
        Iterator<Coordinate> iterator = coordinates.iterator();
        while (iterator.hasNext()) {
            Coordinate coordinate = iterator.next();
            boolean added = addExploredCoordinate(coordinate);
            if (added) {
                addedCoordinates.add(coordinate);
            }
        }
        return addedCoordinates;
    }

    public boolean isCoordinateExplored(Coordinate coordinate) {
        return exploredCoordinates.contains(coordinate);
    }

    public Iterator<Coordinate> getExploredCoordinatesIterator() {
        return exploredCoordinates.iterator();
    }

    public List<Coordinate> getExploredCoordinates() {
        return exploredCoordinates;
    }

    public int getExploredCoordinateCount() {
        return exploredCoordinates.size();
    }

    public void clearExploredCoordinates() {
        exploredCoordinates.clear();
    }

    public void clearMessages() {
        messageManager.clear();
    }

    public void addMessage(Message message) {
        messageManager.addMessage(message);
    }

    public int getUnreadMessageCount() {
        return messageManager.getUnreadMessageCount();
    }

    public Iterator<Message> getUnreadMessagesIterator() {
        return messageManager.getUnreadMessagesIterator();
    }

    public Iterator<Message> getMessagesIterator() {
        return messageManager.getMessagesIterator();
    }

    public void addMission(Mission mission) {
        missions.add(mission);
    }

    public void removeMission(Mission mission) {
        missions.remove(mission);
    }

    public void clearMissions() {
        missions.clear();
    }

    public Iterator<Mission> getMissionsIterator() {
        return missions.iterator();
    }

    public int getMissionCount() {
        return missions.size();
    }

    public int getCompletedMissionCount() {
        int completedMissionCount = 0;
        Iterator<Mission> missionsIterator = getMissionsIterator();
        while (missionsIterator.hasNext()) {
            Mission mission = missionsIterator.next();
            if (mission.getStatus() == Mission.STATUS_COMPLETED) {
                completedMissionCount++;
            }
        }
        return completedMissionCount;
    }

    public int getCompletedMissionPercent() {
        if (getMissionCount() > 0) {
            return getCompletedMissionCount() * 100 / getMissionCount();
        } else {
            return 0;
        }
    }

    public int getFailedMissionCount() {
        int failedMissionCount = 0;
        Iterator<Mission> missionsIterator = getMissionsIterator();
        while (missionsIterator.hasNext()) {
            Mission mission = missionsIterator.next();
            if (mission.getStatus() == Mission.STATUS_FAILED) {
                failedMissionCount++;
            }
        }
        return failedMissionCount;
    }

    public int getFailedMissionPercent() {
        if (getMissionCount() > 0) {
            return getFailedMissionCount() * 100 / getMissionCount();
        } else {
            return 0;
        }
    }

    public void clearBuiltTileImprovementCount() {
        builtImprovementsCount.clear();
    }

    public Iterator<Integer> getBuiltTileImprovementCountIterator() {
        return builtImprovementsCount.keySet().iterator();
    }

    public int getBuiltTileImprovementCount(int typeId) {
        if (builtImprovementsCount.get(typeId) != null) {
            return builtImprovementsCount.get(typeId);
        } else {
            return 0;
        }
    }

    public void setBuiltTileImprovementCount(int typeId, int count) {
        builtImprovementsCount.put(typeId, count);
    }

    public int getBuiltSettlementImprovementCount(int typeId) {
        int builtSettlementImprovementCount = 0;
        Iterator<Settlement> settlementsIterator = getSettlementsIterator();
        while (settlementsIterator.hasNext()) {
            Settlement settlement = settlementsIterator.next();
            if (settlement.hasImprovementType(realm.getSettlementImprovementManager().getImprovement(typeId))) {
                builtSettlementImprovementCount = builtSettlementImprovementCount + 1;
            }
        }
        return builtSettlementImprovementCount;
    }

    public Iterator<Property> getPropertiesIterator() {
        return properties.iterator();
    }

    public Property getProperty(String propertyName) {
        Property returnValue = null;
        Iterator<Property> propertyIterator = properties.iterator();
        while (propertyIterator.hasNext()) {
            Property property = propertyIterator.next();
            if (property.getName().equals(propertyName)) {
                returnValue = property;
            }
        }
        return returnValue;
    }

    public boolean hasPropertyNamed(String name) {
        return getProperty(name) != null;
    }

    public void addProperty(Property property) {
        properties.add(property);
    }

    public float getMovementCostModifier() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getResourceModifier(Resource resource, boolean resourceExists) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getDefenceModifier() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getProductionModifier() {
        int productionModifier = 0;
        Iterator<Property> propertyEditor = getPropertiesIterator();
        while (propertyEditor.hasNext()) {
            Property property = propertyEditor.next();
            if (property instanceof ModifyProduction) {
                ModifyProduction modifyProduction = (ModifyProduction) property;
                productionModifier = productionModifier + modifyProduction.getModifier();
            }
        }
        return productionModifier;
    }

    public int getTaxModifier() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getMaximumTileWorkersModifier() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getEfficiencyModifier() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getCapacityModifier(Resource resource) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getSettlementImprovementBuildCostModifier() {
        int settlementImprovementCostModifier = 0;
        Iterator<Property> propertyEditor = getPropertiesIterator();
        while (propertyEditor.hasNext()) {
            Property property = propertyEditor.next();
            if (property instanceof ModifySettlementImprovementCost) {
                ModifySettlementImprovementCost modifySettlementImprovementCost = (ModifySettlementImprovementCost) property;
                settlementImprovementCostModifier = settlementImprovementCostModifier + modifySettlementImprovementCost.getModifier();
            }
        }
        return settlementImprovementCostModifier;
    }

    public int getSettlementImprovementUpkeepCostModifier() {
        int settlementImprovementUpkeepCostModifier = 0;
        Iterator<Property> propertyEditor = getPropertiesIterator();
        while (propertyEditor.hasNext()) {
            Property property = propertyEditor.next();
            if (property instanceof ModifySettlementImprovementUpkeepCost) {
                ModifySettlementImprovementUpkeepCost modifySettlementImprovementUpkeepCost = (ModifySettlementImprovementUpkeepCost) property;
                settlementImprovementUpkeepCostModifier = settlementImprovementUpkeepCostModifier + modifySettlementImprovementUpkeepCost.getModifier();
            }
        }
        return settlementImprovementUpkeepCostModifier;
    }

    public int getUnitUpkeepCostModifier() {
        int modifier = 0;
        Iterator<Property> propertyEditor = getPropertiesIterator();
        while (propertyEditor.hasNext()) {
            Property property = propertyEditor.next();
            if (property instanceof ModifyUnitUpkeepCost) {
                ModifyUnitUpkeepCost modifyUnitUpkeepCost = (ModifyUnitUpkeepCost) property;
                modifier = modifier + modifyUnitUpkeepCost.getModifier();
            }
        }
        return modifier;
    }

    public int getUnitBuildCostModifier() {
        int unitCostModifier = 0;
        Iterator<Property> propertyEditor = getPropertiesIterator();
        while (propertyEditor.hasNext()) {
            Property property = propertyEditor.next();
            if (property instanceof ModifyUnitCost) {
                ModifyUnitCost modifyUnitCost = (ModifyUnitCost) property;
                unitCostModifier = unitCostModifier + modifyUnitCost.getModifier();
            }
        }
        return unitCostModifier;
    }

    public int getRequiredPopulationResourceAmountModifier(int resourceId) {
        int requiredPopulationResourceAmountModifier = 0;
        Iterator<Property> propertyEditor = getPropertiesIterator();
        while (propertyEditor.hasNext()) {
            Property property = propertyEditor.next();
            if (property instanceof ModifyRequiredPopulationResourceAmount) {
                ModifyRequiredPopulationResourceAmount modifyRequiredPopulationResourceAmount = (ModifyRequiredPopulationResourceAmount) property;
                if (modifyRequiredPopulationResourceAmount.getResourceId() == resourceId) {
                    requiredPopulationResourceAmountModifier = requiredPopulationResourceAmountModifier + modifyRequiredPopulationResourceAmount.getModifier();
                }
            }
        }
        return requiredPopulationResourceAmountModifier;
    }

    public int getSettlementCountHavingImprovementType(int settlementImprovementTypeId) {
        int settlementCountHavingImprovementType = 0;
        Iterator<Settlement> iterator = getSettlementsIterator();
        while (iterator.hasNext()) {
            Settlement settlement = iterator.next();
            if (settlement.hasImprovementType(settlementImprovementTypeId)) {
                settlementCountHavingImprovementType++;
            }
        }
        return settlementCountHavingImprovementType;
    }
}
