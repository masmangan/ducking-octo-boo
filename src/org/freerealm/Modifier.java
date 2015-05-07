package org.freerealm;

import java.util.Iterator;
import org.freerealm.property.Property;
import org.freerealm.resource.Resource;

/**
 *
 * @author Deniz ARIKAN
 */
public interface Modifier {

    public int getResourceModifier(Resource resource, boolean resourceExists);

    public int getDefenceModifier();

    public Iterator<Property> getPropertiesIterator();

    public boolean hasPropertyNamed(String name);

    public Property getProperty(String propertyName);
}
