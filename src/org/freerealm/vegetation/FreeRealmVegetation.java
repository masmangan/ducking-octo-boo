package org.freerealm.vegetation;

import java.util.Iterator;
import java.util.Properties;
import org.freerealm.property.Property;
import org.freerealm.resource.Resource;

/**
 *
 * @author Deniz ARIKAN
 */
public class FreeRealmVegetation implements Vegetation {

    private VegetationType type;
    private Properties customProperties;

    public FreeRealmVegetation() {
        customProperties = new Properties();
    }

    public VegetationType getType() {
        return type;
    }

    public void setType(VegetationType type) {
        this.type = type;
    }

    public String getName() {
        return getType().getName();
    }

    public int getTurnsNeededToClear() {
        return getType().getTurnsNeededToClear();
    }

    public float getMovementCostModifier() {
        return getType().getMovementCostModifier();
    }

    public int getResourceModifier(Resource resource, boolean resourceExists) {
        return getType().getResourceModifier(resource, resourceExists);
    }

    public int getDefenceModifier() {
        return getType().getDefenceModifier();
    }

    public Iterator<Property> getPropertiesIterator() {
        return getType().getPropertiesIterator();
    }

    public boolean hasPropertyNamed(String name) {
        return getType().hasPropertyNamed(name);
    }

    public Property getProperty(String propertyName) {
        return getType().getProperty(propertyName);
    }

    public Object getCustomProperty(Object key) {
        return customProperties.get(key);
    }

    public void addCustomProperty(Object key, Object value) {
        customProperties.put(key, value);
    }

    public Properties getCustomProperties() {
        return customProperties;
    }

    public void setCustomProperties(Properties properties) {
        this.customProperties = properties;
    }
}
