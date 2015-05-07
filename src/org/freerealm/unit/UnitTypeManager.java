package org.freerealm.unit;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author Deniz ARIKAN
 */
public class UnitTypeManager {

    private TreeMap<Integer, UnitType> unitTypes = null;

    public UnitTypeManager() {
        unitTypes = new TreeMap<Integer, UnitType>();
    }

    public UnitType getUnitType(int id) {
        return (UnitType) getUnitTypes().get(id);
    }

    public UnitType getUnitType(String name) {
        UnitType returnValue = null;
        Iterator unitTypeIterator = unitTypes.entrySet().iterator();
        while (unitTypeIterator.hasNext()) {
            Entry entry = (Entry) unitTypeIterator.next();
            UnitType unitType = (UnitType) entry.getValue();
            if (unitType.getName().equals(name)) {
                returnValue = unitType;
                break;
            }
        }
        return returnValue;
    }

    public int getUnitTypeCount() {
        return unitTypes.size();
    }

    public TreeMap<Integer, UnitType> getUnitTypes() {
        return unitTypes;
    }

    public Iterator<UnitType> getUnitTypesIterator() {
        return getUnitTypes().values().iterator();
    }
}
