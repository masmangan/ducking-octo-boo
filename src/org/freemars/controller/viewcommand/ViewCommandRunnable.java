package org.freemars.controller.viewcommand;

import org.freerealm.executor.Command;

/**
 *
 * @author Deniz ARIKAN
 */
public class ViewCommandRunnable implements Runnable {

    private Command command;

    public ViewCommandRunnable(Command command) {
        this.command = command;
    }

    public void run() {
        command.execute();
    }
}
