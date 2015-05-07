package org.freerealm.resource.bonus;

import java.util.Iterator;
import org.freerealm.tile.TileModifier;
import org.freerealm.tile.TileType;

/**
 *
 * @author Deniz ARIKAN
 */
public interface BonusResource extends TileModifier {

    public int getId();

    public String getName();

    public boolean canExistOnTileType(TileType tileType);

    public void addExistableTileType(TileType tileType);

    public void removeExistableTileType(TileType tileType);

    public int getExistableTileTypeCount();

    public Iterator<TileType> getExistableTileTypesIterator();
}
