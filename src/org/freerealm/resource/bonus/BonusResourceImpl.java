package org.freerealm.resource.bonus;

import java.util.Iterator;
import org.freerealm.tile.FreeRealmTileModifier;
import org.freerealm.tile.TileType;

/**
 *
 * @author Deniz ARIKAN
 */
public class BonusResourceImpl extends FreeRealmTileModifier implements BonusResource {

    public boolean canExistOnTileType(TileType tileType) {
        return hasTileType(tileType);
    }

    public void addExistableTileType(TileType tileType) {
        addTileType(tileType);
    }

    public void removeExistableTileType(TileType tileType) {
        removeTileType(tileType);
    }

    public int getExistableTileTypeCount() {
        return getTileTypeCount();
    }

    public Iterator<TileType> getExistableTileTypesIterator() {
        return getTileTypesIterator();
    }
}
