package org.freerealm.settlement;

import org.freerealm.Buildable;
import java.util.Iterator;

/**
 *
 * @author Deniz ARIKAN
 */
public interface SettlementBuildable extends Buildable {

    public Iterator<SettlementBuildablePrerequisite> getPrerequisitesIterator();

    public void addPrerequisite(SettlementBuildablePrerequisite prerequisite);

    public int getPopulationCost();

    public int getBuildCostResourceCount();

    public Iterator<Integer> getBuildCostResourceIdsIterator();

    public int getBuildCostResourceQuantity(int resourceId);
}
