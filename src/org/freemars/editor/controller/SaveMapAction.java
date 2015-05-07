package org.freemars.editor.controller;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import javax.swing.AbstractAction;
import org.freemars.ui.util.FreeMarsOptionPane;
import org.freerealm.map.FreeRealmMap;
import org.freerealm.map.Map;
import org.freerealm.xmlwrapper.map.FreeRealmMapXMLConverter;

/**
 *
 * @author Deniz ARIKAN
 */
public class SaveMapAction extends AbstractAction {

    private EditorController editorController;

    public SaveMapAction(EditorController editorController) {
        super("Save map", null);
        this.editorController = editorController;
    }

    public void actionPerformed(ActionEvent e) {
        String fileName = editorController.getEditorModel().getFileName();
        if (fileName != null) {
            try {
                Map map = editorController.getEditorModel().getRealm().getMap();
                StringBuffer fileData = new StringBuffer();
                fileData.append("<FreeMarsMap>\n");
                fileData.append((new FreeRealmMapXMLConverter()).toXML((FreeRealmMap) map));
                fileData.append("</FreeMarsMap>");
                FileWriter fileWriter = new FileWriter(new File(fileName));
                fileWriter.write(fileData.toString());
                fileWriter.close();
                editorController.getEditorModel().setFileDirty(false);
                editorController.getEditorModel().refresh();
                FreeMarsOptionPane.showMessageDialog(editorController.getEditorFrame(), "Save complete");
            } catch (Exception exception) {
            }
        } else {
            editorController.getAction(EditorController.SAVE_MAP_AS).actionPerformed(e);
        }
    }
}
