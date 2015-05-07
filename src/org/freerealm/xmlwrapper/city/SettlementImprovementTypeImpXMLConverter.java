package org.freerealm.xmlwrapper.city;

import java.util.Iterator;
import org.freerealm.Realm;
import org.freerealm.settlement.improvement.SettlementImprovementType;
import org.freerealm.settlement.improvement.SettlementImprovementTypeImpl;
import org.freerealm.settlement.improvement.SettlementImprovementManager;
import org.freerealm.property.Property;
import org.freerealm.xmlwrapper.PropertyFactory;
import org.freerealm.xmlwrapper.TagManager;
import org.freerealm.xmlwrapper.XMLConverter;
import org.freerealm.xmlwrapper.XMLConverterUtility;
import org.w3c.dom.Node;

/**
 *
 * @author Deniz ARIKAN
 */
public class SettlementImprovementTypeImpXMLConverter implements XMLConverter<SettlementImprovementType> {

    public String toXML(SettlementImprovementType settlementImprovementType) {
        StringBuffer xml = new StringBuffer();
        xml.append("<SettlementImprovement");
        xml.append(" id=\"");
        xml.append(settlementImprovementType.getId());
        xml.append("\" name=\"");
        xml.append(settlementImprovementType.getName());
        xml.append("\" productionCost=\"");
        xml.append(settlementImprovementType.getProductionCost());
        xml.append("\" upkeepCost=\"");
        xml.append(settlementImprovementType.getUpkeepCost());
        xml.append("\">\n");
        if (settlementImprovementType.getMaximumWorkers() > 0) {
            xml.append("<workers>");
            xml.append(settlementImprovementType.getMaximumWorkers());
            xml.append("</workers>\n");
        }
        xml.append("<Properties>\n");
        Iterator<Property> propertyIterator = settlementImprovementType.getPropertiesIterator();
        while (propertyIterator.hasNext()) {
            Property property = propertyIterator.next();
            String xMLWrapperName = TagManager.getXMLConverterName(property.getName());
            Class c;
            try {
                c = Class.forName(xMLWrapperName);
                XMLConverter<Property> xMLConverter = (XMLConverter<Property>) c.newInstance();
                xml.append(xMLConverter.toXML(property) + "\n");
            } catch (Exception ex) {
            }
        }
        xml.append("</Properties>\n");
        xml.append(new PrerequisitesXMLHelper().toXML(settlementImprovementType.getPrerequisitesIterator()));
        xml.append("</SettlementImprovement>");
        return xml.toString();
    }

    public SettlementImprovementType initializeFromNode(Realm realm, Node node) {
        SettlementImprovementTypeImpl cityImprovement = new SettlementImprovementTypeImpl();
        String nameValue = node.getAttributes().getNamedItem("name").getNodeValue();
        int productionCostValue = Integer.parseInt(node.getAttributes().getNamedItem("productionCost").getNodeValue());
        int upkeepCostValue = Integer.parseInt(node.getAttributes().getNamedItem("upkeepCost").getNodeValue());
        int idValue = Integer.parseInt(node.getAttributes().getNamedItem("id").getNodeValue());
        cityImprovement.setName(nameValue);
        cityImprovement.setProductionCost(productionCostValue);
        cityImprovement.setUpkeepCost(upkeepCostValue);
        cityImprovement.setId(idValue);

        Node workersNode = XMLConverterUtility.findNode(node, "workers");
        if (workersNode != null) {
            int workers = Integer.parseInt(workersNode.getFirstChild().getNodeValue());
            cityImprovement.setMaximumWorkers(workers);
        }

        for (Node subNode = node.getFirstChild(); subNode != null; subNode = subNode.getNextSibling()) {
            if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                if (subNode.getNodeName().equals("Properties")) {
                    for (Node propertyNode = subNode.getFirstChild(); propertyNode != null; propertyNode = propertyNode.getNextSibling()) {
                        if (propertyNode.getNodeType() == Node.ELEMENT_NODE) {
                            Property property = PropertyFactory.createProperty(realm, propertyNode);
                            cityImprovement.addProperty(property);
                        }
                    }
                }
            }
        }
        return cityImprovement;
    }

    public void setPrerequisitesFromNode(SettlementImprovementManager cityImprovementManager, Node node) {
        int idValue = Integer.parseInt(node.getAttributes().getNamedItem("id").getNodeValue());
        SettlementImprovementType cityImprovement = cityImprovementManager.getImprovement(idValue);
        new PrerequisitesXMLHelper().initializePrerequisites(cityImprovementManager, cityImprovement, XMLConverterUtility.findNode(node, "Prerequisites"));
    }
}
