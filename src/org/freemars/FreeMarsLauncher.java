package org.freemars;

import javax.swing.JPanel;
import org.apache.log4j.Logger;
import org.freerealm.xmlwrapper.TagManager;
import org.freemars.controller.FreeMarsController;

/**
 *
 * @author Deniz ARIKAN
 */
public class FreeMarsLauncher extends JPanel {

    public static void main(final String[] args) {
        initialize();
        final FreeMarsController freeMarsController = new FreeMarsController();
        freeMarsController.initGameFrame();
        freeMarsController.startViewCommandExecutionThread();
        TagManager.setObjectInPool("freeMarsController", freeMarsController);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Logger.getLogger(FreeMarsLauncher.class).info("Displaying main menu.");
                freeMarsController.displayMainMenuFrame();
                freeMarsController.displayMainMenuWindow();
            }
        });
    }

    private static void initialize() {
        FreeMarsInitializer.initializeLogger();
        FreeMarsInitializer.initializeGameFolders();
        FreeMarsInitializer.initializeUI();
        FreeMarsInitializer.initializeTags();
        Logger.getLogger(FreeMarsLauncher.class).info("Free Mars game initialized successfully.");
    }

}
