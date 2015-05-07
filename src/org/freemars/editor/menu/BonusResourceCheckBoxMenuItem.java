package org.freemars.editor.menu;

import javax.swing.JCheckBoxMenuItem;
import org.freerealm.resource.bonus.BonusResource;

/**
 *
 * @author Deniz ARIKAN
 */
public class BonusResourceCheckBoxMenuItem extends JCheckBoxMenuItem {

    private BonusResource bonusResource;

    public BonusResourceCheckBoxMenuItem(BonusResource bonusResource) {
        this.bonusResource = bonusResource;
        if (bonusResource != null) {
            setText(bonusResource.getName());
        } else {
            setText("None");
        }
    }

    public BonusResource getBonusResource() {
        return bonusResource;
    }
}
