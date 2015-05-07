package org.freemars.colony.manager;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author Deniz ARIKAN
 */
public class SetColoniesTableModeAction extends AbstractAction {

    private ColonyManagerDialog colonyManagerDialog;
    private int mode;

    public SetColoniesTableModeAction(ColonyManagerDialog colonyManagerDialog, int mode) {
        this.colonyManagerDialog = colonyManagerDialog;
        this.mode = mode;
    }

    public void actionPerformed(ActionEvent e) {
        colonyManagerDialog.setMode(mode);
    }
}
