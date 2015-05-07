package org.freemars.earth.command;

import java.util.Iterator;
import org.freemars.ai.ExpeditionaryForcePlayer;
import org.freemars.controller.FreeMarsController;
import org.freemars.model.FreeMarsModel;
import org.freemars.player.FreeMarsPlayer;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.AbstractCommand;
import org.freerealm.executor.command.SetPlayerStatusCommand;
import org.freerealm.player.Player;
import org.freerealm.player.mission.Mission;

/**
 * @author Deniz ARIKAN
 */
public class DeclareIndependenceCommand extends AbstractCommand {

    private final FreeMarsController freeMarsController;
    private final FreeMarsPlayer freeMarsPlayer;

    public DeclareIndependenceCommand(FreeMarsController freeMarsController, FreeMarsPlayer freeMarsPlayer) {
        this.freeMarsController = freeMarsController;
        this.freeMarsPlayer = freeMarsPlayer;
    }

    public CommandResult execute() {
        freeMarsPlayer.setDeclaredIndependence(true);
        freeMarsPlayer.setIndependenceTurn(freeMarsController.getFreeMarsModel().getNumberOfTurns());
        ExpeditionaryForcePlayer expeditionaryForcePlayer = freeMarsController.getFreeMarsModel().getRelatedExpeditionaryForcePlayer(freeMarsPlayer);
        getExecutor().execute(new SetPlayerStatusCommand(expeditionaryForcePlayer, Player.STATUS_ACTIVE));
        Iterator<Mission> iterator = freeMarsPlayer.getMissionsIterator();
        while (iterator.hasNext()) {
            Mission mission = iterator.next();
            mission.setStatus(Mission.STATUS_CANCELED);
        }
        CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "", FreeMarsModel.PLAYER_DECLARED_INDEPENDENCE_UPDATE);
        commandResult.putParameter("player", freeMarsPlayer);
        return commandResult;
    }
}
