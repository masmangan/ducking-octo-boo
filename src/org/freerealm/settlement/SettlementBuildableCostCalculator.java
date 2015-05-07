package org.freerealm.settlement;

import org.freerealm.Utility;
import org.freerealm.settlement.improvement.SettlementImprovementType;
import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public class SettlementBuildableCostCalculator {

    private SettlementBuildable settlementBuildable;
    private SettlementModifier[] settlementModifiers;

    public SettlementBuildableCostCalculator(SettlementBuildable settlementBuildable, SettlementModifier[] settlementModifiers) {
        this.settlementBuildable = settlementBuildable;
        this.settlementModifiers = settlementModifiers;
    }

    public int getCost() {
        int productionCost = settlementBuildable.getProductionCost();
        int settlementImprovementBuildCostModifier = 0;
        int unitBuildCostModifier = 0;
        for (SettlementModifier settlementModifier : settlementModifiers) {
            settlementImprovementBuildCostModifier = settlementImprovementBuildCostModifier + settlementModifier.getSettlementImprovementBuildCostModifier();
            unitBuildCostModifier = unitBuildCostModifier + settlementModifier.getUnitBuildCostModifier();
        }
        if (settlementBuildable instanceof SettlementImprovementType) {
            productionCost = (int) Utility.modifyByPercent(productionCost, settlementImprovementBuildCostModifier);
        } else if (settlementBuildable instanceof UnitType) {
            productionCost = (int) Utility.modifyByPercent(productionCost, unitBuildCostModifier);
        }
        return productionCost;
    }
}
