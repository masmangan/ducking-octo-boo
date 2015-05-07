package org.freerealm.unit;

import org.freerealm.property.Property;
import java.util.Iterator;
import java.util.Vector;
import org.freerealm.property.BuildableProperty;
import org.freerealm.property.MoveProperty;
import org.freerealm.settlement.SettlementBuildablePrerequisite;
import org.freerealm.settlement.SettlementBuildable;

/**
 *
 * @author Deniz ARIKAN
 */
public class UnitType implements Comparable<UnitType>, SettlementBuildable {

    private int id;
    private String name;
    private int explorationRadius;
    private int weightForContainer;
    private Vector<Property> properties;
    private Vector<SettlementBuildablePrerequisite> prerequisites;

    public UnitType() {
        super();
        properties = new Vector<Property>();
        prerequisites = new Vector<SettlementBuildablePrerequisite>();
    }

    @Override
    public String toString() {
        return getName();
    }

    public int compareTo(UnitType unitType) {
        if (getId() < unitType.getId()) {
            return -1;
        } else if (getId() > unitType.getId()) {
            return 1;
        } else {
            return 0;
        }
    }

    public Iterator<SettlementBuildablePrerequisite> getPrerequisitesIterator() {
        return prerequisites.iterator();
    }

    public void addPrerequisite(SettlementBuildablePrerequisite prerequisite) {
        prerequisites.add(prerequisite);
    }

    public int getPrerequisiteCount() {
        return prerequisites.size();
    }

    // <editor-fold defaultstate="collapsed" desc="Getters & setters">
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Iterator<Property> getPropertiesIterator() {
        return properties.iterator();
    }

    public void addProperty(Property property) {
        properties.add(property);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMovementPoints() {
        MoveProperty move = (MoveProperty) getProperty(MoveProperty.NAME);
        if (move != null) {
            return move.getPoints();
        } else {
            return 0;
        }
    }

    public int getProductionCost() {
        BuildableProperty buildable = (BuildableProperty) getProperty(BuildableProperty.NAME);
        if (buildable != null) {
            return buildable.getBuildCost();
        } else {
            return 0;
        }
    }

    public int getUpkeepCost() {
        BuildableProperty buildable = (BuildableProperty) getProperty(BuildableProperty.NAME);
        if (buildable != null) {
            return buildable.getUpkeepCost();
        } else {
            return 0;
        }
    }

    public int getPopulationCost() {
        BuildableProperty buildable = (BuildableProperty) getProperty(BuildableProperty.NAME);
        if (buildable != null) {
            return buildable.getPopulationCost();
        } else {
            return 0;
        }
    }

    public int getBuildCostResourceCount() {
        BuildableProperty buildable = (BuildableProperty) getProperty(BuildableProperty.NAME);
        if (buildable != null) {
            return buildable.getBuildCostResourceCount();
        } else {
            return 0;
        }
    }

    public Iterator<Integer> getBuildCostResourceIdsIterator() {
        BuildableProperty buildable = (BuildableProperty) getProperty(BuildableProperty.NAME);
        if (buildable != null) {
            return buildable.getBuildCostResourceIdsIterator();
        } else {
            return null;
        }
    }

    public int getBuildCostResourceQuantity(int resourceId) {
        BuildableProperty buildable = (BuildableProperty) getProperty(BuildableProperty.NAME);
        if (buildable != null) {
            return buildable.getBuildCostResourceQuantity(resourceId);
        } else {
            return 0;
        }
    }

    public int getExplorationRadius() {
        return explorationRadius;
    }

    public void setExplorationRadius(int explorationRadius) {
        this.explorationRadius = explorationRadius;
    }

    public int getWeightForContainer() {
        return weightForContainer;
    }

    public void setWeightForContainer(int weightForContainer) {
        this.weightForContainer = weightForContainer;
    }
    // </editor-fold>
}
