package org.freemars.controller.action;

import org.freemars.controller.FreeMarsController;
import org.freerealm.executor.command.MoveUnitCommand;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.controller.viewcommand.PlaySoundCommand;
import org.freerealm.map.Direction;
import org.freerealm.executor.command.SkipUnitCommand;
import org.freerealm.map.Coordinate;
import org.freerealm.tile.Tile;
import org.freerealm.Realm;
import org.freerealm.Utility;
import org.freerealm.executor.command.CaptureSettlementCommand;
import org.freerealm.unit.Unit;

/**
 *
 * @author Deniz ARIKAN
 */
public class MoveUnitAction extends AbstractAction {

    private FreeMarsController freeMarsController;
    private Direction direction;

    public MoveUnitAction(FreeMarsController controller, Direction direction) {
        this.freeMarsController = controller;
        this.direction = direction;
    }

    public void actionPerformed(ActionEvent e) {
        Realm realm = freeMarsController.getFreeMarsModel().getRealm();
        Unit unit = realm.getPlayerManager().getActivePlayer().getActiveUnit();
        if (unit != null && unit.getCurrentOrder() == null && unit.getMovementPoints() > 0) {
            Coordinate toCoordinate = realm.getRelativeCoordinate(unit.getCoordinate(), direction);
            if (toCoordinate != null) {
                Tile toTile = realm.getTile(toCoordinate);
                if (attackNeeded(toTile, unit)) {
                    Unit defendingUnit = toTile.getUnits().get(toTile.getUnits().firstKey());
                    new DisplayAttackUnitDialogAction(freeMarsController, unit, defendingUnit).actionPerformed(e);
                } else if (invadeColonyNeeded(toTile, unit)) {
                    freeMarsController.execute(new CaptureSettlementCommand(realm, unit, toTile.getSettlement()));
                } else {
                    boolean unitMovementSound = Boolean.valueOf(freeMarsController.getFreeMarsModel().getFreeMarsPreferences().getProperty("unit_movement_sound"));
                    if (unitMovementSound) {
                        freeMarsController.executeViewCommandImmediately(new PlaySoundCommand("sound/unit_movement.wav"));
                    }
                    MoveUnitCommand moveUnitCommand = new MoveUnitCommand(realm, unit, toCoordinate);
                    freeMarsController.execute(moveUnitCommand);
                }
                if (unit.getMovementPoints() == 0) {
                    freeMarsController.execute(new SkipUnitCommand(unit));
                }
            }
        }
    }

    private boolean attackNeeded(Tile toTile, Unit unit) {
        if (toTile.getNumberOfUnits() > 0) {
            Unit defendingUnit = toTile.getUnits().get(toTile.getUnits().firstKey());
            if (!unit.getPlayer().equals(defendingUnit.getPlayer())) {
                return true;
            }
        }
        return false;
    }

    private boolean invadeColonyNeeded(Tile toTile, Unit unit) {
        return (toTile.getSettlement() != null) && (!unit.getPlayer().equals((toTile.getSettlement().getPlayer())));
    }
}
