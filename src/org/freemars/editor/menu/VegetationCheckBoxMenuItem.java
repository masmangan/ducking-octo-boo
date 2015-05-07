package org.freemars.editor.menu;

import javax.swing.JCheckBoxMenuItem;
import org.freerealm.vegetation.VegetationType;

/**
 *
 * @author Deniz ARIKAN
 */
public class VegetationCheckBoxMenuItem extends JCheckBoxMenuItem {

    private VegetationType vegetation;

    public VegetationCheckBoxMenuItem(VegetationType vegetation) {
        this.vegetation = vegetation;
        if (vegetation != null) {
            setText(vegetation.getName());
        } else {
            setText("None");
        }
    }

    public VegetationType getVegetation() {
        return vegetation;
    }
}
