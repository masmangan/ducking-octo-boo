package org.freemars.controller.action;

import org.freemars.controller.FreeMarsController;
import org.freerealm.executor.command.SkipUnitCommand;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freerealm.Realm;
import org.freerealm.player.Player;
import org.freerealm.unit.Unit;

/**
 *
 * @author Deniz ARIKAN
 */
public class SkipUnitAction extends AbstractAction {

    private FreeMarsController freeMarsController;
    private final Unit unit;

    public SkipUnitAction(FreeMarsController freeMarsController, Unit unit) {
        super("Skip unit");
        this.freeMarsController = freeMarsController;
        this.unit = unit;
    }

    public void actionPerformed(ActionEvent e) {
        Unit unitToOrder = unit != null ? unit : freeMarsController.getFreeMarsModel().getActivePlayer().getActiveUnit();
        freeMarsController.execute((new SkipUnitCommand(unitToOrder)));
    }

    public boolean checkEnabled() {
        Player activePlayer = freeMarsController.getFreeMarsModel().getActivePlayer();
        if (activePlayer == null) {
            return false;
        }
        Unit unitToOrder = unit != null ? unit : freeMarsController.getFreeMarsModel().getActivePlayer().getActiveUnit();
        return unitToOrder != null;
    }
}
