package org.freerealm;

import org.freerealm.modifier.GeneralModifier;

/**
 *
 * @author Deniz ARIKAN
 */
public class UnitUpkeepCostCalculator {

    private int upkeepCost;
    private GeneralModifier[] generalModifiers;

    public UnitUpkeepCostCalculator(int upkeepCost, GeneralModifier[] generalModifiers) {
        this.upkeepCost = upkeepCost;
        this.generalModifiers = generalModifiers;
    }

    public int getUpkeepCost() {
        int modifier = 0;
        for (GeneralModifier generalModifier : generalModifiers) {
            modifier = modifier + generalModifier.getUnitUpkeepCostModifier();
        }
        upkeepCost = (int) Utility.modifyByPercent(upkeepCost, modifier);
        return upkeepCost;
    }
}
