package org.freemars.editor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.Observable;
import org.freerealm.map.Coordinate;
import org.freerealm.tile.Tile;
import java.util.Observer;
import org.freemars.ui.image.FreeMarsImageManager;
import org.freemars.ui.map.RealmGridPanel;

/**
 *
 * @author Deniz ARIKAN
 */
public class EditorMapPanel extends RealmGridPanel implements Observer {

    private EditorModel editorData;

    public EditorMapPanel(EditorModel editorData) {
        super(editorData.getRealm(), 128, 64);
        setBackground(new Color(3, 14, 56));
        setFocusable(true);
        this.editorData = editorData;
    }

    public void update(Observable o, Object arg) {
        setZoomLevel(editorData.getEditorMapZoomLevel());
        setGridCounts();
        centerToCoordinate(editorData.getCenteredCoordinate());
        repaint();
        revalidate();
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        update(null, null);
    }

    // <editor-fold defaultstate="collapsed" desc="Paint methods">
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if ((getOrdinateOffset() + getVerticalGrids()) > getRealm().getMapHeight()) {
            setOrdinateOffset(getRealm().getMapHeight() - getVerticalGrids());
        }
        for (int ordinate = (getOrdinateOffset() > 0 ? -1 : 0); ordinate < getVerticalGrids(); ordinate++) {
            for (int abscissa = -1; abscissa < getHorizontalGrids(); abscissa++) {
                Coordinate relativeCoordinate = new Coordinate(abscissa, ordinate);
                Coordinate worldCoordinate = getWorldCoordinate(relativeCoordinate);
                Point paintPoint = getPaintPoint(relativeCoordinate);
                paintTerrain(g2d, worldCoordinate, paintPoint);
                paintTerrainVegetation(g2d, worldCoordinate, paintPoint);
                paintTerrainBonusResource(g2d, worldCoordinate, paintPoint);
                if (editorData.isEditorMapShowingGrid()) {
                    paintRhombus(g2d, paintPoint);
                }
                if (editorData.isEditorMapShowingCoordinates()) {
                    paintText(g2d, paintPoint, new Font("Arial", 0, 12), Color.WHITE, worldCoordinate.toString());
                }
                if (editorData.isEditorMapShowingTileTypes()) {
                    Tile tile = getRealm().getTile(worldCoordinate);
                    paintText(g2d, paintPoint, new Font("Arial", 0, 12), Color.WHITE, tile.getType().getName());
                }
            }
        }
    }

    protected void paintTerrain(Graphics2D g2d, Coordinate worldCoordinate, Point paintPoint) {
        Tile tile = getRealm().getTile(worldCoordinate);
        Image image = FreeMarsImageManager.getInstance().getImage(tile);
        Image defaultTileImage = FreeMarsImageManager.getInstance().getImage(FreeMarsImageManager.DEFAULT_TILE);
        g2d.drawImage(defaultTileImage, (int) paintPoint.getX(), (int) paintPoint.getY(), getGridWidth(), getGridHeight(), this);
        g2d.drawImage(image, (int) paintPoint.getX(), (int) paintPoint.getY(), getGridWidth(), getGridHeight(), this);
    }

    protected void paintTerrainVegetation(Graphics2D g2d, Coordinate worldCoordinate, Point paintPoint) {
        Tile tile = getRealm().getTile(worldCoordinate);
        if (tile != null && tile.getVegetation() != null) {
            Image image = FreeMarsImageManager.getInstance().getImage(tile.getVegetation());
            g2d.drawImage(image, (int) paintPoint.getX(), (int) paintPoint.getY(), getGridWidth(), getGridHeight(), this);
        }
    }

    protected void paintTerrainBonusResource(Graphics2D g2d, Coordinate worldCoordinate, Point paintPoint) {
        Tile tile = getRealm().getTile(worldCoordinate);
        if (tile != null && tile.getBonusResource() != null) {
            Image image = FreeMarsImageManager.getInstance().getImage(tile.getBonusResource());
            g2d.drawImage(image, (int) paintPoint.getX() + 40, (int) paintPoint.getY(), 40, 40, this);
        }
    }

    // </editor-fold>
    private void setZoomLevel(int zoomLevel) {
        setGridHeight(16 * (zoomLevel + 1));
        setGridWidth(32 * (zoomLevel + 1));
    }
}
