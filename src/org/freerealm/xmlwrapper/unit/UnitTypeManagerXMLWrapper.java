package org.freerealm.xmlwrapper.unit;

import java.util.Iterator;
import org.freerealm.Realm;
import org.w3c.dom.Node;
import org.freerealm.unit.UnitType;
import org.freerealm.unit.UnitTypeManager;
import org.freerealm.xmlwrapper.XMLWrapper;

/**
 *
 * @author Deniz ARIKAN
 */
public class UnitTypeManagerXMLWrapper implements XMLWrapper {

    private UnitTypeManager unitTypeManager;

    public UnitTypeManagerXMLWrapper(UnitTypeManager unitTypeManager) {
        this.unitTypeManager = unitTypeManager;
    }

    public String toXML() {
        StringBuffer xml = new StringBuffer();
        xml.append("<UnitTypes>\n");
        for (Iterator<UnitType> iterator = unitTypeManager.getUnitTypesIterator(); iterator.hasNext();) {
            UnitType unitType = iterator.next();
            xml.append((new UnitTypeXMLConverter()).toXML(unitType) + "\n");
        }
        xml.append("</UnitTypes>");
        return xml.toString();
    }

    public void initializeFromNode(Realm realm, Node node) {
        for (Node unitTypeNode = node.getFirstChild(); unitTypeNode != null; unitTypeNode = unitTypeNode.getNextSibling()) {
            if (unitTypeNode.getNodeType() == Node.ELEMENT_NODE) {
                UnitType unitType = new UnitTypeXMLConverter().initializeFromNode(realm, unitTypeNode);
                unitTypeManager.getUnitTypes().put(unitType.getId(), unitType);
            }
        }
    }
}
