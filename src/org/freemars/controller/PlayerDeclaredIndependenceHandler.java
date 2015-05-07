package org.freemars.controller;

import org.freemars.earth.action.DisplayIndependenceDeclaredDialogAction;
import org.freerealm.executor.CommandResult;

/**
 *
 * @author Deniz ARIKAN
 */
public class PlayerDeclaredIndependenceHandler implements ControllerUpdateHandler {

    public void handleUpdate(FreeMarsController controller, CommandResult commandResult) {
        new DisplayIndependenceDeclaredDialogAction(controller).actionPerformed(null);
    }
}
