package org.freemars.editor;

import java.util.Observable;
import org.freerealm.map.Coordinate;
import org.freerealm.tile.TileType;
import org.freerealm.Realm;
import org.freerealm.executor.Command;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.DefaultExecutor;
import org.freerealm.executor.Executor;
import org.freerealm.executor.command.CreateMapCommand;
import org.freerealm.executor.command.InitializeRealmCommand;
import org.freerealm.resource.bonus.BonusResource;
import org.freerealm.vegetation.VegetationType;

/**
 *
 * @author Deniz ARIKAN
 */
public class EditorModel extends Observable {

    public static int BRUSH_UPDATE = 0;
    public static int MAP_OPTIONS_UPDATE = 1;
    private String fileName;
    private boolean fileDirty;
    private Realm realm;
    private TileType currentTileType;
    private VegetationType currentVegetation;
    private BonusResource bonusResource;
    private int editorMapZoomLevel;
    private boolean editorMapShowingGrid = false;
    private boolean editorMapShowingCoordinates = false;
    private boolean editorMapShowingTileTypes = false;
    private Coordinate centeredCoordinate = new Coordinate(0, 0);
    private Executor executor;

    public EditorModel() {
        this.realm = new Realm();
        execute(new InitializeRealmCommand(realm));
        TileType wastelandTileType = realm.getTileTypeManager().getTileType("Wasteland");
        execute(new CreateMapCommand(realm, wastelandTileType, 80, 80));
        setCurrentTileType(wastelandTileType);
        editorMapZoomLevel = 3;
        executor = new DefaultExecutor();
    }

    public CommandResult execute(Command command) {
        CommandResult commandResult = executor.execute(command);
        return commandResult;
    }

    public void refresh() {
        setChanged();
        notifyObservers(BRUSH_UPDATE);
        setChanged();
        notifyObservers(MAP_OPTIONS_UPDATE);
    }

    public void refresh(Object updateArg) {
        setChanged();
        notifyObservers(updateArg);
    }

    public Realm getRealm() {
        return realm;
    }

    public int getEditorMapZoomLevel() {
        return editorMapZoomLevel;
    }

    public void setEditorMapZoomLevel(int editorMapZoomLevel) {
        this.editorMapZoomLevel = editorMapZoomLevel;
        refresh(MAP_OPTIONS_UPDATE);
    }

    public boolean isEditorMapShowingGrid() {
        return editorMapShowingGrid;
    }

    public void setEditorMapShowingGrid(boolean editorMapShowingGrid) {
        this.editorMapShowingGrid = editorMapShowingGrid;
        refresh(MAP_OPTIONS_UPDATE);
    }

    public Coordinate getCenteredCoordinate() {
        return centeredCoordinate;
    }

    public void setCenteredCoordinate(Coordinate centeredCoordinate) {
        this.centeredCoordinate = centeredCoordinate;
        refresh(MAP_OPTIONS_UPDATE);
    }

    public boolean isEditorMapShowingCoordinates() {
        return editorMapShowingCoordinates;
    }

    public void setEditorMapShowingCoordinates(boolean editorMapShowingCoordinates) {
        this.editorMapShowingCoordinates = editorMapShowingCoordinates;
        refresh(MAP_OPTIONS_UPDATE);
    }

    public boolean isEditorMapShowingTileTypes() {
        return editorMapShowingTileTypes;
    }

    public void setEditorMapShowingTileTypes(boolean editorMapShowingTileTypes) {
        this.editorMapShowingTileTypes = editorMapShowingTileTypes;
        refresh(MAP_OPTIONS_UPDATE);
    }

    public TileType getCurrentTileType() {
        return currentTileType;
    }

    public void setCurrentTileType(TileType currentTileType) {
        this.currentTileType = currentTileType;
        if (getCurrentVegetation() != null && !getCurrentVegetation().canGrowOnTileType(currentTileType)) {
            setCurrentVegetation(null);
        } else {
            refresh(BRUSH_UPDATE);
        }
    }

    public VegetationType getCurrentVegetation() {
        return currentVegetation;
    }

    public void setCurrentVegetation(VegetationType currentVegetation) {
        this.currentVegetation = currentVegetation;
        refresh(BRUSH_UPDATE);
    }

    public BonusResource getBonusResource() {
        return bonusResource;
    }

    public void setBonusResource(BonusResource bonusResource) {
        this.bonusResource = bonusResource;
        refresh(BRUSH_UPDATE);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDefaultFrameTitle() {
        return "Free Mars Editor";
    }

    public boolean isFileDirty() {
        return fileDirty;
    }

    public void setFileDirty(boolean fileDirty) {
        this.fileDirty = fileDirty;
    }
}
