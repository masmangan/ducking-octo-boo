package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import org.freerealm.player.Player;
import org.freerealm.unit.Unit;

/**
 * Command class to remove a player's unit. If removed unit was the player's
 * active unit this command will not find and activate player's next unit, it
 * will just set the active unit of player to null. If needed, the command
 * caller must to make next available unit active.
 * <p>
 * Passing a null player into the command will result in a runtime exception.
 * If unit does not belong to given player command will return an error.
 * @author Deniz ARIKAN
 */
public class RemoveUnitFromPlayerCommand extends AbstractCommand {

    private Player player;
    private Unit unit;

    /**
     * Constructs a RemoveUnitCommand using player, unit.
     * @param player Player to remove unit from
     * @param unit Unit to remove from player and realm's world map
     */
    public RemoveUnitFromPlayerCommand(Player player, Unit unit) {
        this.player = player;
        this.unit = unit;
    }

    /**
     * Executes command and removes given unit.
     * @param realm Realm to execute the command
     * @return CommandResult
     */
    public CommandResult execute() {
        if ((unit != null) && (!player.hasUnit(unit))) {
            return new CommandResult(CommandResult.RESULT_ERROR, "Unit does not belong to active user", CommandResult.NO_UPDATE);
        }
        if (player.getActiveUnit() != null && player.getActiveUnit().equals(unit)) {
            player.setActiveUnit(null);
        }
        player.removeUnit(unit);
        return new CommandResult(CommandResult.RESULT_OK, "");
    }
}
