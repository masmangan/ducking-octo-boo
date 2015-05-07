package org.freemars.editor.menu;

import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import org.freemars.editor.EditorModel;
import org.freemars.editor.controller.NewEmptyMapAction;

/**
 *
 * @author Deniz ARIKAN
 */
public class EditorMenu extends JMenuBar implements Observer {

    private EditorModel editorData;
    private JMenu fileMenu;
    private JMenuItem newEmptyMapMenuItem;
    private JMenuItem openMapMenuItem;
    private JMenuItem saveMapMenuItem;
    private JMenuItem saveMapAsMenuItem;
    private JMenuItem resizeMapMenuItem;
    private JMenuItem displayMapInformationMenuItem;
    private JMenuItem quitToMainMenuMenuItem;
    private JMenuItem exitToSystemMenuItem;
    private BrushMenu brushMenu;
    private JMenu viewMenu;
    private JCheckBoxMenuItem showGridMenuItem;
    private JCheckBoxMenuItem showCoordinatesMenuItem;
    private JCheckBoxMenuItem showTileTypesMenuItem;
    private JMenuItem editorMapZoomInMenuItem;
    private JMenuItem editorMapZoomOutMenuItem;
    private JMenu zoomLevelMenu;
    private JCheckBoxMenuItem[] zoomLevelMenuItems;

    public EditorMenu(EditorModel editorData) {
        this.editorData = editorData;
        add(getFileMenu());
        add(getBrushMenu());
        add(getViewMenu());
    }

    public void update(Observable o, Object arg) {
        if (arg != null) {
            if (arg.equals(EditorModel.BRUSH_UPDATE)) {
                getBrushMenu().refresh();
            } else if (arg.equals(EditorModel.MAP_OPTIONS_UPDATE)) {
                getShowGridMenuItem().setSelected(editorData.isEditorMapShowingGrid());
                getShowCoordinatesMenuItem().setSelected(editorData.isEditorMapShowingCoordinates());
                getShowTileTypesMenuItem().setSelected(editorData.isEditorMapShowingTileTypes());
                selectZoomLevelMenuItem(editorData.getEditorMapZoomLevel());
            }
        }
    }

    public void setNewEmptyMapMenuItemAction(AbstractAction action) {
        getNewEmptyMapMenuItem().setAction(action);
    }

    public void setOpenMapMenuItemAction(AbstractAction action) {
        getOpenMapMenuItem().setAction(action);
    }

    public void setSaveMapMenuItemAction(AbstractAction action) {
        getSaveMapMenuItem().setAction(action);
    }

    public void setSaveMapAsMenuItemAction(AbstractAction action) {
        getSaveMapAsMenuItem().setAction(action);
    }

    public void setDisplayMapInformationMenuItemAction(AbstractAction action) {
        getDisplayMapInformationMenuItem().setAction(action);
    }

    public void setResizeMapMenuItemAction(AbstractAction action) {
        getResizeMapMenuItem().setAction(action);
    }

    public void setQuitToMainMenuMenuItemAction(AbstractAction action) {
        getQuitToMainMenuMenuItem().setAction(action);
    }

    public void setExitToSystemMenuItemAction(AbstractAction action) {
        getExitToSystemMenuItem().setAction(action);
    }

    public void setShowGridMenuItemAction(AbstractAction action) {
        getShowGridMenuItem().setAction(action);
    }

    public void setShowCoordinatesMenuItemAction(AbstractAction action) {
        getShowCoordinatesMenuItem().setAction(action);
    }

    public void setShowTileTypesMenuItemAction(AbstractAction action) {
        getShowTileTypesMenuItem().setAction(action);
    }

    public void setEditorMapZoomInMenuItemAction(AbstractAction action) {
        getEditorMapZoomInMenuItem().setAction(action);
    }

    public void setEditorMapZoomOutMenuItemAction(Action action) {
        getEditorMapZoomOutMenuItem().setAction(action);
    }

    public void setTileTypeSelectionMenuActions(Vector<Action> actions) {
        for (int i = 0; i < getZoomLevelMenuItems().length; i++) {
            getZoomLevelMenuItems()[i].setAction(actions.get(i));
        }
    }

    private JMenu getFileMenu() {
        if (fileMenu == null) {
            fileMenu = new JMenu("File");
            fileMenu.setMnemonic(new Integer(KeyEvent.VK_F));
            fileMenu.add(getNewEmptyMapMenuItem());
            fileMenu.add(getOpenMapMenuItem());
            fileMenu.add(getSaveMapMenuItem());
            fileMenu.add(getSaveMapAsMenuItem());
            fileMenu.add(getResizeMapMenuItem());
            fileMenu.add(getDisplayMapInformationMenuItem());
            fileMenu.add(getQuitToMainMenuMenuItem());
            fileMenu.add(getExitToSystemMenuItem());
        }
        return fileMenu;
    }

    private JMenuItem getNewEmptyMapMenuItem() {
        if (newEmptyMapMenuItem == null) {
            newEmptyMapMenuItem = new JMenuItem();
            newEmptyMapMenuItem.setAction(new NewEmptyMapAction(null));
        }
        return newEmptyMapMenuItem;
    }

    private JMenuItem getOpenMapMenuItem() {
        if (openMapMenuItem == null) {
            openMapMenuItem = new JMenuItem();
        }
        return openMapMenuItem;
    }

    private JMenuItem getSaveMapMenuItem() {
        if (saveMapMenuItem == null) {
            saveMapMenuItem = new JMenuItem();
        }
        return saveMapMenuItem;
    }

    private JMenuItem getSaveMapAsMenuItem() {
        if (saveMapAsMenuItem == null) {
            saveMapAsMenuItem = new JMenuItem();
        }
        return saveMapAsMenuItem;
    }

    private JMenuItem getResizeMapMenuItem() {
        if (resizeMapMenuItem == null) {
            resizeMapMenuItem = new JMenuItem();
        }
        return resizeMapMenuItem;
    }

    private JMenuItem getDisplayMapInformationMenuItem() {
        if (displayMapInformationMenuItem == null) {
            displayMapInformationMenuItem = new JMenuItem();
        }
        return displayMapInformationMenuItem;
    }

    private JMenuItem getQuitToMainMenuMenuItem() {
        if (quitToMainMenuMenuItem == null) {
            quitToMainMenuMenuItem = new JMenuItem();
        }
        return quitToMainMenuMenuItem;
    }

    private JMenuItem getExitToSystemMenuItem() {
        if (exitToSystemMenuItem == null) {
            exitToSystemMenuItem = new JMenuItem();
        }
        return exitToSystemMenuItem;
    }

    private BrushMenu getBrushMenu() {
        if (brushMenu == null) {
            brushMenu = new BrushMenu(editorData);
        }
        return brushMenu;
    }

    private JMenu getViewMenu() {
        if (viewMenu == null) {
            viewMenu = new JMenu("View");
            viewMenu.setMnemonic(new Integer(KeyEvent.VK_V));
            viewMenu.add(getShowGridMenuItem());
            viewMenu.add(getShowCoordinatesMenuItem());
            viewMenu.add(getShowTileTypesMenuItem());
            viewMenu.add(new JSeparator());
            viewMenu.add(getEditorMapZoomInMenuItem());
            viewMenu.add(getEditorMapZoomOutMenuItem());
            viewMenu.add(getZoomLevelMenu());
        }
        return viewMenu;
    }

    private JCheckBoxMenuItem getShowGridMenuItem() {
        if (showGridMenuItem == null) {
            showGridMenuItem = new JCheckBoxMenuItem();
        }
        return showGridMenuItem;
    }

    private JCheckBoxMenuItem getShowCoordinatesMenuItem() {
        if (showCoordinatesMenuItem == null) {
            showCoordinatesMenuItem = new JCheckBoxMenuItem();
        }
        return showCoordinatesMenuItem;
    }

    private JCheckBoxMenuItem getShowTileTypesMenuItem() {
        if (showTileTypesMenuItem == null) {
            showTileTypesMenuItem = new JCheckBoxMenuItem();
        }
        return showTileTypesMenuItem;
    }

    private JMenuItem getEditorMapZoomInMenuItem() {
        if (editorMapZoomInMenuItem == null) {
            editorMapZoomInMenuItem = new JMenuItem();
        }
        return editorMapZoomInMenuItem;
    }

    private JMenuItem getEditorMapZoomOutMenuItem() {
        if (editorMapZoomOutMenuItem == null) {
            editorMapZoomOutMenuItem = new JMenuItem();
        }
        return editorMapZoomOutMenuItem;
    }

    private JMenu getZoomLevelMenu() {
        zoomLevelMenu = new JMenu("Zoom level");
        for (int i = 0; i < getZoomLevelMenuItems().length; i++) {
            zoomLevelMenu.add(getZoomLevelMenuItems()[i]);
        }
        return zoomLevelMenu;
    }

    public void selectZoomLevelMenuItem(int index) {
        for (int i = 0; i < zoomLevelMenuItems.length; i++) {
            getZoomLevelMenuItems()[i].setSelected(false);
        }
        getZoomLevelMenuItems()[index].setSelected(true);
    }

    private JCheckBoxMenuItem[] getZoomLevelMenuItems() {
        if (zoomLevelMenuItems == null) {
            zoomLevelMenuItems = new JCheckBoxMenuItem[6];
            for (int i = 0; i < zoomLevelMenuItems.length; i++) {
                zoomLevelMenuItems[i] = new JCheckBoxMenuItem();
            }
        }
        return zoomLevelMenuItems;
    }

    public void setZoomLevelMenuActions(Vector<Action> actions) {
        for (int i = 0; i < getZoomLevelMenuItems().length; i++) {
            getZoomLevelMenuItems()[i].setAction(actions.get(i));
        }
    }
}
