package org.freerealm.settlement.improvement;

import java.util.Iterator;
import org.freerealm.settlement.SettlementBuildablePrerequisite;
import java.util.Vector;
import org.freerealm.settlement.Settlement;

/**
 *
 * @author Deniz ARIKAN
 */
public class NoSettlementImprovementPrerequisite implements SettlementBuildablePrerequisite {

    private Settlement settlement;
    private Vector<SettlementImprovementType> excludingImprovements;

    public NoSettlementImprovementPrerequisite(Vector<SettlementImprovementType> excludingImprovements) {
        this.excludingImprovements = excludingImprovements;
    }

    public boolean isSatisfied() {
        for (SettlementImprovementType improvementType : excludingImprovements) {
            if (settlement.hasImprovementType(improvementType)) {
                return false;
            }
        }
        return true;
    }

    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
    }

    public Iterator<SettlementImprovementType> getExcludingImprovementsIterator() {
        return excludingImprovements.iterator();
    }
}
