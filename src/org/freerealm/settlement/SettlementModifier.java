package org.freerealm.settlement;

import org.freerealm.Modifier;
import org.freerealm.resource.Resource;

/**
 *
 * @author Deniz ARIKAN
 */
public interface SettlementModifier extends Modifier {

    public int getProductionModifier();

    public int getTaxModifier();

    public int getMaximumTileWorkersModifier();

    public int getEfficiencyModifier();

    public int getCapacityModifier(Resource resource);

    public int getSettlementImprovementBuildCostModifier();

    public int getUnitBuildCostModifier();
}
