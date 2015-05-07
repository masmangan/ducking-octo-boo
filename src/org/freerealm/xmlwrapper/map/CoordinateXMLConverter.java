package org.freerealm.xmlwrapper.map;

import org.freerealm.Realm;
import org.freerealm.map.Coordinate;
import org.freerealm.xmlwrapper.XMLConverter;
import org.freerealm.xmlwrapper.XMLConverterUtility;
import org.w3c.dom.Node;

/**
 *
 * @author Deniz ARIKAN
 */
public class CoordinateXMLConverter implements XMLConverter<Coordinate> {

    public String toXML(Coordinate coordinate) {
        StringBuffer xml = new StringBuffer();
        xml.append("<coordinate>");
        xml.append("<x>" + coordinate.getAbscissa() + "</x>");
        xml.append("<y>" + coordinate.getOrdinate() + "</y>");
        xml.append("</coordinate>");
        return xml.toString();
    }

    public Coordinate initializeFromNode(Realm realm, Node node) {
        Coordinate coordinate = new Coordinate();
        Node xNode = XMLConverterUtility.findNode(node, "x");
        if (xNode != null) {
            String xNodeValue = xNode.getFirstChild().getNodeValue().toString().trim();
            int xValue = Integer.parseInt(xNodeValue);
            coordinate.setAbscissa(xValue);
        }
        Node yNode = XMLConverterUtility.findNode(node, "y");
        if (yNode != null) {
            String yNodeValue = yNode.getFirstChild().getNodeValue().toString().trim();
            int yValue = Integer.parseInt(yNodeValue);
            coordinate.setOrdinate(yValue);
        }
        return coordinate;
    }
}
