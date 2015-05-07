package org.freerealm.executor.command;

import org.freerealm.Realm;
import org.freerealm.executor.CommandResult;
import org.freerealm.settlement.Settlement;
import org.freerealm.player.Player;
import org.freerealm.unit.Unit;

/**
 * Command class to make a unit capture a settlement that does not belong to
 * unit's player. When executed InvadeCityCommand will remove the settlement
 * from its player, add it to unit's player and set movement points of capturing
 * unit to 0. The command will not move the unit, it assumes that the invading
 * unit is already moved and is at the coordinate of invaded settlement.
 * <p>
 * InvadeCityCommand will return an error if :
 * <ul>
 * <li>Unit does not have "Move" property</li>
 * <li>Unit does not have "Fight" property</li>
 * <li>Unit is not at the coordinate of invaded settlement</li>
 * </ul>
 *
 * @author Deniz ARIKAN
 */
public class CaptureSettlementCommand extends AbstractCommand {

    private Unit unit;
    private Settlement settlement;

    /**
     * Constructs an InvadeCityCommand using unit, settlement.
     *
     * @param unit Capturing unit
     * @param settlement Settlement that is invaded by unit
     */
    public CaptureSettlementCommand(Realm realm, Unit unit, Settlement settlement) {
        super(realm);
        this.unit = unit;
        this.settlement = settlement;
    }

    /**
     * Executes command and adds captured settlement to unit's player.
     *
     * @return CommandResult
     */
    public CommandResult execute() {
        Player defendingPlayer = settlement.getPlayer();
        settlement.setPlayer(unit.getPlayer());
        MoveUnitCommand moveUnitCommand = new MoveUnitCommand(getRealm(), unit, settlement.getCoordinate());
        CommandResult result = getExecutor().execute(moveUnitCommand);
        if (result.getCode() == CommandResult.RESULT_OK) {
            defendingPlayer.removeSettlement(settlement);
            unit.getPlayer().addSettlement(settlement);
            getExecutor().execute(new UnitSetMovementPointsCommand(unit, 0));
            CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.SETTLEMENT_CAPTURED_UPDATE);
            commandResult.putParameter("settlement", settlement);
            commandResult.putParameter("newOwner", unit.getPlayer());
            commandResult.putParameter("previousOwner", defendingPlayer);
            return commandResult;
        } else {
            settlement.setPlayer(defendingPlayer);
            return result;
        }

    }
}
