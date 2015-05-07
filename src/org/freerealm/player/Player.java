package org.freerealm.player;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import org.freerealm.settlement.Settlement;
import org.freerealm.map.Coordinate;
import org.freerealm.modifier.GeneralModifier;
import org.freerealm.nation.Nation;
import org.freerealm.player.mission.Mission;
import org.freerealm.property.Property;
import org.freerealm.settlement.improvement.SettlementImprovementType;
import org.freerealm.unit.Unit;
import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public interface Player extends GeneralModifier {

    public static final int STATUS_ACTIVE = 0;
    public static final int STATUS_PASSIVE = 1;
    public static final int STATUS_REMOVED = 2;

    public int getId();

    public void setId(int id);

    public int getStatus();

    public void setStatus(int status);

    public String getName();

    public void setName(String name);

    public Iterator<Property> getPropertiesIterator();

    public Property getProperty(String propertyName);

    public void addProperty(Property property);

    public Nation getNation();

    public void setNation(Nation nation);

    public boolean isTurnEnded();

    public void setTurnEnded(boolean turnEnded);

    public int getPopulation();

    public int getSettlementCount();

    public Iterator<Settlement> getSettlementsIterator();

    public void addSettlement(Settlement settlement);

    public void removeSettlement(Settlement settlement);

    public String getNextSettlementName();

    public Iterator<Unit> getUnitsIterator();

    public Unit getUnit(int unitId);

    public int getUnitCount();

    public Unit getActiveUnit();

    public boolean hasUnit(Unit unit);

    public void setActiveUnit(Unit unit);

    public Unit getNextUnit(Unit unit);

    public void addUnit(Unit unit);

    public void removeUnit(Unit unit);

    public List<Unit> getUnitsOfType(UnitType unitType);

    public int getWealth();

    public void setWealth(int wealth);

    public int getTaxRate();

    public void setTaxRate(int taxRate);

    public int getTotalIncome();

    public int getTotalIncomeIf(int tax);

    public int getTotalExpenses();

    public boolean addExploredCoordinate(Coordinate coordinate);

    public List<Coordinate> addExploredCoordinates(List<Coordinate> coordinates);

    public boolean isCoordinateExplored(Coordinate coordinate);

    public void clearExploredCoordinates();

    public List<Coordinate> getExploredCoordinates();

    public Iterator<Coordinate> getExploredCoordinatesIterator();

    public int getExploredCoordinateCount();

    public Color getPrimaryColor();

    public void setPrimaryColor(Color color);

    public Color getSecondaryColor();

    public void setSecondaryColor(Color color);

    public void addMission(Mission mission);

    public void removeMission(Mission mission);

    public void clearMissions();

    public Iterator<Mission> getMissionsIterator();

    public int getMissionCount();

    public int getCompletedMissionCount();

    public int getCompletedMissionPercent();

    public int getFailedMissionCount();

    public int getFailedMissionPercent();

    public boolean isMissionAssigned(int missionId);

    public void addMessage(Message message);

    public int getUnreadMessageCount();

    public Iterator<Message> getUnreadMessagesIterator();

    public Iterator<Message> getMessagesIterator();

    public void clearBuiltTileImprovementCount();

    public Iterator<Integer> getBuiltTileImprovementCountIterator();

    public int getBuiltTileImprovementCount(int typeId);

    public void setBuiltTileImprovementCount(int typeId, int count);

    public int getBuiltSettlementImprovementCount(int typeId);

    public int getSettlementCountHavingImprovementType(int settlementImprovementTypeId);
}
