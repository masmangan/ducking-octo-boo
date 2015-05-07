package org.freemars.editor;

import java.awt.BorderLayout;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.freemars.ui.util.FreeMarsFrame;
import org.freemars.editor.menu.EditorMenu;

/**
 *
 * @author Deniz ARIKAN
 */
public class EditorFrame extends FreeMarsFrame implements Observer {

    private EditorModel editorData;
    private JPanel editorMainPanel;
    private EditorMapPanel editorMapPanel;
    private EditorMenu editorMenuBar;

    public EditorFrame(EditorModel editorData) {
        this.editorData = editorData;
        setJMenuBar(getEditorMenu());
        setContentPane(getEditorMainPanel());
        pack();
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void update(Observable o, Object arg) {
        String title = editorData.getDefaultFrameTitle();
        if (editorData.getFileName() != null) {
            title = title + " - " + editorData.getFileName();
        }
        if (editorData.isFileDirty()) {
            title = title + " *";
        }
        setTitle(title);
        getEditorMenu().update(o, arg);
        getEditorMapPanel().update(o, arg);
        getEditorMapPanel().requestFocus();
    }

    public EditorMenu getEditorMenu() {
        if (editorMenuBar == null) {
            editorMenuBar = new EditorMenu(editorData);
        }
        return editorMenuBar;
    }

    private JPanel getEditorMainPanel() {
        if (editorMainPanel == null) {
            editorMainPanel = new JPanel(new BorderLayout());
            editorMainPanel.add(getEditorMapPanel(), BorderLayout.CENTER);
        }
        return editorMainPanel;
    }

    private EditorMapPanel getEditorMapPanel() {
        if (editorMapPanel == null) {
            editorMapPanel = new EditorMapPanel(editorData);
        }
        return editorMapPanel;
    }

    public void addEditorMapPanelMouseListener(MouseListener mouseListener) {
        getEditorMapPanel().addMouseListener(mouseListener);
    }
}
