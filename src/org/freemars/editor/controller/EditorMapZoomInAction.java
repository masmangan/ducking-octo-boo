package org.freemars.editor.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.editor.EditorModel;

/**
 *
 * @author Deniz ARIKAN
 */
public class EditorMapZoomInAction extends AbstractAction {

    private EditorModel editorData;

    public EditorMapZoomInAction(EditorModel editorData) {
        super("Zoom in", null);
        this.editorData = editorData;
    }

    public void actionPerformed(ActionEvent e) {
        editorData.setEditorMapZoomLevel(editorData.getEditorMapZoomLevel() + 1);
    }
}
