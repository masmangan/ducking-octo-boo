package org.freemars.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.freemars.colony.AddFertilizerToColonyTilesCommand;
import org.freemars.colony.FreeMarsColony;
import org.freemars.colony.RemoveFertilizerFromColonyTilesCommand;
import org.freemars.earth.command.SeizeSpaceshipsCommand;
import org.freemars.earth.command.SeizeUnitsCommand;
import org.freemars.message.NotEnoughFertilizerMessage;
import org.freemars.player.FreeMarsPlayer;
import org.freerealm.Realm;
import org.freerealm.executor.Command;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.DefaultRandomEventCommand;
import org.freerealm.executor.command.RemovePlayerCommand;
import org.freerealm.executor.command.SettlementAutomanageResourceCommand;
import org.freerealm.executor.command.StartNewTurnCommand;
import org.freerealm.player.Player;
import org.freerealm.random.RandomEvent;
import org.freerealm.resource.Resource;
import org.freerealm.settlement.Settlement;
import org.freerealm.unit.Unit;

/**
 *
 * @author Deniz ARIKAN
 */
public class TurnEndedHandler {

    public Command handleUpdate(FreeMarsController controller, CommandResult commandResult) {
        handleDefeatedPlayers(controller);
        handleFertilizerUsage(controller);
        handleRandomEvents(controller);
        handleSeizedSpaceships(controller);
        handleSeizedUnits(controller);
        if (controller.getFreeMarsModel().getNumberOfTurns() >= controller.getFreeMarsModel().getMaximumNumberOfTurns()) {
            return null;
        }
        return new StartNewTurnCommand(controller.getFreeMarsModel().getRealm());
    }

    private void handleDefeatedPlayers(FreeMarsController controller) {
        List<Player> defeatedPlayers = FreeMarsPlayerUtilities.getDefeatedPlayers(controller.getFreeMarsModel());
        for (Player player : defeatedPlayers) {
            if (!player.equals(controller.getFreeMarsModel().getHumanPlayer())) {
                List<Unit> unitsToRemoveFromEarthFlightModel = new ArrayList<Unit>();
                Iterator<Unit> iterator = controller.getFreeMarsModel().getEarthFlightModel().getUnitsIterator();
                while (iterator.hasNext()) {
                    Unit unit = iterator.next();
                    if (unit.getPlayer().equals(player)) {
                        unitsToRemoveFromEarthFlightModel.add(unit);
                    }
                }
                for (Unit unitToRemove : unitsToRemoveFromEarthFlightModel) {
                    controller.getFreeMarsModel().getEarthFlightModel().removeUnitLocation(unitToRemove);
                }
                controller.execute(new RemovePlayerCommand(controller.getFreeMarsModel().getRealm(), player));
            } else {
                controller.getFreeMarsModel().setHumanPlayerDefeated(true);
            }
        }
    }

    private void handleFertilizerUsage(FreeMarsController freeMarsController) {
        Iterator<Player> playersIterator = freeMarsController.getFreeMarsModel().getPlayersIterator();
        while (playersIterator.hasNext()) {
            Player player = playersIterator.next();
            Iterator<Settlement> settlementsIterator = player.getSettlementsIterator();
            while (settlementsIterator.hasNext()) {
                Settlement settlement = settlementsIterator.next();
                FreeMarsColony freeMarsColony = (FreeMarsColony) settlement;
                Realm realm = freeMarsController.getFreeMarsModel().getRealm();
                freeMarsController.execute(new RemoveFertilizerFromColonyTilesCommand(realm, freeMarsColony, false));
                if (freeMarsColony.isAutoUsingFertilizer()) {
                    CommandResult addFertilizerCommandResult = freeMarsController.execute(new AddFertilizerToColonyTilesCommand(realm, freeMarsColony));
                    if (addFertilizerCommandResult.getCode() == CommandResult.RESULT_ERROR) {
                        freeMarsColony.setAutoUsingFertilizer(false);
                        NotEnoughFertilizerMessage notEnoughFertilizerMessage = new NotEnoughFertilizerMessage();
                        notEnoughFertilizerMessage.setTurnSent(freeMarsController.getFreeMarsModel().getNumberOfTurns());
                        notEnoughFertilizerMessage.setSubject("No fertilizer");
                        notEnoughFertilizerMessage.setText("No fertilizer left in " + freeMarsColony.getName() + ". Auto using fertilizer is turned off.");
                        notEnoughFertilizerMessage.setFreeMarsColony(freeMarsColony);
                        Resource resource = freeMarsController.getFreeMarsModel().getRealm().getResourceManager().getResource("Fertilizer");
                        notEnoughFertilizerMessage.setFertilizerResource(resource);
                        freeMarsColony.getPlayer().addMessage(notEnoughFertilizerMessage);
                    }
                }
                Resource foodResource = freeMarsController.getFreeMarsModel().getRealm().getResourceManager().getResource(Resource.FOOD);
                if (freeMarsColony.isAutomanagingResource(foodResource)) {
                    freeMarsController.execute(new SettlementAutomanageResourceCommand(realm, freeMarsColony, foodResource));
                }
            }
        }
    }

    private void handleRandomEvents(FreeMarsController controller) {
        Iterator<Player> iterator = controller.getFreeMarsModel().getPlayersIterator();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            FreeMarsPlayer freeMarsPlayer = (FreeMarsPlayer) player;
            if (freeMarsPlayer.getStatus() == Player.STATUS_ACTIVE) {
                ArrayList<RandomEvent> randomEvents = controller.getFreeMarsModel().getRealm().getRandomEventGenerator().generateRandomEvents();
                if (randomEvents.size() > 0) {
                    for (RandomEvent randomEvent : randomEvents) {
                        controller.execute(getRandomEventCommand(controller, randomEvent, player));
                    }
                }
            }
        }
    }

    private DefaultRandomEventCommand getRandomEventCommand(FreeMarsController freeMarsController, RandomEvent randomEvent, Player player) {
        try {
            DefaultRandomEventCommand randomEventCommand = (DefaultRandomEventCommand) Class.forName(randomEvent.getCommand()).newInstance();
            randomEventCommand.setRealm(freeMarsController.getFreeMarsModel().getRealm());
            randomEventCommand.setRandomEvent(randomEvent);
            randomEventCommand.setPlayer(player);
            return randomEventCommand;
        } catch (Exception ex) {
            return null;
        }
    }

    private void handleSeizedSpaceships(FreeMarsController controller) {
        String seizeSpaceshipsAfterIndependenceTurnsProperty = controller.getFreeMarsModel().getRealm().getProperty("seize_spaceships_after_independence_turns");
        int seizeSpaceshipsAfterIndependenceTurns = 3;
        if (seizeSpaceshipsAfterIndependenceTurnsProperty != null) {
            seizeSpaceshipsAfterIndependenceTurns = Integer.parseInt(seizeSpaceshipsAfterIndependenceTurnsProperty);
        }
        Iterator<Player> iterator = controller.getFreeMarsModel().getPlayersIterator();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            FreeMarsPlayer freeMarsPlayer = (FreeMarsPlayer) player;
            if (freeMarsPlayer.hasDeclaredIndependence()) {
                int independenceTurn = freeMarsPlayer.getIndependenceTurn();
                int difference = controller.getFreeMarsModel().getNumberOfTurns() - independenceTurn;
                if (difference == seizeSpaceshipsAfterIndependenceTurns) {
                    SeizeSpaceshipsCommand seizeSpaceshipsCommand = new SeizeSpaceshipsCommand(controller, freeMarsPlayer);
                    controller.execute(seizeSpaceshipsCommand);
                }
            }
        }
    }

    private void handleSeizedUnits(FreeMarsController controller) {
        String seizeUnitsAfterIndependenceTurnsProperty = controller.getFreeMarsModel().getRealm().getProperty("seize_units_after_independence_turns");
        int seizeUnitsAfterIndependenceTurns = 3;
        if (seizeUnitsAfterIndependenceTurnsProperty != null) {
            seizeUnitsAfterIndependenceTurns = Integer.parseInt(seizeUnitsAfterIndependenceTurnsProperty);
        }
        Iterator<Player> iterator = controller.getFreeMarsModel().getPlayersIterator();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            FreeMarsPlayer freeMarsPlayer = (FreeMarsPlayer) player;
            if (freeMarsPlayer.hasDeclaredIndependence()) {
                int independenceTurn = freeMarsPlayer.getIndependenceTurn();
                int difference = controller.getFreeMarsModel().getNumberOfTurns() - independenceTurn;
                if (difference == seizeUnitsAfterIndependenceTurns) {
                    SeizeUnitsCommand seizeUnitsCommand = new SeizeUnitsCommand(controller, freeMarsPlayer);
                    controller.execute(seizeUnitsCommand);
                }
            }
        }
    }
}
