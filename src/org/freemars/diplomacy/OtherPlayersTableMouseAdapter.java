package org.freemars.diplomacy;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Deniz ARIKAN
 */
public class OtherPlayersTableMouseAdapter extends MouseAdapter {

    private DiplomacyDialogModel diplomacyDialogModel;
    private DiplomacyDialog diplomacyDialog;

    public OtherPlayersTableMouseAdapter(DiplomacyDialogModel diplomacyDialogModel, DiplomacyDialog diplomacyDialog) {
        this.diplomacyDialogModel = diplomacyDialogModel;
        this.diplomacyDialog = diplomacyDialog;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        diplomacyDialogModel.setRelatedPlayer(diplomacyDialog.getSelectedOtherPlayer());
    }
}
