package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.player.Player;
import org.freerealm.player.mission.Mission;

/**
 *
 * @author Deniz ARIKAN
 */
public class RemoveMissionFromPlayerCommand extends AbstractCommand {

    private Player player;
    private Mission mission;

    /**
     * Constructs a RemoveMissionFromPlayerCommand using player and mission.
     * @param player Player to remove mission
     * @param mission Mission to remove from player
     */
    public RemoveMissionFromPlayerCommand(Player player, Mission mission) {
        this.player = player;
        this.mission = mission;
    }

    /**
     * Executes command and removes the mission from player.
     * @param realm Realm to execute the command
     * @return CommandResult
     */
    public CommandResult execute() {
        player.removeMission(mission);
        CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.MISSION_REMOVED_UPDATE);
        commandResult.putParameter("player", player);
        commandResult.putParameter("mission", mission);
        return commandResult;
    }
}
