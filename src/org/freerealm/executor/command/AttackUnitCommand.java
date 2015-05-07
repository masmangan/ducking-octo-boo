package org.freerealm.executor.command;

import org.freerealm.executor.CommandResult;
import java.util.Random;
import org.apache.log4j.Logger;
import org.freerealm.Realm;

import org.freerealm.Utility;
import org.freerealm.unit.Unit;

/**
 * Command class to execute an attack to a hostile unit by the given attacking
 * unit.
 * <p>
 * TODO : More explanation when the command is finalized.
 *
 * @author Deniz ARIKAN
 */
public class AttackUnitCommand extends AbstractCommand {

    private Unit attacker;
    private Unit defender;
    private int attackBonusPercentage = 0;
    private int defenceBonusPercentage = 0;

    /**
     * Constructs an AttackUnitCommand using attacker, defender.
     *
     * @param attacker Unit making the attack
     * @param defender Defending unit
     */
    public AttackUnitCommand(Realm realm, Unit attacker, Unit defender) {
        super(realm);
        this.attacker = attacker;
        this.defender = defender;
    }

    /**
     * Executes command to make unit attack given defending unit.
     *
     * @return CommandResult
     */
    public CommandResult execute() {
        CommandResult commandResult = null;
        if (attacker.canAttack()) {
            Unit winner = null;
            Unit loser = null;
            int attackPoints = attacker.getAttackPoints();
            int defencePoints = defender.getDefencePoints();
            String log = attacker.getPlayer().getNation().getAdjective() + " " + attacker.getName() + " is attacking " + defender.getPlayer().getNation().getAdjective() + " " + defender.getName() + ".";
            Logger.getLogger(AttackUnitCommand.class).info(log);
            Logger.getLogger(AttackUnitCommand.class).info("Attack points : " + attackPoints + " Defense points : " + defencePoints);
            attackPoints = (int) Utility.modifyByPercent(attackPoints, attackBonusPercentage);
            defenceBonusPercentage = Utility.getCoordinateDefenceBonus(getRealm(), defender.getCoordinate());
            defencePoints = (int) Utility.modifyByPercent(defencePoints, defenceBonusPercentage);
            Logger.getLogger(AttackUnitCommand.class).info("Actual attack points : " + attackPoints + " Actual defense points : " + defencePoints);
            int total = attackPoints + defencePoints;
            Random random = new Random(System.currentTimeMillis());
            int randomInt = random.nextInt(total);
            log = "Total : " + total + " Random : " + randomInt;
            if (randomInt < attackPoints) {
                loser = defender;
                winner = attacker;
                log = log + " Attacker won!";
            } else {
                loser = attacker;
                winner = defender;
                log = log + " Defender won!";
            }
            Logger.getLogger(AttackUnitCommand.class).info(log);
            getExecutor().execute(new UnitSetMovementPointsCommand(winner, 0));
            getExecutor().execute(new RemoveUnitCommand(getRealm(), loser.getPlayer(), loser));
            commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.UNIT_ATTACKED_UPDATE);
            commandResult.putParameter("attacker", attacker);
            commandResult.putParameter("defender", defender);
            commandResult.putParameter("winner", winner);
            commandResult.putParameter("coordinate", defender.getCoordinate());
        } else {
            commandResult = new CommandResult(CommandResult.RESULT_OK, "", CommandResult.NO_UPDATE);
        }
        return commandResult;
    }
}
