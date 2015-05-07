package org.freemars.editor.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.editor.EditorModel;

/**
 *
 * @author Deniz ARIKAN
 */
public class ToggleEditorMapGridAction extends AbstractAction {

    private EditorModel editorData;

    public ToggleEditorMapGridAction(EditorModel editorData) {
        super("Show grid", null);
        this.editorData = editorData;
    }

    public void actionPerformed(ActionEvent e) {
        editorData.setEditorMapShowingGrid(!editorData.isEditorMapShowingGrid());
    }
}
