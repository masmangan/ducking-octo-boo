package org.freemars.controller.viewcommand;

import org.freemars.controller.FreeMarsController;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.AbstractCommand;
import org.freerealm.map.Path;

/**
 *
 * @author Deniz ARIKAN
 */
public class SetMainMapUnitPathCommand extends AbstractCommand implements ViewCommand {

    private final FreeMarsController freeMarsController;
    private final Path path;

    public SetMainMapUnitPathCommand(FreeMarsController freeMarsController, Path path) {
        this.freeMarsController = freeMarsController;
        this.path = path;
    }

    @Override
    public String toString() {
        return "SetMainMapUnitPath";
    }

    public CommandResult execute() {
        freeMarsController.getFreeMarsModel().getFreeMarsViewModel().setMapPanelUnitPath(path);
        freeMarsController.updateGameFrame();
        return new CommandResult(CommandResult.RESULT_OK, "");
    }

    public boolean isBlockingViewExecutionThread() {
        return false;
    }

}
