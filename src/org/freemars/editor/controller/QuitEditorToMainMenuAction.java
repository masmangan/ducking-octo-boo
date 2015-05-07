package org.freemars.editor.controller;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 *
 * @author Deniz ARIKAN
 */
public class QuitEditorToMainMenuAction extends AbstractAction {

    private EditorController controller;

    public QuitEditorToMainMenuAction(EditorController controller) {
        super("Quit to main menu", null);
        putValue(SHORT_DESCRIPTION, "Quit to main menu");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_Q));
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        Object[] options = {"Yes", "No, thanks"};
        int value = JOptionPane.showOptionDialog(controller.getEditorFrame(),
                "Quit editor and return to main menu?",
                "Quit editor",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);
        if (value == JOptionPane.YES_OPTION) {
            controller.getEditorFrame().setVisible(false);
            controller.getQuitEditorAction().actionPerformed(e);
        }
    }
}
