package org.freemars.controller.action.file;

import org.freemars.controller.FreeMarsController;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.freemars.command.ResetFreeMarsModelCommand;

/**
 *
 * @author Deniz ARIKAN
 */
public class QuitToMainMenuAction extends AbstractAction {

    private FreeMarsController controller;

    public QuitToMainMenuAction(FreeMarsController controller) {
        super("Quit to main menu");
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        Object[] options = {"Yes", "No, thanks"};
        int value = JOptionPane.showOptionDialog(controller.getCurrentFrame(),
                "Quit current game and return to main menu?",
                "Quit game",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);
        if (value == JOptionPane.YES_OPTION) {
            controller.execute(new ResetFreeMarsModelCommand(controller.getFreeMarsModel()));
            controller.displayMainMenuFrame();
            controller.displayMainMenuWindow();
        }
    }
}
