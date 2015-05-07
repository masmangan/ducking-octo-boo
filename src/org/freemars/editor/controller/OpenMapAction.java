package org.freemars.editor.controller;

import org.freerealm.executor.command.ReadMapFileCommand;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.freemars.ui.util.FRMFileChooser;

/**
 *
 * @author Deniz ARIKAN
 */
public class OpenMapAction extends AbstractAction {

    private EditorController controller;

    public OpenMapAction(EditorController editorController) {
        super("Open map", null);
        this.controller = editorController;
    }

    public void actionPerformed(ActionEvent e) {
        if (controller.getEditorModel().isFileDirty()) {
            int result = JOptionPane.showConfirmDialog(controller.getEditorFrame(), "Current map is not saved. Do you wish to save it?", "Save current map", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                controller.getAction(EditorController.SAVE_MAP_AS).actionPerformed(e);
            }
        }
        FRMFileChooser frmFileChooser = new FRMFileChooser();
        if (frmFileChooser.showOpenDialog(controller.getEditorFrame()) == JFileChooser.APPROVE_OPTION) {
            File file = frmFileChooser.getSelectedFile();
            try {
                controller.getEditorModel().execute(new ReadMapFileCommand(controller.getEditorModel().getRealm(), new FileInputStream(file)));
                controller.getEditorModel().setFileName(file.getAbsolutePath());
                controller.getEditorModel().setFileDirty(false);
                controller.getEditorModel().refresh();
            } catch (Exception exc) {
            }
        }
    }
}
