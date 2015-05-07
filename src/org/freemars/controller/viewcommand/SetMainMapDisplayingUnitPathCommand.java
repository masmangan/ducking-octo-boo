package org.freemars.controller.viewcommand;

import org.freemars.controller.FreeMarsController;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.AbstractCommand;

/**
 *
 * @author Deniz ARIKAN
 */
public class SetMainMapDisplayingUnitPathCommand extends AbstractCommand implements ViewCommand {

    private final FreeMarsController freeMarsController;
    private final boolean displayUnitPath;

    public SetMainMapDisplayingUnitPathCommand(FreeMarsController freeMarsController, boolean displayUnitPath) {
        this.freeMarsController = freeMarsController;
        this.displayUnitPath = displayUnitPath;
    }

    @Override
    public String toString() {
        return "SetMainMapDisplayingUnitPath";
    }

    public CommandResult execute() {
        freeMarsController.getFreeMarsModel().getFreeMarsViewModel().setMapPanelDisplayingUnitPath(displayUnitPath);
        freeMarsController.updateGameFrame();
        return new CommandResult(CommandResult.RESULT_OK, "");
    }

    public boolean isBlockingViewExecutionThread() {
        return false;
    }

}
