package org.freemars.controller.action.file;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.freerealm.executor.command.AddPlayerCommand;
import org.freerealm.executor.command.AddUnitCommand;
import org.freerealm.executor.command.CreateMapCommand;
import org.freerealm.executor.command.ReadMapFileCommand;
import org.freerealm.executor.command.SetActivePlayerCommand;
import java.awt.Color;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;
import org.freerealm.map.Coordinate;
import org.freerealm.tile.Tile;
import org.freerealm.tile.TileType;
import org.freemars.ai.AIPlayer;
import org.freerealm.nation.Nation;
import org.freerealm.unit.Unit;
import org.freerealm.unit.UnitType;
import org.freemars.controller.FreeMarsController;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import org.apache.log4j.Logger;
import org.freemars.command.ResetFreeMarsModelCommand;
import org.freemars.ai.DecisionModel;
import org.freemars.earth.EarthFlightModel;
import org.freemars.earth.Location;
import org.freemars.message.FirstArrivalToMarsMessage;
import org.freemars.mission.MissionReader;
import org.freemars.model.FreeMarsModel;
import org.freemars.ai.ExpeditionaryForceDecisionModel;
import org.freerealm.Realm;
import org.freerealm.executor.command.InitializeRealmCommand;
import org.freerealm.executor.command.UnitSuspendCommand;
import org.freemars.ui.wizard.newgame.NewGameWizard;
import org.freemars.model.wizard.newgame.NewGameOptions;
import org.freemars.player.FreeMarsPlayer;
import org.freemars.ai.ExpeditionaryForcePlayer;
import org.freemars.controller.viewcommand.ClearPaintModelsCommand;
import org.freemars.controller.viewcommand.RepaintMapPanelCommand;
import org.freemars.controller.viewcommand.UpdateExploredAreaPaintModelsCommand;
import org.freemars.tile.SpaceshipDebrisCollectable;
import org.freemars.util.PlayerStartCoordinateGenerator;
import org.freerealm.diplomacy.Diplomacy;
import org.freerealm.executor.command.AddRequiredPopulationResourceCommand;
import org.freerealm.executor.command.SetPlayerStatusCommand;
import org.freerealm.executor.command.SetTileCollectableCommand;
import org.freerealm.player.Player;
import org.freerealm.property.Property;
import org.freerealm.property.SetStartupUnitCountProperty;

/**
 *
 * @author Deniz ARIKAN
 */
public class NewGameAction extends AbstractAction {

    private final Random rand = new Random(System.currentTimeMillis());
    private final FreeMarsController freeMarsController;
    private Realm realm;

    public NewGameAction(FreeMarsController controller) {
        super("New game");
        this.freeMarsController = controller;
    }

    public void actionPerformed(ActionEvent e) {
        if (!freeMarsController.isMainMenuFrameVisible()) {
            Object[] options = {"Yes, start new game", "No, thanks"};
            int value = JOptionPane.showOptionDialog(freeMarsController.getCurrentFrame(),
                    "Do you want to start a new game game?",
                    "New game",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
            if (value != JOptionPane.YES_OPTION) {
                return;
            }
        }
        realm = freeMarsController.getFreeMarsModel().getRealm();
        freeMarsController.execute(new ResetFreeMarsModelCommand(freeMarsController.getFreeMarsModel()));
        freeMarsController.execute(new InitializeRealmCommand(freeMarsController.getFreeMarsModel().getRealm()));
        freeMarsController.execute(new AddRequiredPopulationResourceCommand(realm, 0, 1));
        freeMarsController.execute(new AddRequiredPopulationResourceCommand(realm, 1, 6));
        freeMarsController.execute(new AddRequiredPopulationResourceCommand(realm, 2, 1));

        EarthFlightModel earthFlightModel = new EarthFlightModel(freeMarsController.getFreeMarsModel().getRealm());
        freeMarsController.getFreeMarsModel().setEarthFlightModel(earthFlightModel);

        if (!freeMarsController.isMainMenuFrameVisible()) {
            freeMarsController.displayMainMenuFrame();
        }
        freeMarsController.hideMainMenuWindow();
        NewGameOptions newGameOptions = freeMarsController.displayNewGameWizard();
        if (newGameOptions.getReturnCode() == NewGameWizard.NEW_GAME_FINISH_RETURN_CODE) {
            if (newGameOptions.getMapType() == NewGameOptions.CUSTOMIZED_MAP) {
                TreeMap<TileType, Integer> tileTypes = newGameOptions.getTileTypes();
                Iterator<TileType> iterator = tileTypes.keySet().iterator();
                while (iterator.hasNext()) {
                    TileType tileType = iterator.next();
                    int probability = tileTypes.get(tileType);
                    tileType.setProbability(probability);
                }
                freeMarsController.execute(new CreateMapCommand(realm, newGameOptions.getMapWidth(), newGameOptions.getMapHeight()));
            } else {
                String premadeMapPath = newGameOptions.getPremadeMapPath();
                InputStream inputStream;
                if (premadeMapPath.startsWith("maps/")) {
                    inputStream = ClassLoader.getSystemResourceAsStream(premadeMapPath);
                } else {
                    try {
                        inputStream = new FileInputStream(new File(premadeMapPath));
                    } catch (Exception exception) {
                        inputStream = null;
                    }
                }
                freeMarsController.execute(new ReadMapFileCommand(realm, inputStream));
            }
            freeMarsController.getFreeMarsModel().setObjectives(newGameOptions.getObjectives());

            Diplomacy diplomacy = new Diplomacy();

            Player firstPlayer = null;
            Iterator<Nation> nationsIterator = newGameOptions.getNationsIterator();
            while (nationsIterator.hasNext()) {
                Nation nation = nationsIterator.next();
                Color primaryColor = newGameOptions.getNationPrimaryColor(nation);
                Color secondaryColor = newGameOptions.getNationSecondaryColor(nation);
                Player player;
                if (nation.equals(newGameOptions.getHumanPlayerNation())) {
                    player = new FreeMarsPlayer(freeMarsController.getFreeMarsModel().getRealm());
                    FirstArrivalToMarsMessage message = new FirstArrivalToMarsMessage();
                    message.setSubject("Welcome to Red planet!");
                    message.setTurnSent(0);
                    message.setText(new StringBuffer("After 6 months of voyage you have arrived to Mars.\nYour shuttle is waiting in orbit."));
                    player.addMessage(message);
                } else {
                    player = new AIPlayer(freeMarsController.getFreeMarsModel().getRealm());
                    ((AIPlayer) player).setDecisionModel(new DecisionModel(freeMarsController, (AIPlayer) player));
                }
                if (firstPlayer == null) {
                    firstPlayer = player;
                }
                player.setId(freeMarsController.getFreeMarsModel().getRealm().getPlayerManager().getNextAvailablePlayerId());
                player.setStatus(Player.STATUS_ACTIVE);
                player.setName(nation.getName());
                player.setNation(nation);
                player.setPrimaryColor(primaryColor);
                player.setSecondaryColor(secondaryColor);
                Iterator<Property> propertiesIterator = nation.getPropertiesIterator();
                while (propertiesIterator.hasNext()) {
                    Property property = propertiesIterator.next();
                    player.addProperty(property);
                }
                addPlayer(player);
                if (nation.equals(newGameOptions.getHumanPlayerNation())) {
                    freeMarsController.getFreeMarsModel().setHumanPlayer(player);
                }
                freeMarsController.execute(new AddPlayerCommand(realm, getRelatedExpeditionaryForcePlayer(freeMarsController.getFreeMarsModel(), player)));
                if (!firstPlayer.equals(player)) {
                    diplomacy.addPlayerRelation(firstPlayer, player, Diplomacy.NO_CONTACT);
                    diplomacy.addPlayerRelation(player, firstPlayer, Diplomacy.NO_CONTACT);
                }
            }
            new MissionReader().readMissions(freeMarsController);
            Logger.getLogger(NewGameAction.class).info("Starting a new game.");
            freeMarsController.executeViewCommand(new ClearPaintModelsCommand(freeMarsController));
            freeMarsController.executeViewCommand(new UpdateExploredAreaPaintModelsCommand(freeMarsController));
            freeMarsController.executeViewCommand(new RepaintMapPanelCommand(freeMarsController));
            freeMarsController.displayGameFrame();
            freeMarsController.execute(new SetActivePlayerCommand(realm, firstPlayer));
        } else {
            freeMarsController.displayMainMenuFrame();
            freeMarsController.displayMainMenuWindow();
        }

    }

    private ExpeditionaryForcePlayer getRelatedExpeditionaryForcePlayer(FreeMarsModel freeMarsModel, Player player) {
        ExpeditionaryForcePlayer expeditionaryForcePlayer = new ExpeditionaryForcePlayer(freeMarsModel.getRealm());
        expeditionaryForcePlayer.setId(freeMarsModel.getRealm().getPlayerManager().getNextAvailablePlayerId());
        expeditionaryForcePlayer.setName(player.getNation().getAdjective() + " expeditionary force");
        expeditionaryForcePlayer.setNation(player.getNation());
        expeditionaryForcePlayer.setPrimaryColor(Color.black);
        expeditionaryForcePlayer.setSecondaryColor(Color.lightGray);
        expeditionaryForcePlayer.setTargetPlayerId(player.getId());
        expeditionaryForcePlayer.setDecisionModel(new ExpeditionaryForceDecisionModel(freeMarsController, expeditionaryForcePlayer));
        freeMarsController.execute(new SetPlayerStatusCommand(expeditionaryForcePlayer, Player.STATUS_PASSIVE));
        UnitType infantryUnitType = freeMarsModel.getRealm().getUnitTypeManager().getUnitType("Infantry");
        for (int i = 0; i < ExpeditionaryForcePlayer.BASE_INFANTRY_COUNT; i++) {
            Unit infantryUnit = new Unit(freeMarsModel.getRealm(), infantryUnitType, null, expeditionaryForcePlayer);
            freeMarsController.execute(new AddUnitCommand(realm, expeditionaryForcePlayer, infantryUnit, Unit.UNIT_SUSPENDED));
        }
        UnitType mechUnitType = freeMarsModel.getRealm().getUnitTypeManager().getUnitType("Mech");
        for (int i = 0; i < ExpeditionaryForcePlayer.BASE_MECH_COUNT; i++) {
            Unit mechUnit = new Unit(freeMarsModel.getRealm(), mechUnitType, null, expeditionaryForcePlayer);
            freeMarsController.execute(new AddUnitCommand(realm, expeditionaryForcePlayer, mechUnit, Unit.UNIT_SUSPENDED));
        }
        UnitType LGTUnitType = freeMarsModel.getRealm().getUnitTypeManager().getUnitType("LGT");
        for (int i = 0; i < ExpeditionaryForcePlayer.BASE_LGT_COUNT; i++) {
            Unit LGTUnit = new Unit(freeMarsModel.getRealm(), LGTUnitType, null, expeditionaryForcePlayer);
            freeMarsController.execute(new AddUnitCommand(realm, expeditionaryForcePlayer, LGTUnit, Unit.UNIT_SUSPENDED));
        }
        int expeditionaryForceFlightTurns = Integer.parseInt(realm.getProperty("expeditionary_force_flight_turns"));
        expeditionaryForcePlayer.setEarthToMarsFlightTurns(expeditionaryForceFlightTurns);
        return expeditionaryForcePlayer;
    }

    private void addPlayer(Player player) {
        Realm realm = freeMarsController.getFreeMarsModel().getRealm();
        freeMarsController.execute(new AddPlayerCommand(realm, player));
        Coordinate coordinate = PlayerStartCoordinateGenerator.generate(freeMarsController);
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
        addLandingModuleDebris(realm, coordinate);
    }

    private void addLandingModuleDebris(Realm realm, Coordinate centerCoordinate) {
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

    private Coordinate getRandomCoordinateForCollectable(Realm realm, List<Coordinate> coordinates) {
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

}
