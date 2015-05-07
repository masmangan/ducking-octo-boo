package org.freerealm.xmlwrapper.city;

import org.freerealm.settlement.workforce.WorkForce;
import org.freerealm.map.Coordinate;
import org.freerealm.resource.Resource;
import org.freerealm.Realm;
import org.w3c.dom.Node;
import org.freerealm.xmlwrapper.XMLWrapper;
import org.freerealm.xmlwrapper.map.CoordinateXMLConverter;

/**
 *
 * @author Deniz ARIKAN
 */
public class WorkForceXMLWrapper implements XMLWrapper {

    private WorkForce workForce;

    public WorkForceXMLWrapper(WorkForce workForce) {
        this.workForce = workForce;
    }

    public String toXML() {
        StringBuffer xml = new StringBuffer();
        xml.append("<WorkForce>\n");
        xml.append("<resource>" + workForce.getResource().getId() + "</resource>\n");
        xml.append("<numberOfWorkers>" + workForce.getNumberOfWorkers() + "</numberOfWorkers>\n");
        xml.append(new CoordinateXMLConverter().toXML(workForce.getCoordinate()) + "\n");
        xml.append("</WorkForce>");
        return xml.toString();
    }

    public void initializeFromNode(Realm realm, Node node) {
        for (Node subNode = node.getFirstChild(); subNode != null; subNode = subNode.getNextSibling()) {
            if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                if (subNode.getNodeName().equals("resource")) {
                    int resourceId = Integer.parseInt(subNode.getFirstChild().getNodeValue());
                    Resource resourceValue = realm.getResourceManager().getResource(resourceId);
                    workForce.setResource(resourceValue);
                } else if (subNode.getNodeName().equals("numberOfWorkers")) {
                    int numberOfWorkersValue = Integer.parseInt(subNode.getFirstChild().getNodeValue());
                    workForce.setNumberOfWorkers(numberOfWorkersValue);
                } else if (subNode.getNodeName().equals("coordinate")) {
                    workForce.setCoordinate(new CoordinateXMLConverter().initializeFromNode(realm, subNode));
                }
            }
        }
    }
}
