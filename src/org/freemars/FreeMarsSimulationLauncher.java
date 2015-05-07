package org.freemars;

import java.awt.Color;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import org.freemars.ai.AIPlayer;
import org.freemars.ai.DecisionModel;
import org.freemars.ai.ExpeditionaryForceDecisionModel;
import org.freemars.ai.ExpeditionaryForcePlayer;
import org.freemars.command.ResetFreeMarsModelCommand;
import org.freemars.controller.FreeMarsController;
import org.freemars.earth.EarthFlightModel;
import org.freemars.earth.Location;
import org.freemars.mission.MissionReader;
import org.freemars.model.FreeMarsModel;
import org.freemars.tile.SpaceshipDebrisCollectable;
import org.freemars.util.PlayerStartCoordinateGenerator;
import org.freerealm.Realm;
import org.freerealm.executor.command.AddPlayerCommand;
import org.freerealm.executor.command.AddRequiredPopulationResourceCommand;
import org.freerealm.executor.command.AddUnitCommand;
import org.freerealm.executor.command.InitializeRealmCommand;
import org.freerealm.executor.command.ReadMapFileCommand;
import org.freerealm.executor.command.SetActivePlayerCommand;
import org.freerealm.executor.command.SetMaximumNumberOfTurnsCommand;
import org.freerealm.executor.command.SetPlayerStatusCommand;
import org.freerealm.executor.command.SetTileCollectableCommand;
import org.freerealm.executor.command.UnitSuspendCommand;
import org.freerealm.map.Coordinate;
import org.freerealm.nation.Nation;
import org.freerealm.player.Player;
import org.freerealm.property.Property;
import org.freerealm.property.SetStartupUnitCountProperty;
import org.freerealm.tile.Tile;
import org.freerealm.unit.Unit;
import org.freerealm.unit.UnitType;

/**
 *
 * @author Deniz ARIKAN
 */
public class FreeMarsSimulationLauncher extends JPanel {
    
    private static final Logger logger = Logger.getLogger(FreeMarsSimulationLauncher.class);
    
    public static void main(final String[] args) {
        initialize();
        int simulationCount = 1;
        logger.info(simulationCount + " simulation(s) will be performed.");
        
        int totalTradeIncome = 0;
        int totalCompletedMissions = 0;
        int totalFailedMissions = 0;
        
        for (int i = 0; i < simulationCount; i++) {
            logger.info("Starting simulation " + (i + 1) + ".");
            long start = System.currentTimeMillis();
            final FreeMarsController freeMarsController = new FreeMarsController();
            freeMarsController.getFreeMarsModel().setMode(FreeMarsModel.SIMULATION_MODE);
            Realm realm = freeMarsController.getFreeMarsModel().getRealm();
            freeMarsController.execute(new ResetFreeMarsModelCommand(freeMarsController.getFreeMarsModel()));
            freeMarsController.execute(new InitializeRealmCommand(freeMarsController.getFreeMarsModel().getRealm()));
            freeMarsController.execute(new SetMaximumNumberOfTurnsCommand(realm, 366));
            freeMarsController.execute(new AddRequiredPopulationResourceCommand(realm, 0, 1));
            freeMarsController.execute(new AddRequiredPopulationResourceCommand(realm, 1, 6));
            freeMarsController.execute(new AddRequiredPopulationResourceCommand(realm, 2, 1));
            EarthFlightModel earthFlightModel = new EarthFlightModel(freeMarsController.getFreeMarsModel().getRealm());
            freeMarsController.getFreeMarsModel().setEarthFlightModel(earthFlightModel);
            InputStream inputStream = ClassLoader.getSystemResourceAsStream("maps/mars_small.frm");
            freeMarsController.execute(new ReadMapFileCommand(realm, inputStream));
            
            Nation nation = realm.getNationManager().getNation(2);
            AIPlayer aiPlayer = new AIPlayer(freeMarsController.getFreeMarsModel().getRealm());
            aiPlayer.setDecisionModel(new DecisionModel(freeMarsController, aiPlayer));
            aiPlayer.setId(freeMarsController.getFreeMarsModel().getRealm().getPlayerManager().getNextAvailablePlayerId());
            aiPlayer.setStatus(Player.STATUS_ACTIVE);
            aiPlayer.setName(nation.getName());
            aiPlayer.setNation(nation);
            aiPlayer.setPrimaryColor(nation.getDefaultColor1());
            aiPlayer.setSecondaryColor(nation.getDefaultColor2());
            Iterator<Property> propertiesIterator = nation.getPropertiesIterator();
            while (propertiesIterator.hasNext()) {
                Property property = propertiesIterator.next();
                aiPlayer.addProperty(property);
            }
            addPlayer(freeMarsController, aiPlayer);
            freeMarsController.execute(new AddPlayerCommand(realm, getRelatedExpeditionaryForcePlayer(freeMarsController, aiPlayer)));
            new MissionReader().readMissions(freeMarsController);
            freeMarsController.execute(new SetActivePlayerCommand(realm, aiPlayer));
            long duration = System.currentTimeMillis() - start;
            logger.info("Simulation summary...");
            logger.info("Population : " + aiPlayer.getPopulation() + " Colony count : " + aiPlayer.getSettlementCount());
            logger.info("Wealth : " + aiPlayer.getWealth() + " Unit count : " + aiPlayer.getUnitCount());
            logger.info("Trade income : " + aiPlayer.getTradeIncome());
            logger.info("Mission count : " + aiPlayer.getMissionCount());
            logger.info("Completed mission count : " + aiPlayer.getCompletedMissionCount());
            logger.info("Completed mission ratio : " + aiPlayer.getCompletedMissionPercent() + "%");
            logger.info("Failed mission count : " + aiPlayer.getFailedMissionCount());
            logger.info("Failed mission ratio : " + aiPlayer.getFailedMissionPercent() + "%");
            logger.info("Simulation " + (i + 1) + " finished successfully in " + getDurationBreakdown(duration) + ".");
            logger.info("-----");
            
            totalTradeIncome = totalTradeIncome + aiPlayer.getTradeIncome();
            totalCompletedMissions = totalCompletedMissions + aiPlayer.getCompletedMissionCount();
            totalFailedMissions = totalFailedMissions + aiPlayer.getFailedMissionCount();
        }
        logger.info(simulationCount + " simulation(s) finished successfully.");
        logger.info("Mean trade income : " + (totalTradeIncome / simulationCount));
        logger.info("Completed mission(s) : " + totalCompletedMissions);
        logger.info("Failed mission(s) : " + totalFailedMissions);
        System.exit(0);
    }
    
    private static void initialize() {
        FreeMarsInitializer.initializeLogger();
        FreeMarsInitializer.initializeGameFolders();
        FreeMarsInitializer.initializeTags();
        logger.info("Free Mars simulator initialized successfully.");
    }
    
    private static void addPlayer(FreeMarsController freeMarsController, Player player) {
        Realm realm = freeMarsController.getFreeMarsModel().getRealm();
        freeMarsController.execute(new AddPlayerCommand(realm, player));
        Coordinate coordinate = PlayerStartCoordinateGenerator.generate(freeMarsController);
        logger.info("Starting coordinate is " + coordinate + ".");
        int scoutCount = 1;
        UnitType scoutType = realm.getUnitTypeManager().getUnitType("Scout");
        for (int i = 0; i < scoutCount; i++) {
            freeMarsController.execute(new AddUnitCommand(realm, player, new Unit(realm, scoutType, coordinate, player)));
        }
        
        UnitType colonizerType = realm.getUnitTypeManager().getUnitType("Colonizer");
        int startupColonizerCount = 2;
        SetStartupUnitCountProperty setStartupUnitCount = (SetStartupUnitCountProperty) player.getProperty(SetStartupUnitCountProperty.NAME);
        if (setStartupUnitCount != null && colonizerType.equals(setStartupUnitCount.getUnitType())) {
            startupColonizerCount = setStartupUnitCount.getCount();
        }
        for (int i = 0; i < startupColonizerCount; i++) {
            freeMarsController.execute(new AddUnitCommand(realm, player, new Unit(realm, colonizerType, coordinate, player)));
        }
        UnitType engineerType = realm.getUnitTypeManager().getUnitType("Engineer");
        freeMarsController.execute(new AddUnitCommand(realm, player, new Unit(realm, engineerType, coordinate, player)));
        freeMarsController.execute(new AddUnitCommand(realm, player, new Unit(realm, engineerType, coordinate, player)));
        
        UnitType shuttleType = realm.getUnitTypeManager().getUnitType("Shuttle");
        Unit shuttle = new Unit(realm, shuttleType, coordinate, player);
        freeMarsController.execute(new AddUnitCommand(realm, player, shuttle));
        
        freeMarsController.execute(new UnitSuspendCommand(realm, player, shuttle));
        freeMarsController.getFreeMarsModel().getEarthFlightModel().addUnitLocation(shuttle, Location.MARS_ORBIT);
        addLandingModuleDebris(freeMarsController, realm, coordinate);
    }
    
    private static void addLandingModuleDebris(FreeMarsController freeMarsController, Realm realm, Coordinate centerCoordinate) {
        for (int i = 3; i < 5; i++) {
            List<Coordinate> coordinates = freeMarsController.getFreeMarsModel().getRealm().getCircleCoordinates(centerCoordinate, i);
            for (int j = 0; j < 6; j++) {
                Coordinate coordinate = getRandomCoordinateForCollectable(realm, coordinates);
                if (coordinate != null) {
                    SpaceshipDebrisCollectable spaceshipDebrisCollectable = new SpaceshipDebrisCollectable();
                    spaceshipDebrisCollectable.setExecutor(freeMarsController);
                    Random random = new Random();
                    int resourceRandom = random.nextInt(5);
                    switch (resourceRandom) {
                        case 0:
                            spaceshipDebrisCollectable.setResourceId(2);
                            break;
                        case 1:
                            spaceshipDebrisCollectable.setResourceId(3);
                            break;
                        case 2:
                            spaceshipDebrisCollectable.setResourceId(6);
                            break;
                        case 3:
                            spaceshipDebrisCollectable.setResourceId(7);
                            break;
                        case 4:
                            spaceshipDebrisCollectable.setResourceId(8);
                            break;
                    }
                    int amountRandom = random.nextInt(5);
                    spaceshipDebrisCollectable.setAmount(400 + 50 * amountRandom);
                    freeMarsController.execute(new SetTileCollectableCommand(freeMarsController.getFreeMarsModel().getTile(coordinate), spaceshipDebrisCollectable));
                }
            }
        }
    }
    
    private static Coordinate getRandomCoordinateForCollectable(Realm realm, List<Coordinate> coordinates) {
        for (int i = 0; i < 5; i++) {
            Random randomGenerator = new Random();
            int coordinateIndex = randomGenerator.nextInt(coordinates.size());
            Coordinate coordinate = coordinates.get(coordinateIndex);
            Tile tile = realm.getTile(coordinate);
            if (tile != null && tile.getType().getId() != 6 && tile.getType().getId() != 7 && tile.getType().getId() != 10) {
                return coordinate;
            }
        }
        return null;
    }
    
    private static ExpeditionaryForcePlayer getRelatedExpeditionaryForcePlayer(FreeMarsController freeMarsController, Player player) {
        ExpeditionaryForcePlayer expeditionaryForcePlayer = new ExpeditionaryForcePlayer(freeMarsController.getFreeMarsModel().getRealm());
        expeditionaryForcePlayer.setId(freeMarsController.getFreeMarsModel().getRealm().getPlayerManager().getNextAvailablePlayerId());
        expeditionaryForcePlayer.setName(player.getNation().getAdjective() + " expeditionary force");
        expeditionaryForcePlayer.setNation(player.getNation());
        expeditionaryForcePlayer.setPrimaryColor(Color.black);
        expeditionaryForcePlayer.setSecondaryColor(Color.lightGray);
        expeditionaryForcePlayer.setTargetPlayerId(player.getId());
        expeditionaryForcePlayer.setDecisionModel(new ExpeditionaryForceDecisionModel(freeMarsController, expeditionaryForcePlayer));
        freeMarsController.execute(new SetPlayerStatusCommand(expeditionaryForcePlayer, Player.STATUS_PASSIVE));
        UnitType infantryUnitType = freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Infantry");
        for (int i = 0; i < ExpeditionaryForcePlayer.BASE_INFANTRY_COUNT; i++) {
            Unit infantryUnit = new Unit(freeMarsController.getFreeMarsModel().getRealm(), infantryUnitType, null, expeditionaryForcePlayer);
            freeMarsController.execute(new AddUnitCommand(freeMarsController.getFreeMarsModel().getRealm(), expeditionaryForcePlayer, infantryUnit, Unit.UNIT_SUSPENDED));
        }
        UnitType mechUnitType = freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("Mech");
        for (int i = 0; i < ExpeditionaryForcePlayer.BASE_MECH_COUNT; i++) {
            Unit mechUnit = new Unit(freeMarsController.getFreeMarsModel().getRealm(), mechUnitType, null, expeditionaryForcePlayer);
            freeMarsController.execute(new AddUnitCommand(freeMarsController.getFreeMarsModel().getRealm(), expeditionaryForcePlayer, mechUnit, Unit.UNIT_SUSPENDED));
        }
        UnitType LGTUnitType = freeMarsController.getFreeMarsModel().getRealm().getUnitTypeManager().getUnitType("LGT");
        for (int i = 0; i < ExpeditionaryForcePlayer.BASE_LGT_COUNT; i++) {
            Unit LGTUnit = new Unit(freeMarsController.getFreeMarsModel().getRealm(), LGTUnitType, null, expeditionaryForcePlayer);
            freeMarsController.execute(new AddUnitCommand(freeMarsController.getFreeMarsModel().getRealm(), expeditionaryForcePlayer, LGTUnit, Unit.UNIT_SUSPENDED));
        }
        int expeditionaryForceFlightTurns = Integer.parseInt(freeMarsController.getFreeMarsModel().getRealm().getProperty("expeditionary_force_flight_turns"));
        expeditionaryForcePlayer.setEarthToMarsFlightTurns(expeditionaryForceFlightTurns);
        return expeditionaryForcePlayer;
    }
    
    public static String getDurationBreakdown(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        StringBuilder sb = new StringBuilder(64);
        if (minutes > 0) {
            sb.append(minutes);
            sb.append(" minute(s) ");
        }
        sb.append(seconds);
        sb.append(" second(s)");
        return sb.toString();
    }
}
