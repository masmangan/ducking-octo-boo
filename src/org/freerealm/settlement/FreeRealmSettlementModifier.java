package org.freerealm.settlement;

import java.util.Iterator;
import org.freerealm.FreeRealmModifier;
import org.freerealm.property.ModifyEfficiency;
import org.freerealm.property.ModifyMaximumWorkers;
import org.freerealm.property.ModifyProduction;
import org.freerealm.property.ModifySettlementImprovementCost;
import org.freerealm.property.ModifyTaxIncome;
import org.freerealm.property.ModifyUnitCost;
import org.freerealm.property.Property;
import org.freerealm.property.StoreResourceProperty;
import org.freerealm.resource.Resource;

/**
 *
 * @author Deniz ARIKAN
 */
public class FreeRealmSettlementModifier extends FreeRealmModifier implements SettlementModifier {

    public int getProductionModifier() {
        int productionModifier = 0;
        Iterator propertyIterator = getPropertiesIterator();
        while (propertyIterator.hasNext()) {
            Property property = (Property) propertyIterator.next();
            if (property instanceof ModifyProduction) {
                ModifyProduction increaseProduction = (ModifyProduction) property;
                productionModifier = productionModifier + increaseProduction.getModifier();
            }
        }

        return productionModifier;
    }

    public int getTaxModifier() {
        int taxModifier = 0;
        Iterator propertyEditor = getPropertiesIterator();
        while (propertyEditor.hasNext()) {
            Property property = (Property) propertyEditor.next();
            if (property instanceof ModifyTaxIncome) {
                ModifyTaxIncome increaseTaxIncome = (ModifyTaxIncome) property;
                taxModifier = taxModifier + increaseTaxIncome.getModifier();
            }
        }
        return taxModifier;
    }

    public int getMaximumTileWorkersModifier() {
        int increasePercent = 0;
        Iterator propertyEditor = getPropertiesIterator();
        while (propertyEditor.hasNext()) {
            Property property = (Property) propertyEditor.next();
            if (property instanceof ModifyMaximumWorkers) {
                ModifyMaximumWorkers modifyMaximumWorkers = (ModifyMaximumWorkers) property;
                increasePercent = increasePercent + modifyMaximumWorkers.getModifier();
            }
        }
        return increasePercent;
    }

    public int getEfficiencyModifier() {
        int efficiencyModifier = 0;
        Iterator propertyEditor = getPropertiesIterator();
        while (propertyEditor.hasNext()) {
            Property property = (Property) propertyEditor.next();
            if (property instanceof ModifyEfficiency) {
                ModifyEfficiency increaseEfficiency = (ModifyEfficiency) property;
                efficiencyModifier = efficiencyModifier + increaseEfficiency.getModifier();
            }
        }
        return efficiencyModifier;
    }

    public int getCapacityModifier(Resource resource) {
        int capacityModifier = 0;
        Iterator<Property> propertyEditor = getPropertiesIterator();
        while (propertyEditor.hasNext()) {
            Property property = propertyEditor.next();
            if (property instanceof StoreResourceProperty) {
                StoreResourceProperty storeResource = (StoreResourceProperty) property;
                if (resource.equals(storeResource.getResource())) {
                    capacityModifier = capacityModifier + storeResource.getStorage();
                }
            }
        }
        return capacityModifier;
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
}
