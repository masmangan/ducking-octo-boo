package org.freemars.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import org.freemars.ai.AIPlayer;
import org.freemars.earth.command.SetEarthTaxRateCommand;
import org.freemars.mission.WealthReward;
import org.freemars.model.FreeMarsModel;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.SignalPlayerEndTurnCommand;
import org.freerealm.executor.command.WealthAddCommand;
import org.freerealm.player.Player;
import org.freerealm.player.mission.Mission;
import org.freerealm.player.mission.Reward;

/**
 *
 * @author Deniz ARIKAN
 */
public class FreeMarsAIPlayerActivatedHandler extends ActivePlayerHandler {

    private static final Logger logger = Logger.getLogger(FreeMarsAIPlayerActivatedHandler.class);
    private final AIPlayer freeMarsAIPlayer;

    public FreeMarsAIPlayerActivatedHandler(AIPlayer freeMarsAIPlayer) {
        this.freeMarsAIPlayer = freeMarsAIPlayer;
    }

    @Override
    public void handleUpdate(final FreeMarsController freeMarsController, CommandResult commandResult) {
        if (freeMarsAIPlayer.getStatus() == Player.STATUS_ACTIVE) {
            if (freeMarsController.getFreeMarsModel().getMode() == FreeMarsModel.SIMULATION_MODE) {
                play(freeMarsController);
            } else {
                SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        play(freeMarsController);
                        return null;
                    }
                };
                mySwingWorker.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (evt.getPropertyName().equals("state")) {
                            if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
                                AISplashDisplayer.hide();
                            }
                        }
                    }
                });
                mySwingWorker.execute();
                AISplashDisplayer.display(freeMarsController, freeMarsAIPlayer);
            }
        } else {
            String logInfo = "Status of AI player with id ";
            logInfo = logInfo + freeMarsAIPlayer.getId() + " and name \"" + freeMarsAIPlayer.getName() + "\" is ";
            if (freeMarsAIPlayer.getStatus() == Player.STATUS_PASSIVE) {
                logInfo = logInfo + "\"passive\". ";
            } else {
                logInfo = logInfo + "\"unknown(" + freeMarsAIPlayer.getStatus() + ")\". ";
            }
            logInfo = logInfo + "Auto skipping turn.";
            logger.info(logInfo);
        }
        freeMarsController.execute(new SignalPlayerEndTurnCommand(freeMarsAIPlayer));
    }

    private void play(FreeMarsController freeMarsController) {
        AISplashDisplayer.setCurrentProcessLabelText("Automated units...");
        manageAutomatedUnits(freeMarsAIPlayer);
        AISplashDisplayer.setCurrentProcessLabelText("Checking mission assignments...");
        if (!freeMarsAIPlayer.hasDeclaredIndependence()) {
            freeMarsController.assignMissions(freeMarsAIPlayer);
            if (freeMarsController.getFreeMarsModel().getNumberOfTurns() > 24 && freeMarsController.getFreeMarsModel().getNumberOfTurns() % 24 == 0) {
                byte currentEarthTaxRate = freeMarsAIPlayer.getEarthTaxRate();
                byte newEarthTaxRate = new EarthTaxRateCalculator(freeMarsController.getFreeMarsModel(), freeMarsAIPlayer).getTaxRate();
                if (currentEarthTaxRate != newEarthTaxRate) {
                    freeMarsController.execute(new SetEarthTaxRateCommand(freeMarsAIPlayer, newEarthTaxRate));
                }
            }
        }
        AISplashDisplayer.setCurrentProcessLabelText("Checking completed missions...");
        Iterator<Mission> missionsIterator = freeMarsAIPlayer.getMissionsIterator();
        while (missionsIterator.hasNext()) {
            Mission mission = missionsIterator.next();
            if (mission.getStatus() == Mission.STATUS_ACTIVE) {
                mission.checkStatus();
                if (mission.getStatus() == Mission.STATUS_COMPLETED) {
                    Iterator<Reward> rewardIterator = mission.getRewardsIterator();
                    while (rewardIterator.hasNext()) {
                        Reward reward = rewardIterator.next();
                        if (reward instanceof WealthReward) {
                            WealthReward wealthReward = (WealthReward) reward;
                            freeMarsController.execute(new WealthAddCommand(freeMarsAIPlayer, wealthReward.getAmount()));
                        }
                    }
                }
            }
        }
        AISplashDisplayer.setCurrentProcessLabelText("Playing...");
        freeMarsAIPlayer.play();
        AISplashDisplayer.setCurrentProcessLabelText("Completed");
    }

}
