package org.freerealm.property;

import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public class SetStartupUnitCountProperty implements Property {

    public static final String NAME = "set_startup_unit_count_property";
    private UnitType unitType;
    private int count;

    public String getName() {
        return NAME;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
