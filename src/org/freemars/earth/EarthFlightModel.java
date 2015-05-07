package org.freemars.earth;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.freemars.earth.order.RelocateUnitOrder;
import org.freerealm.Realm;
import org.freerealm.resource.Resource;
import org.freerealm.unit.Unit;
import org.freerealm.unit.UnitType;
import org.w3c.dom.Node;

/**
 *
 * @author Deniz ARIKAN
 */
public class EarthFlightModel {

    public static final int UNIT_RELOCATION_UPDATE = 0;
    public static final int UNIT_SELECTION_UPDATE = 1;
    public static final int BUY_SELL_UPDATE = 2;
    public static final int REALM_TURN_UPDATE = 3;
    public static final int PURCHASE_EARTH_UNIT_UPDATE = 4;
    public static final int SELL_UNIT_TO_EARTH_UPDATE = 5;
    private Realm realm;
    private HashMap<Unit, Location> unitLocations;
    private EarthPricesManager earthPricesManager;
    private Unit earthDialogSelectedUnit;
    private int lastUpdateTurn = 0;

    public EarthFlightModel(Realm realm) {
        this.realm = realm;
        unitLocations = new HashMap<Unit, Location>();
        earthPricesManager = new EarthPricesManager(realm.getResourceManager());
    }

    public String toXML() {
        StringBuffer xml = new StringBuffer();
        xml.append("<earthFlightModel>\n");
        xml.append("<unitLocations>\n");
        Iterator<Entry<Unit, Location>> iterator = getUnitLocationsIterator();
        while (iterator.hasNext()) {
            Entry<Unit, Location> entry = iterator.next();
            if (entry.getKey() != null) {
                xml.append("<unitLocation>\n");
                xml.append("<playerId>" + entry.getKey().getPlayer().getId() + "</playerId>\n");
                xml.append("<unitId>" + entry.getKey().getId() + "</unitId>\n");
                xml.append("<location>" + entry.getValue().getId() + "</location>\n");
                xml.append("</unitLocation>\n");
            }
        }
        xml.append("</unitLocations>\n");
        xml.append((new EarthPricesManagerXMLConverter()).toXML(earthPricesManager));
        xml.append("<lastUpdateTurn>" + lastUpdateTurn + "</lastUpdateTurn>\n");
        xml.append("</earthFlightModel>\n");
        return xml.toString();
    }

    public void initializeFrom(Node node) {
        Node unitLocationsNode = findNode(node, "unitLocations");
        for (Node subNode = unitLocationsNode.getFirstChild(); subNode != null; subNode = subNode.getNextSibling()) {
            if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                if (subNode.getNodeName().equals("unitLocation")) {
                    Node playerIdNode = findNode(subNode, "playerId");
                    Node unitIdNode = findNode(subNode, "unitId");
                    Node locationNode = findNode(subNode, "location");
                    int playerIdValue = Integer.parseInt(playerIdNode.getFirstChild().getNodeValue());
                    int unitIdValue = Integer.parseInt(unitIdNode.getFirstChild().getNodeValue());
                    int locationValue = Integer.parseInt(locationNode.getFirstChild().getNodeValue());
                    Unit unit = realm.getPlayerManager().getPlayer(playerIdValue).getUnit(unitIdValue);
                    Location location = Location.getLocation(locationValue);
                    addUnitLocation(unit, location);
                }
            }
        }
        Node earthPricesManagerNode = findNode(node, "EarthPricesManager");
        earthPricesManager = new EarthPricesManagerXMLConverter().initializeFromNode(realm, earthPricesManagerNode);
        Node lastUpdateTurnNode = findNode(node, "lastUpdateTurn");
        lastUpdateTurn = Integer.parseInt(lastUpdateTurnNode.getFirstChild().getNodeValue());
    }

    private static Node findNode(Node node, String nodeName) {
        int length = node.getChildNodes().getLength();
        for (int i = 0; i < length; i++) {
            Node subNode = node.getChildNodes().item(i);
            if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                if (nodeName.equals(subNode.getNodeName())) {
                    return subNode;
                }
            }
        }
        return null;
    }

    public Realm getRealm() {
        return realm;
    }

    public void addUnitLocation(Unit unit, Location location) {
        unitLocations.put(unit, location);
    }

    public void removeUnitLocation(Unit unit) {
        unitLocations.remove(unit);
    }

    public Location getUnitLocation(Unit unit) {
        return unitLocations.get(unit);
    }

    public Iterator<Unit> getUnitsIterator() {
        return unitLocations.keySet().iterator();
    }

    public Iterator<Entry<Unit, Location>> getUnitLocationsIterator() {
        return unitLocations.entrySet().iterator();
    }

    /**
     * Earth will sell given unit type at this price.
     */
    public int getEarthSellsAtPrice(UnitType unitType) {
        return earthPricesManager.getEarthSellsAtPrice(getRealm(), unitType);
    }

    /**
     * Earth will sell given resource at this price.
     */
    public int getEarthSellsAtPrice(Resource resource) {
        return earthPricesManager.getEarthSellsAtPrice(resource);
    }

    /**
     * Earth will buy given resource at this price.
     */
    public int getEarthBuysAtPrice(Resource resource) {
        return earthPricesManager.getEarthBuysAtPrice(resource);
    }

    public void addResource(Resource resource, int quantity) {
        earthPricesManager.addResource(resource, quantity);
    }

    public void removeResource(Resource resource, int quantity) {
        earthPricesManager.removeResource(resource, quantity);
    }

    public Unit getEarthDialogSelectedUnit() {
        return earthDialogSelectedUnit;
    }

    public void setEarthDialogSelectedUnit(Unit earthDialogSelectedUnit) {
        this.earthDialogSelectedUnit = earthDialogSelectedUnit;
    }

    public int findETA(Unit unit) {
        if (unit.getCurrentOrder() != null && unit.getCurrentOrder() instanceof RelocateUnitOrder) {
            RelocateUnitOrder relocateUnitOrder = (RelocateUnitOrder) unit.getCurrentOrder();
            return relocateUnitOrder.getRemainingTurns();
        }
        return 0;
    }

    public String findDestination(Unit unit) {
        if (unit.getCurrentOrder() != null && unit.getCurrentOrder() instanceof RelocateUnitOrder) {
            RelocateUnitOrder relocateUnitOrder = (RelocateUnitOrder) unit.getCurrentOrder();
            Location destination = relocateUnitOrder.getDestination();
            if (destination.equals(Location.EARTH)) {
                return "Earth";
            } else if (destination.equals(Location.MARS)) {
                if (relocateUnitOrder.getLandOnColony() != null) {
                    return relocateUnitOrder.getLandOnColony().getName();
                }
            } else if (destination.equals(Location.MARS_ORBIT)) {
                return "Mars orbit";
            }
        }
        return "";
    }
}
