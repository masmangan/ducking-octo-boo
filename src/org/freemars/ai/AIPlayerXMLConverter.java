package org.freemars.ai;

import org.freemars.player.FreeMarsPlayerXMLConverter;
import org.freerealm.Realm;
import org.w3c.dom.Node;
import org.freerealm.xmlwrapper.XMLConverter;

/**
 *
 * @author Deniz ARIKAN
 */
public class AIPlayerXMLConverter implements XMLConverter<AIPlayer> {

    public String toXML(AIPlayer player) {
        StringBuilder xml = new StringBuilder();
        xml.append("<AIPlayer>");
        xml.append(System.getProperty("file.separator"));
        xml.append(FreeMarsPlayerXMLConverter.getInnerXML(player));
        xml.append(System.getProperty("file.separator"));
        xml.append("</AIPlayer>");
        return xml.toString();
    }

    public AIPlayer initializeFromNode(Realm realm, Node node) {
        AIPlayer player = new AIPlayer(realm);
        FreeMarsPlayerXMLConverter.populatePlayerFromNode(player, realm, node);
        return player;
    }
}
