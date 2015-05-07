package org.freemars.ai;

import org.freerealm.Realm;
import org.w3c.dom.Node;
import org.freerealm.xmlwrapper.XMLConverter;
import org.freerealm.xmlwrapper.XMLConverterUtility;
import org.freerealm.xmlwrapper.player.FreeRealmPlayerXMLConverter;

/**
 *
 * @author Deniz ARIKAN
 */
public class ExpeditionaryForcePlayerXMLConverter implements XMLConverter<ExpeditionaryForcePlayer> {

    public String toXML(ExpeditionaryForcePlayer player) {
        StringBuffer xml = new StringBuffer();
        xml.append("<ExpeditionaryForcePlayer>\n");
        xml.append(new FreeRealmPlayerXMLConverter().getInnerXML(player) + "\n");
        xml.append("<targetPlayerId>" + player.getTargetPlayerId() + "</targetPlayerId>");
        xml.append("</ExpeditionaryForcePlayer>");
        return xml.toString();
    }

    public ExpeditionaryForcePlayer initializeFromNode(Realm realm, Node node) {
        ExpeditionaryForcePlayer expeditionaryForcePlayer = new ExpeditionaryForcePlayer(realm);
        new FreeRealmPlayerXMLConverter().populatePlayerFromNode(expeditionaryForcePlayer, realm, node);

        Node targetPlayerIdNode = XMLConverterUtility.findNode(node, "targetPlayerId");
        if (targetPlayerIdNode != null) {
            expeditionaryForcePlayer.setTargetPlayerId(Integer.parseInt(targetPlayerIdNode.getFirstChild().getNodeValue()));
        }
        int expeditionaryForceFlightTurns = Integer.parseInt(realm.getProperty("expeditionary_force_flight_turns"));
        expeditionaryForcePlayer.setEarthToMarsFlightTurns(expeditionaryForceFlightTurns);
        return expeditionaryForcePlayer;
    }
}
