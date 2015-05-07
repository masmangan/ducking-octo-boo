package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.player.Player;
import org.freerealm.player.mission.Mission;

/**
 *
 * @author Deniz ARIKAN
 */
public class AssignMissionToPlayerCommand extends AbstractCommand {

    private Player player;
    private Mission mission;

    /**
     * Constructs an AddMissionToPlayerCommand using player and mission.
     *
     * @param player New player to add to realm
     * @param mission Mission to assign to player
     */
    public AssignMissionToPlayerCommand(Player player, Mission mission) {
        this.player = player;
        this.mission = mission;
    }

    /**
     * Executes command and adds the mission to player.
     *
     * @return CommandResult
     */
    public CommandResult execute() {
        mission.setPlayer(player);
        player.addMission(mission);
        CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.MISSION_ASSIGNED_UPDATE);
        commandResult.putParameter("player", player);
        commandResult.putParameter("mission", mission);
        return commandResult;
    }
}
