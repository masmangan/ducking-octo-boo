package org.freemars.editor.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.editor.EditorModel;

/**
 *
 * @author Deniz ARIKAN
 */
public class ToggleEditorMapShowTileTypesAction extends AbstractAction {

    private EditorModel editorData;

    public ToggleEditorMapShowTileTypesAction(EditorModel editorData) {
        super("Show tile types", null);
        this.editorData = editorData;
    }

    public void actionPerformed(ActionEvent e) {
        editorData.setEditorMapShowingTileTypes(!editorData.isEditorMapShowingTileTypes());
    }
}
