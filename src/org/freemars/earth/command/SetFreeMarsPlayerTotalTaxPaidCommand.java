package org.freemars.earth.command;

import org.freemars.player.FreeMarsPlayer;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.AbstractCommand;

/**
 *
 * @author Deniz ARIKAN
 */
public class SetFreeMarsPlayerTotalTaxPaidCommand extends AbstractCommand {

    private FreeMarsPlayer freeMarsPlayer;
    private int totalTaxPaid;

    public SetFreeMarsPlayerTotalTaxPaidCommand(FreeMarsPlayer freeMarsPlayer, int totalTaxPaid) {
        this.freeMarsPlayer = freeMarsPlayer;
        this.totalTaxPaid = totalTaxPaid;
    }

    public CommandResult execute() {
        freeMarsPlayer.setTotalTaxPaid(totalTaxPaid);
        CommandResult commandResult = new CommandResult(CommandResult.RESULT_OK, "");
        return commandResult;
    }
}
