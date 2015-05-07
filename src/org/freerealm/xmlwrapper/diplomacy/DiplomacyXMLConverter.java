package org.freerealm.xmlwrapper.diplomacy;

import java.util.Iterator;
import org.freerealm.Realm;
import org.freerealm.diplomacy.Diplomacy;
import org.freerealm.diplomacy.PlayerRelation;
import org.freerealm.player.Player;
import org.freerealm.xmlwrapper.XMLConverter;
import org.freerealm.xmlwrapper.XMLConverterUtility;
import org.w3c.dom.Node;

/**
 *
 * @author Deniz ARIKAN
 */
public class DiplomacyXMLConverter implements XMLConverter<Diplomacy> {

    public String toXML(Diplomacy diplomacy) {
        StringBuffer xml = new StringBuffer();
        xml.append("<diplomacy>\n");
        xml.append(getInnerXML(diplomacy));
        xml.append("</diplomacy>\n");
        return xml.toString();
    }

    public String getInnerXML(Diplomacy diplomacy) {
        StringBuffer xml = new StringBuffer();
        xml.append("<relations>\n");
        Iterator<PlayerRelation> iterator = diplomacy.getPlayerRelationsIterator();
        while (iterator.hasNext()) {
            PlayerRelation playerRelation = iterator.next();
            xml.append("<relation>\n");
            xml.append("<player1>" + playerRelation.getPlayer1().getId() + "</player1>");
            xml.append("<player2>" + playerRelation.getPlayer2().getId() + "</player2>");
            xml.append("<status>" + diplomacy.getPlayerRelationStatus(playerRelation) + "</status>");
            xml.append("</relation>\n");
        }
        xml.append("</relations>\n");
        return xml.toString();
    }

    public Diplomacy initializeFromNode(Realm realm, Node node) {
        Diplomacy diplomacy = new Diplomacy();
        Node tileTypesNode = XMLConverterUtility.findNode(node, "relations");
        for (Node subNode = tileTypesNode.getFirstChild(); subNode != null; subNode = subNode.getNextSibling()) {
            if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                int player1Id = Integer.parseInt(XMLConverterUtility.findNode(subNode, "player1").getFirstChild().getNodeValue());
                int player2Id = Integer.parseInt(XMLConverterUtility.findNode(subNode, "player2").getFirstChild().getNodeValue());
                int status = Integer.parseInt(XMLConverterUtility.findNode(subNode, "status").getFirstChild().getNodeValue());
                Player player1 = realm.getPlayerManager().getPlayer(player1Id);
                Player player2 = realm.getPlayerManager().getPlayer(player2Id);
                diplomacy.addPlayerRelation(player1, player2, status);
            }
        }
        return diplomacy;
    }
}
