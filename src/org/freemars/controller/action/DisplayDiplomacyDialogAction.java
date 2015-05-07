package org.freemars.controller.action;

import org.freemars.controller.FreeMarsController;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.diplomacy.CloseDiplomacyDialogAction;
import org.freemars.diplomacy.DeclareWarAction;
import org.freemars.diplomacy.DiplomacyDialog;
import org.freemars.diplomacy.DiplomacyDialogModel;
import org.freemars.diplomacy.OtherPlayersTableMouseAdapter;

/**
 *
 * @author Deniz ARIKAN
 */
public class DisplayDiplomacyDialogAction extends AbstractAction {

    private FreeMarsController controller;

    public DisplayDiplomacyDialogAction(FreeMarsController controller) {
        super("Diplomacy", null);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        DiplomacyDialogModel diplomacyDialogModel = new DiplomacyDialogModel();
        diplomacyDialogModel.setFreeMarsModel(controller.getFreeMarsModel());
        diplomacyDialogModel.setPlayer(controller.getFreeMarsModel().getActivePlayer());
        DiplomacyDialog diplomacyDialog = new DiplomacyDialog(controller.getCurrentFrame());
        diplomacyDialog.setModel(diplomacyDialogModel);
        diplomacyDialog.setOtherPlayersTableMouseAdapter(new OtherPlayersTableMouseAdapter(diplomacyDialogModel, diplomacyDialog));
        diplomacyDialog.setDeclareWarAction(new DeclareWarAction(diplomacyDialogModel));
        diplomacyDialog.setCloseAction(new CloseDiplomacyDialogAction(diplomacyDialog));
        diplomacyDialog.display();
    }
}
