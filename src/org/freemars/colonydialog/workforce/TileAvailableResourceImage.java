package org.freemars.colonydialog.workforce;

import java.awt.Image;

/**
 *
 * @author Deniz ARIKAN
 */
public class TileAvailableResourceImage {

    private Image image;
    private int count;

    public TileAvailableResourceImage(Image image, int count) {
        this.image = image;
        this.count = count;
    }

    public Image getImage() {
        return image;
    }

    public int getCount() {
        return count;
    }
}
