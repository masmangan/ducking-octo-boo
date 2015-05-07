package org.freerealm.settlement.improvement;

import java.util.Iterator;
import java.util.Vector;
import org.freerealm.settlement.SettlementBuildablePrerequisite;
import org.freerealm.settlement.FreeRealmSettlementModifier;
import org.freerealm.property.ProduceResource;
import org.freerealm.property.Property;
import org.freerealm.resource.Resource;

/**
 *
 * @author Deniz ARIKAN
 */
public class SettlementImprovementTypeImpl extends FreeRealmSettlementModifier implements SettlementImprovementType {

    private int productionCost;
    private int upkeepCost;
    private int maximumWorkers;
    private Vector<SettlementBuildablePrerequisite> prerequisites;

    public SettlementImprovementTypeImpl() {
        prerequisites = new Vector<SettlementBuildablePrerequisite>();
    }

    @Override
    public String toString() {
        return getName();
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

    public int getProductionCost() {
        return productionCost;
    }

    public void setProductionCost(int productionCost) {
        this.productionCost = productionCost;
    }

    public int getUpkeepCost() {
        return upkeepCost;
    }

    public void setUpkeepCost(int upkeepCost) {
        this.upkeepCost = upkeepCost;
    }

    public int getInputResourceCount() {
        Iterator propertyIterator = getPropertiesIterator();
        while (propertyIterator.hasNext()) {
            Property property = (Property) propertyIterator.next();
            if (property instanceof ProduceResource) {
                ProduceResource produceResource = (ProduceResource) property;
                return produceResource.getInputCount();
            }
        }
        return 0;
    }

    public int getInputQuantity(Resource resource) {
        int inputQuantity = 0;
        Iterator propertyIterator = getPropertiesIterator();
        while (propertyIterator.hasNext()) {
            Property property = (Property) propertyIterator.next();
            if (property instanceof ProduceResource) {
                ProduceResource produceResource = (ProduceResource) property;
                inputQuantity = inputQuantity + produceResource.getInputQuantity(resource);
            }
        }
        return inputQuantity;
    }

    public int getOutputResourceCount() {
        Iterator propertyIterator = getPropertiesIterator();
        while (propertyIterator.hasNext()) {
            Property property = (Property) propertyIterator.next();
            if (property instanceof ProduceResource) {
                ProduceResource produceResource = (ProduceResource) property;
                return produceResource.getOutputCount();
            }
        }
        return 0;
    }

    public int getOutputQuantity(Resource resource) {
        int outputQuantity = 0;
        Iterator propertyIterator = getPropertiesIterator();
        while (propertyIterator.hasNext()) {
            Property property = (Property) propertyIterator.next();
            if (property instanceof ProduceResource) {
                ProduceResource produceResource = (ProduceResource) property;
                outputQuantity = outputQuantity + produceResource.getOutputQuantity(resource);
            }
        }
        return outputQuantity;
    }

    public int getMaximumMultiplier() {
        Iterator propertyIterator = getPropertiesIterator();
        while (propertyIterator.hasNext()) {
            Property property = (Property) propertyIterator.next();
            if (property instanceof ProduceResource) {
                ProduceResource produceResource = (ProduceResource) property;
                return produceResource.getMaximumMultiplier();
            }
        }
        return 0;
    }

    public Iterator<Resource> getInputResourcesIterator() {
        Iterator<Resource> inputResourcesIterator = null;
        Iterator propertyIterator = getPropertiesIterator();
        while (propertyIterator.hasNext()) {
            Property property = (Property) propertyIterator.next();
            if (property instanceof ProduceResource) {
                ProduceResource produceResource = (ProduceResource) property;
                inputResourcesIterator = produceResource.getInputResourcesIterator();
            }
        }
        return inputResourcesIterator;
    }

    public Iterator<Resource> getOutputResourcesIterator() {
        Iterator<Resource> outputResourcesIterator = null;
        Iterator propertyIterator = getPropertiesIterator();
        while (propertyIterator.hasNext()) {
            Property property = (Property) propertyIterator.next();
            if (property instanceof ProduceResource) {
                ProduceResource produceResource = (ProduceResource) property;
                outputResourcesIterator = produceResource.getOutputResourcesIterator();
            }
        }
        return outputResourcesIterator;
    }

    public boolean isResourceProducer() {
        Iterator propertyIterator = getPropertiesIterator();
        while (propertyIterator.hasNext()) {
            Property property = (Property) propertyIterator.next();
            if (property instanceof ProduceResource) {
                return true;
            }
        }
        return false;
    }

    public int getMaximumWorkers() {
        return maximumWorkers;
    }

    public void setMaximumWorkers(int maximumWorkers) {
        this.maximumWorkers = maximumWorkers;
    }

    public int getPopulationCost() {
        return 0;
    }

    public int getBuildCostResourceCount() {
        return 0;
    }

    public Iterator<Integer> getBuildCostResourceIdsIterator() {
        return null;
    }

    public int getBuildCostResourceQuantity(int resourceId) {
        return 0;
    }
}
