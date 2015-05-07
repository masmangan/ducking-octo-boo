package org.freemars.earth;

import java.util.HashMap;
import java.util.Iterator;
import org.freerealm.Realm;
import org.freerealm.resource.Resource;
import org.freerealm.resource.ResourceManager;
import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public class EarthPricesManager {

    private HashMap<Resource, Integer> minPrices;
    private HashMap<Resource, Integer> maxPrices;
    private HashMap<Resource, Integer> resourceQuantity;

    public EarthPricesManager(ResourceManager resourceManager) {
        minPrices = new HashMap<Resource, Integer>();
        maxPrices = new HashMap<Resource, Integer>();
        resourceQuantity = new HashMap<Resource, Integer>();
        minPrices.put(resourceManager.getResource("Water"), 1);
        minPrices.put(resourceManager.getResource(Resource.FOOD), 1);
        minPrices.put(resourceManager.getResource("Energy"), 1);
        minPrices.put(resourceManager.getResource("Hydrogen"), 5);
        minPrices.put(resourceManager.getResource("Lumber"), 3);
        minPrices.put(resourceManager.getResource("Fertilizer"), 1);
        minPrices.put(resourceManager.getResource("Iron"), 3);
        minPrices.put(resourceManager.getResource("Steel"), 6);
        minPrices.put(resourceManager.getResource("Silica"), 3);
        minPrices.put(resourceManager.getResource("Glass"), 6);
        minPrices.put(resourceManager.getResource("Minerals"), 3);
        minPrices.put(resourceManager.getResource("Chemicals"), 6);
        minPrices.put(resourceManager.getResource("Magnesium"), 6);
        minPrices.put(resourceManager.getResource("Gauss rifle"), 6);
        minPrices.put(resourceManager.getResource("Aluminium"), 6);
        minPrices.put(resourceManager.getResource("Spaceship parts"), 6);

        maxPrices.put(resourceManager.getResource("Water"), 1);
        maxPrices.put(resourceManager.getResource(Resource.FOOD), 2);
        maxPrices.put(resourceManager.getResource("Energy"), 3);
        maxPrices.put(resourceManager.getResource("Hydrogen"), 10);
        maxPrices.put(resourceManager.getResource("Lumber"), 5);
        maxPrices.put(resourceManager.getResource("Fertilizer"), 2);
        maxPrices.put(resourceManager.getResource("Iron"), 5);
        maxPrices.put(resourceManager.getResource("Steel"), 13);
        maxPrices.put(resourceManager.getResource("Silica"), 6);
        maxPrices.put(resourceManager.getResource("Glass"), 13);
        maxPrices.put(resourceManager.getResource("Minerals"), 6);
        maxPrices.put(resourceManager.getResource("Chemicals"), 13);
        maxPrices.put(resourceManager.getResource("Magnesium"), 14);
        maxPrices.put(resourceManager.getResource("Gauss rifle"), 18);
        maxPrices.put(resourceManager.getResource("Aluminium"), 12);
        maxPrices.put(resourceManager.getResource("Spaceship parts"), 25);
        Iterator<Resource> iterator = resourceManager.getResourcesIterator();
        while (iterator.hasNext()) {
            Resource resource = iterator.next();
            resourceQuantity.put(resource, 0);
        }
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\nResource quantities :\n-----\n");
        for (Resource resource : resourceQuantity.keySet()) {
            stringBuffer.append(resource.getName() + " : " + resourceQuantity.get(resource) + "\n");
        }
        return stringBuffer.toString();
    }

    public HashMap<Resource, Integer> getResourceQuantities() {
        return resourceQuantity;
    }

    public int getEarthSellsAtPrice(Realm realm, UnitType unitType) {
        UnitType shuttleUnitType = realm.getUnitTypeManager().getUnitType("Shuttle");
        if (unitType.equals(shuttleUnitType)) {
            return 50000;
        }
        UnitType freighterUnitType = realm.getUnitTypeManager().getUnitType("Freighter");
        if (unitType.equals(freighterUnitType)) {
            return 100000;
        }
        UnitType bulkFreighterUnitType = realm.getUnitTypeManager().getUnitType("Bulk freighter");
        if (unitType.equals(bulkFreighterUnitType)) {
            return 150000;
        }
        int buyPrice = unitType.getProductionCost() * 4;
        if (unitType.getId() == 0) {
            int financeCostPerColonist = Integer.parseInt(realm.getProperty("finance_cost_per_colonist"));
            buyPrice = buyPrice + financeCostPerColonist * 100;
        }
        return buyPrice;
    }

    /**
     * Earth will sell given resource at this price.
     */
    public int getEarthSellsAtPrice(Resource resource) {
        if (resource.getId() == 5) {
            return 2;
        } else {
            Double earthBuysAtPrice = (new Double(getEarthBuysAtPrice(resource) + 3) * 1.5);
            return earthBuysAtPrice.intValue();
        }
    }

    /**
     * Earth will buy given resource at this price.
     */
    public int getEarthBuysAtPrice(Resource resource) {
        if (resource.getId() == 0) {
            return 0;
        } else if (resource.getId() == 2) {
            return 1;
        } else {
            int divider = 10;
            int consumptionPerTurn = 500;
            int quantity = resourceQuantity.get(resource);
            int enoughTurns = quantity / consumptionPerTurn;
            double step = (double) (maxPrices.get(resource) - minPrices.get(resource)) / divider;
            int multiplier = enoughTurns > divider ? 0 : divider - enoughTurns;
            int price = (int) (minPrices.get(resource) + multiplier * step);
            return price;
        }
    }

    public void addResource(Resource resource, int quantity) {
        int currentQuantity = resourceQuantity.get(resource);
        resourceQuantity.put(resource, currentQuantity + quantity);
    }

    public void removeResource(Resource resource, int quantity) {
        int newQuantity = resourceQuantity.get(resource) - quantity;
        if (newQuantity < 0) {
            newQuantity = 0;
        }
        resourceQuantity.put(resource, newQuantity);
    }

    public void manageResourceConsumption(int numberOfTurns) {
        for (Resource resource : resourceQuantity.keySet()) {
            removeResource(resource, 500 * numberOfTurns);
        }
    }
}
