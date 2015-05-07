package org.freerealm.settlement.improvement;

import org.freerealm.settlement.SettlementModifier;
import org.freerealm.settlement.SettlementBuildable;
import org.freerealm.resource.ResourceProducer;

/**
 *
 * @author Deniz ARIKAN
 */
public interface SettlementImprovementType extends SettlementModifier, ResourceProducer, SettlementBuildable {

    public int getId();

    public boolean isResourceProducer();

    public int getMaximumWorkers();

    public void setMaximumWorkers(int maximumWorkers);
}
