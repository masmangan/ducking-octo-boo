package org.freemars.editor.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.editor.EditorModel;

/**
 *
 * @author Deniz ARIKAN
 */
public class EditorMapZoomOutAction extends AbstractAction {

    private EditorModel editorData;

    public EditorMapZoomOutAction(EditorModel editorData) {
        super("Zoom out", null);
        this.editorData = editorData;
    }

    public void actionPerformed(ActionEvent e) {
        editorData.setEditorMapZoomLevel(editorData.getEditorMapZoomLevel() - 1);
    }
}
