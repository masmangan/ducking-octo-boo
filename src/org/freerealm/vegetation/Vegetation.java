package org.freerealm.vegetation;

import org.freerealm.Customizable;
import org.freerealm.tile.TileModifier;

/**
 *
 * @author Deniz ARIKAN
 */
public interface Vegetation extends TileModifier, Customizable {

    public VegetationType getType();

    public void setType(VegetationType type);

    public String getName();

    public int getTurnsNeededToClear();
}
