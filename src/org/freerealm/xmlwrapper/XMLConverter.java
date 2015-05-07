package org.freerealm.xmlwrapper;

import org.freerealm.Realm;
import org.w3c.dom.Node;

/**
 *
 * @author phoenix
 */
public interface XMLConverter<Type> {

    public String toXML(Type object);

    public Type initializeFromNode(Realm realm, Node node);
}
