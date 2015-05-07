package org.freemars.editor.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.editor.EditorModel;

/**
 *
 * @author Deniz ARIKAN
 */
public class ToggleEditorMapShowCoordinatesAction extends AbstractAction {

    private EditorModel editorData;

    public ToggleEditorMapShowCoordinatesAction(EditorModel editorData) {
        super("Show coordinate", null);
        this.editorData = editorData;
    }

    public void actionPerformed(ActionEvent e) {
        editorData.setEditorMapShowingCoordinates(!editorData.isEditorMapShowingCoordinates());
    }
}
