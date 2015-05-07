package org.freerealm;

import java.util.Iterator;
import java.util.Vector;
import org.freerealm.property.ModifyDefence;
import org.freerealm.property.ModifyResourceProduction;
import org.freerealm.property.Property;
import org.freerealm.resource.Resource;

/**
 *
 * @author Deniz ARIKAN
 */
public class FreeRealmModifier implements Modifier {

    private int id;
    private String name;
    private Vector<Property> properties;

    public FreeRealmModifier() {
        properties = new Vector<Property>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasProperty(Property property) {
        return getProperties().contains(property);
    }

    public Property getProperty(String propertyName) {
        Property returnValue = null;
        Iterator<Property> propertyIterator = properties.iterator();
        while (propertyIterator.hasNext()) {
            Property property = propertyIterator.next();
            if (property.getName().equals(propertyName)) {
                returnValue = property;
            }
        }
        return returnValue;
    }

    public void addProperty(Property property) {
        getProperties().add(property);
    }

    public void removeProperty(Property property) {
        getProperties().remove(property);
    }

    public int getPropertyCount() {
        return getProperties().size();
    }

    public Iterator<Property> getPropertiesIterator() {
        return getProperties().iterator();
    }

    public boolean hasPropertyNamed(String name) {
        Iterator<Property> iterator = getPropertiesIterator();
        while (iterator.hasNext()) {
            Property property = iterator.next();
            if (property.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private Vector<Property> getProperties() {
        return properties;
    }

    public int getResourceModifier(Resource resource, boolean resourceExists) {
        int resourceModifier = 0;
        Iterator<Property> propertyIterator = getProperties().iterator();
        while (propertyIterator.hasNext()) {
            Property property = propertyIterator.next();
            if (property instanceof ModifyResourceProduction) {
                ModifyResourceProduction modifyResourceProduction = (ModifyResourceProduction) property;
                if (resource.equals(modifyResourceProduction.getResource())) {
                    if (!modifyResourceProduction.isModifyingOnlyIfResourceExists() || (modifyResourceProduction.isModifyingOnlyIfResourceExists() && resourceExists)) {
                        resourceModifier = resourceModifier + modifyResourceProduction.getModifier();
                    }
                }
            }
        }
        return resourceModifier;
    }

    public int getDefenceModifier() {
        int defenceModifier = 0;
        Iterator<Property> propertyEditor = getProperties().iterator();
        while (propertyEditor.hasNext()) {
            Property property = propertyEditor.next();
            if (property instanceof ModifyDefence) {
                ModifyDefence increaseDefence = (ModifyDefence) property;
                defenceModifier = defenceModifier + increaseDefence.getModifier();
            }
        }
        return defenceModifier;
    }
}
