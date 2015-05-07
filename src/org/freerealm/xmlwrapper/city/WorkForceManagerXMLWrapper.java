package org.freerealm.xmlwrapper.city;

import org.freerealm.settlement.WorkForceManager;
import org.freerealm.settlement.workforce.WorkForce;
import java.util.Iterator;
import org.freerealm.Realm;
import org.w3c.dom.Node;
import org.freerealm.xmlwrapper.XMLWrapper;

/**
 *
 * @author Deniz ARIKAN
 */
public class WorkForceManagerXMLWrapper implements XMLWrapper {

    private WorkForceManager workForceManager;

    public WorkForceManagerXMLWrapper(WorkForceManager workForceManager) {
        this.workForceManager = workForceManager;
    }

    public String toXML() {
        StringBuffer xml = new StringBuffer();
        xml.append("<allWorkForce>\n");
        Iterator<WorkForce> workForceIterator = workForceManager.getWorkForceIterator();
        while (workForceIterator.hasNext()) {
            xml.append((new WorkForceXMLWrapper(workForceIterator.next())).toXML() + "\n");
        }
        xml.append("</allWorkForce>");
        return xml.toString();
    }

    public void initializeFromNode(Realm realm, Node node) {
        workForceManager.clearAssignedWorkForce();
        for (Node subNode = node.getFirstChild(); subNode != null; subNode = subNode.getNextSibling()) {
            if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                WorkForce workForce = new WorkForce();
                (new WorkForceXMLWrapper(workForce)).initializeFromNode(realm, subNode);
                workForceManager.addWorkForce(workForce.getCoordinate(), workForce);
            }
        }
    }
}
