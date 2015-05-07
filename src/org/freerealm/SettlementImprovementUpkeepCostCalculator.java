package org.freerealm;

import org.freerealm.modifier.GeneralModifier;

/**
 *
 * @author Deniz ARIKAN
 */
public class SettlementImprovementUpkeepCostCalculator {

    private int upkeepCost;
    private GeneralModifier[] generalModifiers;

    public SettlementImprovementUpkeepCostCalculator(int upkeepCost, GeneralModifier[] generalModifiers) {
        this.upkeepCost = upkeepCost;
        this.generalModifiers = generalModifiers;
    }

    public int getUpkeepCost() {
        int settlementImprovementUpkeepCostModifier = 0;
        for (GeneralModifier generalModifier : generalModifiers) {
            settlementImprovementUpkeepCostModifier = settlementImprovementUpkeepCostModifier + generalModifier.getSettlementImprovementUpkeepCostModifier();
        }
        upkeepCost = (int) Utility.modifyByPercent(upkeepCost, settlementImprovementUpkeepCostModifier);
        return upkeepCost;
    }
}
