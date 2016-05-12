package org.freemars.unit.automater;

import org.freemars.controller.FreeMarsController;
import org.freerealm.Realm;
import org.freerealm.xmlwrapper.TagManager;
import org.freerealm.xmlwrapper.XMLConverter;
import org.w3c.dom.Node;

/**
 *
 * @author Deniz ARIKAN
 */
public class PeppaAutomaterXMLConverter implements XMLConverter<PeppaAutomater> {

    public String toXML(EngineerAutomater engineerAutomater) {
        StringBuffer xml = new StringBuffer();
        xml.append("<freeMarsEngineerAutomater />\n");
        return xml.toString();
    }

    public PeppaAutomater initializeFromNode(Realm realm, Node node) {
        PeppaAutomater peppaAutomater = new PeppaAutomater();
        FreeMarsController freeMarsController = (FreeMarsController) TagManager.getObjectFromPool("freeMarsController");
        peppaAutomater.setFreeMarsController(freeMarsController);
        return peppaAutomater;
    }

	@Override
	public String toXML(PeppaAutomater object) {
        StringBuffer xml = new StringBuffer();
        xml.append("<freeMarsPeppaAutomater />\n");
        return xml.toString();
	}
}
