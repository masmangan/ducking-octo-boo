package org.freemars.editor.controller;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import org.freemars.ui.util.FreeMarsOptionPane;
import org.freerealm.map.FreeRealmMap;
import org.freemars.ui.util.FRMFileChooser;
import org.freerealm.map.Map;
import org.freerealm.xmlwrapper.map.FreeRealmMapXMLConverter;

/**
 *
 * @author Deniz ARIKAN
 */
public class SaveMapAsAction extends AbstractAction {

    private EditorController editorController;

    public SaveMapAsAction(EditorController editorController) {
        super("Save map as", null);
        this.editorController = editorController;
    }

    public void actionPerformed(ActionEvent e) {
        FRMFileChooser frmFileChooser = new FRMFileChooser();
        if (editorController.getEditorModel().getFileName() != null) {
            frmFileChooser.setSelectedFile(new File(editorController.getEditorModel().getFileName()));
        }
        if (frmFileChooser.showSaveDialog(editorController.getEditorFrame()) == JFileChooser.APPROVE_OPTION) {
            Map map = editorController.getEditorModel().getRealm().getMap();
            StringBuffer fileData = new StringBuffer();
            fileData.append("<FreeMarsMap>\n");
            fileData.append((new FreeRealmMapXMLConverter()).toXML((FreeRealmMap) map));
            fileData.append("</FreeMarsMap>");
            try {
                String saveFileName = frmFileChooser.getSelectedFile().getAbsolutePath();
                if (!saveFileName.endsWith(".frm")) {
                    saveFileName = saveFileName + ".frm";
                }
                FileWriter fileWriter = new FileWriter(new File(saveFileName));
                fileWriter.write(fileData.toString());
                fileWriter.close();
                editorController.getEditorModel().setFileName(saveFileName);
                editorController.getEditorModel().setFileDirty(false);
                editorController.getEditorModel().refresh();
                FreeMarsOptionPane.showMessageDialog(editorController.getEditorFrame(), "Save complete");
            } catch (Exception exception) {
            }
        }
    }
}
