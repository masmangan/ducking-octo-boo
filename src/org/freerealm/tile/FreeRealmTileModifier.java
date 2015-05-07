package org.freerealm.tile;

import java.util.HashMap;
import java.util.Iterator;
import org.freerealm.FreeRealmModifier;
import org.freerealm.property.Property;
import org.freerealm.property.SetMovementCostProperty;

/**
 *
 * @author Deniz ARIKAN
 */
public class FreeRealmTileModifier extends FreeRealmModifier implements TileModifier {

    private HashMap<Integer, TileType> tileTypes;

    public FreeRealmTileModifier() {
        tileTypes = new HashMap<Integer, TileType>();
    }

    public boolean hasTileType(TileType tileType) {
        return getTileTypes().containsKey(tileType.getId());
    }

    public void addTileType(TileType tileType) {
        getTileTypes().put(tileType.getId(), tileType);
    }

    public void removeTileType(TileType tileType) {
        getTileTypes().remove(tileType.getId());
    }

    public int getTileTypeCount() {
        return getTileTypes().size();
    }

    public Iterator<TileType> getTileTypesIterator() {
        return getTileTypes().values().iterator();
    }

    public float getMovementCostModifier() {
        float movementCost = 0;
        Iterator<Property> propertyEditor = getPropertiesIterator();
        while (propertyEditor.hasNext()) {
            Property property = propertyEditor.next();
            if (property instanceof SetMovementCostProperty) {
                SetMovementCostProperty setMovementCost = (SetMovementCostProperty) property;
                if (movementCost == 0 || setMovementCost.getMovementCost() < movementCost) {
                    movementCost = setMovementCost.getMovementCost();
                }
            }
        }
        return movementCost;
    }

    private HashMap<Integer, TileType> getTileTypes() {
        return tileTypes;
    }
}
