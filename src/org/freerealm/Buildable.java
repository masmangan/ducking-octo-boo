package org.freerealm;

/**
 *
 * @author Deniz ARIKAN
 */
public interface Buildable {

    public String getName();

    public int getPrerequisiteCount();

    public int getProductionCost();

    public int getUpkeepCost();

}
