package org.freerealm.tile;

import java.util.HashMap;
import org.freerealm.unit.Unit;
import org.freerealm.settlement.Settlement;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import org.freerealm.player.Player;
import org.freerealm.resource.Resource;
import org.freerealm.resource.bonus.BonusResource;
import org.freerealm.tile.improvement.TileImprovementType;
import org.freerealm.unit.UnitType;
import org.freerealm.vegetation.Vegetation;

/**
 *
 * @author Deniz ARIKAN
 */
public class FreeRealmTile implements Tile {

    private TileType type;
    private Vegetation vegetation;
    private Settlement settlement;
    private Vector<TileImprovementType> improvements;
    private TreeMap<Integer, Unit> units;
    private BonusResource bonusResource;
    private Collectable collectable;
    private HashMap<String, TileModifier> customModifiers;
    private Properties customProperties;

    public FreeRealmTile() {
        units = new TreeMap<Integer, Unit>();
        improvements = new Vector<TileImprovementType>();
        customModifiers = new HashMap<String, TileModifier>();
        customProperties = new Properties();
    }

    public FreeRealmTile(TileType type) {
        this();
        this.type = type;
    }

    public int getProduction(Resource resource) {
        return getType().getProduction(resource) + getResourceModifier(resource);
    }

    public void putUnit(Unit unit) {
        units.put(unit.getId(), unit);
    }

    public void removeUnit(Unit unit) {
        units.remove(unit.getId());
    }

    public int getNumberOfUnits() {
        return getUnits().size();
    }

    public Iterator<Unit> getUnitsIterator() {
        return getUnits().values().iterator();
    }

    public TreeMap<Integer, Unit> getUnits() {
        return units;
    }

    public int getNumberOfUnitsOfType(UnitType unitType) {
        int numberOfUnitsOfType = 0;
        Iterator<Unit> iterator = getUnitsIterator();
        while (iterator.hasNext()) {
            Unit unit = iterator.next();
            if (unit.getType().equals(unitType)) {
                numberOfUnitsOfType = numberOfUnitsOfType + 1;
            }
        }
        return numberOfUnitsOfType;
    }

    public Settlement getSettlement() {
        return settlement;
    }

    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public Player getOccupiedByPlayer() {
        Iterator<Unit> iterator = getUnitsIterator();
        if (iterator.hasNext()) {
            return iterator.next().getPlayer();
        }
        return null;
    }

    public float getMovementCost() {
        float typeMovementCost = getType().getMovementCost();
        if (getVegetation() != null) {
            if (getVegetation().getMovementCostModifier() > typeMovementCost) {
                typeMovementCost = getVegetation().getMovementCostModifier();
            }
        }
        float improvementMovementCost = getImprovementMovementCost();
        if (typeMovementCost > improvementMovementCost) {
            typeMovementCost = improvementMovementCost;
        }
        return typeMovementCost;
    }

    public boolean hasImprovement(TileImprovementType improvement) {
        return getImprovements().contains(improvement);
    }

    public Vegetation getVegetation() {
        return vegetation;
    }

    public void setVegetation(Vegetation vegetation) {
        this.vegetation = vegetation;
    }

    public Iterator<TileImprovementType> getImprovementsIterator() {
        return getImprovements().iterator();
    }

    public void addImprovement(TileImprovementType improvement) {
        getImprovements().add(improvement);
    }

    public void removeImprovement(TileImprovementType improvement) {
        getImprovements().remove(improvement);
    }

    public int getImprovementCount() {
        return getImprovements().size();
    }

    public void clearImprovements() {
        getImprovements().clear();
    }

    public BonusResource getBonusResource() {
        return bonusResource;
    }

    public void setBonusResource(BonusResource bonusResource) {
        this.bonusResource = bonusResource;
    }

    public void clearCustomModifiers() {
        customModifiers.clear();
    }

    public int getCustomModifierCount() {
        return customModifiers.size();
    }

    public Iterator<String> getCustomModifierNamesIterator() {
        return customModifiers.keySet().iterator();
    }

    public TileModifier getCustomModifier(String modifierName) {
        return customModifiers.get(modifierName);
    }

    public void addCustomModifier(String modifierName, TileModifier tileModifier) {
        customModifiers.put(modifierName, tileModifier);
    }

    public void removeCustomModifier(String modifierName) {
        customModifiers.remove(modifierName);
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

    private Vector<TileImprovementType> getImprovements() {
        return improvements;
    }

    private float getImprovementMovementCost() {
        float improvementMovementCost = Float.MAX_VALUE;
        Iterator<TileImprovementType> improvemenIterator = getImprovementsIterator();
        while (improvemenIterator.hasNext()) {
            TileImprovementType improvement = improvemenIterator.next();
            if (improvement.getMovementCostModifier() != 0 && improvement.getMovementCostModifier() < improvementMovementCost) {
                improvementMovementCost = improvement.getMovementCostModifier();
            }
        }
        return improvementMovementCost;
    }

    private int getResourceModifier(Resource resource) {
        int resourceModifier = 0;
        boolean tileHasResource = (getType().getProduction(resource) > 0);
        Iterator<TileImprovementType> improvementsIterator = getImprovementsIterator();
        while (improvementsIterator.hasNext()) {
            TileImprovementType improvement = improvementsIterator.next();
            resourceModifier = resourceModifier + improvement.getResourceModifier(resource, tileHasResource);
        }
        if (getVegetation() != null) {
            resourceModifier = resourceModifier + getVegetation().getResourceModifier(resource, tileHasResource);
        }
        if (getBonusResource() != null) {
            resourceModifier = resourceModifier + getBonusResource().getResourceModifier(resource, tileHasResource);
        }
        Iterator<TileModifier> customModifiersIterator = customModifiers.values().iterator();
        while (customModifiersIterator.hasNext()) {
            TileModifier tileModifier = customModifiersIterator.next();
            resourceModifier = resourceModifier + tileModifier.getResourceModifier(resource, tileHasResource);
        }
        return resourceModifier;
    }

    public Collectable getCollectable() {
        return collectable;
    }

    public void setCollectable(Collectable collectable) {
        this.collectable = collectable;
    }
}
