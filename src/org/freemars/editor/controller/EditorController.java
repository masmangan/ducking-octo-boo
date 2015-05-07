package org.freemars.editor.controller;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.freemars.editor.EditorModel;
import org.freemars.editor.EditorFrame;
import org.freemars.editor.EditorMapPanelMouseListener;

/**
 *
 * @author Deniz ARIKAN
 */
public class EditorController implements Observer {

    public static final int MAIN_MAP_ZOOM_IN = 0;
    public static final int MAIN_MAP_ZOOM_OUT = 1;
    public static final int TOGGLE_MAP_GRID = 2;
    public static final int TOGGLE_MAP_SHOW_COORDINATES = 3;
    public static final int TOGGLE_MAP_SHOW_TILE_TYPES = 4;
    public static final int OPEN_MAP = 5;
    public static final int SAVE_MAP = 6;
    public static final int SAVE_MAP_AS = 7;
    public static final int RESIZE_MAP = 8;
    public static final int DISPLAY_MAP_INFORMATION = 9;
    public static final int QUIT_TO_MAIN_MENU = 10;
    public static final int EXIT_TO_SYSTEM = 11;
    public static final int NEW_EMPTY_MAP = 12;
    private EditorModel editorModel;
    private EditorFrame editorFrame;
    private AbstractAction quitEditorAction;
    private HashMap<Integer, AbstractAction> actions;

    public EditorController() {
        editorModel = new EditorModel();
        editorFrame = new EditorFrame(editorModel);
        editorModel.addObserver(editorFrame);
        editorModel.addObserver(this);
        actions = new HashMap<Integer, AbstractAction>();
        actions.put(MAIN_MAP_ZOOM_IN, new EditorMapZoomInAction(editorModel));
        actions.put(MAIN_MAP_ZOOM_OUT, new EditorMapZoomOutAction(editorModel));
        actions.put(TOGGLE_MAP_GRID, new ToggleEditorMapGridAction(editorModel));
        actions.put(TOGGLE_MAP_SHOW_COORDINATES, new ToggleEditorMapShowCoordinatesAction(editorModel));
        actions.put(TOGGLE_MAP_SHOW_TILE_TYPES, new ToggleEditorMapShowTileTypesAction(editorModel));
        actions.put(NEW_EMPTY_MAP, new NewEmptyMapAction(this));
        actions.put(OPEN_MAP, new OpenMapAction(this));
        actions.put(SAVE_MAP, new SaveMapAction(this));
        actions.put(SAVE_MAP_AS, new SaveMapAsAction(this));
        actions.put(RESIZE_MAP, new ResizeMapAction(this));
        actions.put(DISPLAY_MAP_INFORMATION, new DisplayMapInformationAction(this));
        actions.put(QUIT_TO_MAIN_MENU, new QuitEditorToMainMenuAction(this));
        actions.put(EXIT_TO_SYSTEM, new ExitToSystemAction(this));

        editorFrame.addEditorMapPanelMouseListener(new EditorMapPanelMouseListener(editorModel));
        editorFrame.getEditorMenu().setNewEmptyMapMenuItemAction(getAction(NEW_EMPTY_MAP));
        editorFrame.getEditorMenu().setOpenMapMenuItemAction(getAction(OPEN_MAP));
        editorFrame.getEditorMenu().setSaveMapMenuItemAction(getAction(SAVE_MAP));
        editorFrame.getEditorMenu().setSaveMapAsMenuItemAction(getAction(SAVE_MAP_AS));
        editorFrame.getEditorMenu().setResizeMapMenuItemAction(getAction(RESIZE_MAP));
        editorFrame.getEditorMenu().setDisplayMapInformationMenuItemAction(getAction(DISPLAY_MAP_INFORMATION));
        editorFrame.getEditorMenu().setQuitToMainMenuMenuItemAction(getAction(QUIT_TO_MAIN_MENU));
        editorFrame.getEditorMenu().setExitToSystemMenuItemAction(getAction(EXIT_TO_SYSTEM));
        editorFrame.getEditorMenu().setEditorMapZoomInMenuItemAction(getAction(MAIN_MAP_ZOOM_IN));
        editorFrame.getEditorMenu().setEditorMapZoomOutMenuItemAction(getAction(MAIN_MAP_ZOOM_OUT));
        editorFrame.getEditorMenu().setShowGridMenuItemAction(getAction(TOGGLE_MAP_GRID));
        editorFrame.getEditorMenu().setShowCoordinatesMenuItemAction(getAction(TOGGLE_MAP_SHOW_COORDINATES));
        editorFrame.getEditorMenu().setShowTileTypesMenuItemAction(getAction(TOGGLE_MAP_SHOW_TILE_TYPES));

        Vector<Action> actionsVector = new Vector<Action>();
        for (int i = 0; i < 6; i++) {
            actionsVector.add(new SetEditorPanelZoomLevelAction(getEditorModel(), i));
        }
        editorFrame.getEditorMenu().setZoomLevelMenuActions(actionsVector);
    }

    public void displayEditorFrame() {
        getEditorModel().addObserver(getEditorFrame());
        getEditorFrame().setVisible(true);
        getEditorModel().refresh();
    }

    public void update(Observable o, Object arg) {
        if (getEditorModel().getEditorMapZoomLevel() < 5) {
            getAction(MAIN_MAP_ZOOM_IN).setEnabled(true);
        }
        if (getEditorModel().getEditorMapZoomLevel() > 0) {
            getAction(MAIN_MAP_ZOOM_OUT).setEnabled(true);
        }
    }

    public AbstractAction getAction(int key) {
        return actions.get(key);
    }

    public EditorModel getEditorModel() {
        return editorModel;
    }

    public EditorFrame getEditorFrame() {
        return editorFrame;
    }

    public AbstractAction getQuitEditorAction() {
        return quitEditorAction;
    }

    public void setQuitEditorAction(AbstractAction quitEditorAction) {
        this.quitEditorAction = quitEditorAction;
    }
}
