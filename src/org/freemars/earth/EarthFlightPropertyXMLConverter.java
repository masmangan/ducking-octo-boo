package org.freemars.earth;

import org.freerealm.Realm;
import org.freerealm.xmlwrapper.XMLConverter;
import org.w3c.dom.Node;

/**
 *
 * @author Deniz ARIKAN
 */
public class EarthFlightPropertyXMLConverter implements XMLConverter<EarthFlightProperty> {

    public String toXML(EarthFlightProperty earthFlightProperty) {
        StringBuffer xml = new StringBuffer();
        xml.append("<EarthFlight");
        xml.append(" earthMarsTravelTime=\"" + earthFlightProperty.getEarthMarsTravelTime() + "\" ");
        xml.append(" hydrogenConsumption=\"" + earthFlightProperty.getHydrogenConsumption() + "\"");
        xml.append("/>");
        return xml.toString();
    }

    public EarthFlightProperty initializeFromNode(Realm realm, Node node) {
        EarthFlightProperty earthFlightProperty = new EarthFlightProperty();
        String earthMarsTravelTimeValue = node.getAttributes().getNamedItem("earthMarsTravelTime").getNodeValue();
        earthFlightProperty.setEarthMarsTravelTime(Integer.parseInt(earthMarsTravelTimeValue));
        String hydrogenConsumptionValue = node.getAttributes().getNamedItem("hydrogenConsumption").getNodeValue();
        earthFlightProperty.setHydrogenConsumption(Integer.parseInt(hydrogenConsumptionValue));
        return earthFlightProperty;
    }
}
