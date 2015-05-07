package org.freemars.editor.menu;

import javax.swing.JCheckBoxMenuItem;
import org.freerealm.tile.TileType;

/**
 *
 * @author Deniz ARIKAN
 */
public class TileTypeCheckBoxMenuItem extends JCheckBoxMenuItem {

    private TileType tileType;

    public TileTypeCheckBoxMenuItem(TileType tileType) {
        this.tileType = tileType;
        setText(tileType.getName());
    }

    public TileType getTileType() {
        return tileType;
    }
}
