package org.freerealm.executor.command;

import org.freerealm.Realm;
import org.freerealm.executor.CommandResult;
import org.freerealm.player.Player;
import org.freerealm.unit.Unit;

/**
 * Command class to remove a player's unit. RemoveUnitCommand will also remove
 * the given unit from realm's world map. If removed unit was the player's
 * active unit this command will not find and activate player's next unit, it
 * will just set the active unit of player to null. If needed, the command
 * caller must to make next available unit active.
 * <p>
 * Passing a null player into the command will result in a runtime exception. If
 * unit does not belong to given player command will return an error.
 *
 * @author Deniz ARIKAN
 */
public class RemoveUnitCommand extends AbstractCommand {

    private final Player player;
    private final Unit unit;

    /**
     * Constructs a RemoveUnitCommand using player, unit.
     *
     * @param player Player to remove unit from
     * @param unit Unit to remove from player and realm's world map
     */
    public RemoveUnitCommand(Realm realm, Player player, Unit unit) {
        super(realm);
        this.player = player;
        this.unit = unit;
    }

    /**
     * Executes command and removes given unit.
     *
     * @return CommandResult
     */
    public CommandResult execute() {
        CommandResult removeUnitFromPlayerCommandResult = getExecutor().execute(new RemoveUnitFromPlayerCommand(player, unit));
        if (removeUnitFromPlayerCommandResult.getCode() != CommandResult.RESULT_OK) {
            return removeUnitFromPlayerCommandResult;
        }
        if (unit.getStatus() == Unit.UNIT_ACTIVE) {
            getExecutor().execute(new RemoveUnitFromMapCommand(getRealm(), unit));
        }
        CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.UNIT_REMOVED_UPDATE);
        commandResult.putParameter("unit", unit);
        return commandResult;
    }
}
