package org.freemars.editor.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 *
 * @author Deniz ARIKAN
 */
public class ExitToSystemAction extends AbstractAction {

    private EditorController controller;

    public ExitToSystemAction(EditorController controller) {
        super("Exit editor", null);
        putValue(SHORT_DESCRIPTION, "Exit editor");
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        Object[] options = {"Yes, exit", "No, thanks"};
        int value = JOptionPane.showOptionDialog(controller.getEditorFrame(),
                "Really quit editor?",
                "Quit game",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);
        if (value == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
