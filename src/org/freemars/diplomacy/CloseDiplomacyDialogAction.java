package org.freemars.diplomacy;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author Deniz ARIKAN
 */
public class CloseDiplomacyDialogAction extends AbstractAction {

    private DiplomacyDialog diplomacyDialog;

    public CloseDiplomacyDialogAction(DiplomacyDialog diplomacyDialog) {
        super("Close", null);
        this.diplomacyDialog = diplomacyDialog;
    }

    public void actionPerformed(ActionEvent e) {
        diplomacyDialog.dispose();
    }
}
