package org.freerealm.tile.improvement;

import java.util.Iterator;
import org.freerealm.tile.TileBuildable;
import org.freerealm.tile.TileModifier;
import org.freerealm.tile.TileType;

/**
 *
 * @author Deniz ARIKAN
 */
public interface TileImprovementType extends TileModifier, TileBuildable {

    public int getId();

    public char getSymbol();
    
    public boolean canBeBuiltOnTileType(TileType tileType);

    public void addBuildableTileType(TileType tileType);

    public void removeBuildableTileType(TileType tileType);

    public int getBuildableTileTypeCount();

    public Iterator<TileType> getBuildableTileTypesIterator();
}
