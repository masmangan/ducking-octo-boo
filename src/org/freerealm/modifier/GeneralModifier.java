package org.freerealm.modifier;

import org.freerealm.settlement.SettlementModifier;
import org.freerealm.tile.TileModifier;

/**
 *
 * @author Deniz ARIKAN
 */
public interface GeneralModifier extends TileModifier, SettlementModifier {

    public int getRequiredPopulationResourceAmountModifier(int resourceId);

    public int getSettlementImprovementUpkeepCostModifier();

    public int getUnitUpkeepCostModifier();
}
