package org.freemars.editor.controller;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import org.freemars.editor.EditorModel;

/**
 *
 * @author Deniz ARIKAN
 */
public class SetEditorPanelZoomLevelAction extends AbstractAction {

    private EditorModel editorData;
    private int zoomLevel;

    public SetEditorPanelZoomLevelAction(EditorModel editorData, int zoomLevel) {
        super(String.valueOf(zoomLevel), null);
        this.editorData = editorData;
        this.zoomLevel = zoomLevel;
        putValue(SHORT_DESCRIPTION, "Set zoom level to : " + String.valueOf(zoomLevel));
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_1 + zoomLevel - 1));
    }

    public void actionPerformed(ActionEvent e) {
        editorData.setEditorMapZoomLevel(zoomLevel);
    }
}
