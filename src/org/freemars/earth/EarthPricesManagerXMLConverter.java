package org.freemars.earth;

import java.util.Iterator;
import org.freerealm.Realm;
import org.freerealm.resource.Resource;
import org.freerealm.xmlwrapper.XMLConverterUtility;
import org.freerealm.xmlwrapper.XMLConverter;
import org.w3c.dom.Node;

/**
 *
 * @author Deniz ARIKAN
 */
public class EarthPricesManagerXMLConverter implements XMLConverter<EarthPricesManager> {

    public String toXML(EarthPricesManager earthPricesManager) {
        StringBuffer xml = new StringBuffer();
        xml.append("<EarthPricesManager>\n");
        xml.append("<resourceQuantities>\n");
        for (Resource resource : earthPricesManager.getResourceQuantities().keySet()) {
            if (earthPricesManager.getResourceQuantities().get(resource) > 0) {
                xml.append("<resourceQuantity>\n");
                xml.append("<resourceId>");
                xml.append(resource.getId());
                xml.append("</resourceId>\n");
                xml.append("<quantity>");
                xml.append(earthPricesManager.getResourceQuantities().get(resource));
                xml.append("</quantity>\n");
                xml.append("</resourceQuantity>\n");
            }
        }
        xml.append("</resourceQuantities>\n");
        xml.append("</EarthPricesManager>\n");
        return xml.toString();
    }

    public EarthPricesManager initializeFromNode(Realm realm, Node node) {
        EarthPricesManager earthPricesManager = new EarthPricesManager(realm.getResourceManager());
        Iterator<Resource> resourcesIterator = realm.getResourceManager().getResourcesIterator();
        while (resourcesIterator.hasNext()) {
            Resource resource = resourcesIterator.next();
            earthPricesManager.getResourceQuantities().put(resource, 0);
        }
        Node resourceQuantitiesNode = XMLConverterUtility.findNode(node, "resourceQuantities");
        for (Node subNode = resourceQuantitiesNode.getFirstChild(); subNode != null; subNode = subNode.getNextSibling()) {
            if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                if (subNode.getNodeName().equals("resourceQuantity")) {
                    Node resourceIdNode = XMLConverterUtility.findNode(subNode, "resourceId");
                    Node quantityNode = XMLConverterUtility.findNode(subNode, "quantity");
                    int resourceIdValue = Integer.parseInt(resourceIdNode.getFirstChild().getNodeValue());
                    int quantityValue = Integer.parseInt(quantityNode.getFirstChild().getNodeValue());
                    Resource resource = realm.getResourceManager().getResource(resourceIdValue);
                    earthPricesManager.getResourceQuantities().put(resource, quantityValue);
                }
            }
        }
        return earthPricesManager;
    }
}
