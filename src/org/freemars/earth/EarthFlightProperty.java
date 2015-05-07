package org.freemars.earth;

import org.freerealm.property.Property;

/**
 *
 * @author Deniz ARIKAN
 */
public class EarthFlightProperty implements Property {

    private int earthMarsTravelTime;
    private int hydrogenConsumption;

    public EarthFlightProperty() {
        earthMarsTravelTime = 0;
    }

    public String getName() {
        return "EarthFlight";
    }

    public int getEarthMarsTravelTime() {
        return earthMarsTravelTime;
    }

    public void setEarthMarsTravelTime(int earthMarsTravelTime) {
        this.earthMarsTravelTime = earthMarsTravelTime;
    }

    public int getHydrogenConsumption() {
        return hydrogenConsumption;
    }

    public void setHydrogenConsumption(int hydrogenConsumption) {
        this.hydrogenConsumption = hydrogenConsumption;
    }
}
