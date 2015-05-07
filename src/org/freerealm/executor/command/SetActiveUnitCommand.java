package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.player.Player;
import org.freerealm.unit.Unit;

/**
 * Command class to set active unit of a player, value of new unit can be null
 * to indicate none of the units of player are currently active. Passing a null
 * player into the command will result in a runtime exception. If unit does not
 * belong to given player command will return an error.
 *
 * @author Deniz ARIKAN
 */
public class SetActiveUnitCommand extends AbstractCommand {

    private Player player;
    private Unit unit;

    /**
     * Constructs a SetActiveUnitCommand using player, unit.
     *
     * @param player Player to set active unit
     * @param unit Unit to set active, can be null
     */
    public SetActiveUnitCommand(Player player, Unit unit) {
        this.player = player;
        this.unit = unit;
    }

    /**
     * Executes command to activate given unit for player.
     *
     * @param realm Realm to execute the command
     * @return CommandResult
     */
    public CommandResult execute() {
        if ((unit != null) && (!player.hasUnit(unit))) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Unit does not belong to active user");
        }
        CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.ACTIVE_UNIT_UPDATE);
        commandResult.putParameter("previous_active_unit", player.getActiveUnit());
        player.setActiveUnit(unit);
        commandResult.putParameter("active_unit", unit);
        return commandResult;
    }
}
