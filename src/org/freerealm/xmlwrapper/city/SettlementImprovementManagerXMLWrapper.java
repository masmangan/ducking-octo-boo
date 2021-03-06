package org.freerealm.xmlwrapper.city;

import java.util.Iterator;
import org.w3c.dom.Node;
import org.freerealm.Realm;
import org.freerealm.settlement.improvement.SettlementImprovementType;
import org.freerealm.settlement.improvement.SettlementImprovementManager;

/**
 *
 * @author Deniz ARIKAN
 */
public class SettlementImprovementManagerXMLWrapper {

    private SettlementImprovementManager settlementImprovementManager;

    public SettlementImprovementManagerXMLWrapper(SettlementImprovementManager cityImprovementManager) {
        this.settlementImprovementManager = cityImprovementManager;
    }

    public String toXML() {
        StringBuffer xml = new StringBuffer();
        xml.append("<SettlementImprovements>\n");
        for (Iterator<SettlementImprovementType> iterator = settlementImprovementManager.getImprovementsIterator(); iterator.hasNext();) {
            SettlementImprovementType cityImprovement = iterator.next();
            xml.append(new SettlementImprovementTypeImpXMLConverter().toXML(cityImprovement) + "\n");
        }
        xml.append("</SettlementImprovements>");
        return xml.toString();
    }

    public void initializeFromNode(Realm realm, Node node) {
        for (Node settlementImprovementsNode = node.getFirstChild(); settlementImprovementsNode != null; settlementImprovementsNode = settlementImprovementsNode.getNextSibling()) {
            if (settlementImprovementsNode.getNodeType() == Node.ELEMENT_NODE) {
                SettlementImprovementType cityImprovement = new SettlementImprovementTypeImpXMLConverter().initializeFromNode(realm, settlementImprovementsNode);
                settlementImprovementManager.addImprovement(cityImprovement);
            }
        }
        for (Node settlementImprovementsNode = node.getFirstChild(); settlementImprovementsNode != null; settlementImprovementsNode = settlementImprovementsNode.getNextSibling()) {
            if (settlementImprovementsNode.getNodeType() == Node.ELEMENT_NODE) {
                new SettlementImprovementTypeImpXMLConverter().setPrerequisitesFromNode(settlementImprovementManager, settlementImprovementsNode);
            }
        }
    }
}
