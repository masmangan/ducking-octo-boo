package org.freemars.diplomacy;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freerealm.diplomacy.Diplomacy;

/**
 *
 * @author Deniz ARIKAN
 */
public class DeclareWarAction extends AbstractAction {

    private DiplomacyDialogModel diplomacyDialogModel;

    public DeclareWarAction(DiplomacyDialogModel diplomacyDialogModel) {
        super("Declare war", null);
        this.diplomacyDialogModel = diplomacyDialogModel;
    }

    public void actionPerformed(ActionEvent e) {
        if (diplomacyDialogModel.getRelatedPlayer() != null) {
            diplomacyDialogModel.addPlayerRelation(diplomacyDialogModel.getPlayer(), diplomacyDialogModel.getRelatedPlayer(), Diplomacy.AT_WAR);
        }
    }
}
